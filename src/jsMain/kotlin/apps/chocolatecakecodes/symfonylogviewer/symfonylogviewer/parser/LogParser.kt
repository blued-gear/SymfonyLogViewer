package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.*
import kotlinx.coroutines.yield
import kotlinx.serialization.json.*
import kotlin.js.Date

internal class LogParser(
    val log: String,
) {

    companion object {

        val INVALID_DATE = Date(0)
    }

    suspend fun processLines(): List<LogLine> {
        var strIdx = 0
        var lineEndIdx = -1
        var line: String
        var lineNum = 1
        var json: JsonElement

        val ret = mutableListOf<LogLine>()
        while(lineEndIdx < log.lastIndex) {
            lineEndIdx = log.indexOf('\n', strIdx)
            line = log.substring(strIdx, lineEndIdx)

            try {
                json = Json.parseToJsonElement(line)
                val parsed: LogLine = tryParseHttpException(json, line)
                    ?: tryParseMessangerException(json, line)
                    ?: tryParseActivityHandlerLine(json, line)
                    ?: tryParseActivityPubManagerLine(json, line)
                    ?: tryParseDownloadErrorLine(json, line)
                    ?: tryParseUnknownLine(json, line)?.also { console.warn("encountered unknown line at line $lineNum") }
                    ?: fallbackLine(line).also { console.warn("encountered unparseable line at line $lineNum") }
                ret.add(parsed)
            } catch(e: Throwable) {
                console.warn("unable to parse line $lineNum", e)
                ret.add(fallbackLine(line))
            }

            strIdx = lineEndIdx + 1
            lineNum++

            // prevent blocking of the UI
            if(lineNum / 200 == 1)
                yield()
        }

        return ret
    }

    private fun tryParseHttpException(json: JsonElement, line: String): HttpExceptionLine? {
        val basics = parseBasics(json) ?: return null
        json as JsonObject

        val ctx = json.getAsObj("context") ?: return null

        val exceptionType = ctx.getAsString("e") ?: return null
        val httpType = ctx.getAsString("type")?.let {
            val sepIdx = it.indexOf(':')
            if (sepIdx == -1) return null
            return@let it.substring(sepIdx + 1)
        } ?: return null
        val httpAddr = ctx.getAsString("address") ?: return null
        val httpReqBody = ctx.getAsString("body") ?: return null
        val httpRespBody = ctx.getAsString("content") ?: return null
        val httpReqStatus = ctx.getAsString("msg")?.let {
            val promotor = ", status code: "
            val promotorIdx = it.indexOf(promotor) + promotor.length
            if(promotorIdx != promotor.length - 1) {
                val terminatorIdx = it.indexOf(",", promotorIdx)
                return@let it.substring(promotorIdx, terminatorIdx).toIntOrNull()
            } else {
                return@let 0
            }
        } ?: return null
        val ctxMessage = ctx.getAsString("msg") ?: return null

        return HttpExceptionLine(
            line,
            basics.date,
            basics.level,
            basics.channel,
            exceptionType,
            httpType,
            httpAddr,
            httpReqBody,
            httpReqStatus,
            httpRespBody,
            ctxMessage,
        )
    }

    private fun tryParseMessangerException(json: JsonElement, line: String): MessangerExceptionLine? {
        val basics = parseBasics(json) ?: return null
        json as JsonObject

        val ctx = json.getAsObj("context") ?: return null
        val exception = ctx.getAsObj("exception") ?: return null

        val message = json.getAsString("message") ?: return null
        val errorMessage = exception.getAsString("message") ?: return null
        val exceptionType = exception.getAsString("class") ?: return null
        val messageType = ctx.getAsString("class") ?: return null
        val messageId = ctx.getAsString("message_id") ?: "<null>"
        val retryCount = ctx.getAsInt("retryCount") ?: return null
        val file = exception.getAsString("file") ?: return null
        val prevErrHash = exception.getAsObj("previous")
            ?.getAsObj("previous")
            ?.getAsString("message")
            ?.hashCode() ?: -1

        return MessangerExceptionLine(
            line,
            basics.date,
            basics.level,
            basics.channel,
            message,
            errorMessage,
            exceptionType,
            messageType,
            messageId,
            retryCount,
            file,
            prevErrHash,
        )
    }

    private fun tryParseActivityPubManagerLine(json: JsonElement, line: String): ActivityPubManagerLine? {
        val basics = parseBasics(json) ?: return null
        json as JsonObject

        val ctx = json.getAsObj("context") ?: return null

        val message = json.getAsString("message") ?: return null
        val activityId = ctx.getAsString("id") ?: return null
        val activityActor = ctx.getAsString("actor") ?: return null
        val activityObject = ctx.getAsString("object") ?: return null
        val activityType = ctx.getAsString("type") ?: return null
        val activityAudience = ctx.getAsString("audience") ?: return null

        return ActivityPubManagerLine(
            line,
            basics.date,
            basics.level,
            basics.channel,
            message,
            activityId,
            activityActor,
            activityObject,
            activityType,
            activityAudience,
        )
    }

    private fun tryParseActivityHandlerLine(json: JsonElement, line: String): ActivityHandlerLine? {
        val basics = parseBasics(json) ?: return null
        json as JsonObject

        val ctx = json.getAsObj("context") ?: return null
        val message = json.getAsString("message") ?: return null

        return ActivityHandlerLine(
            line,
            basics.date,
            basics.level,
            basics.channel,
            message,
            ctx
        )
    }

    private fun tryParseDownloadErrorLine(json: JsonElement, line: String): DownloadErrorLine? {
        val basics = parseBasics(json) ?: return null
        json as JsonObject

        val ctx = json.getAsObj("context") ?: return null
        val message = json.getAsString("message") ?: return null
        if(!message.startsWith("couldn't download file")) return null
        val url = ctx.getAsString("url") ?: return null

        return DownloadErrorLine(
            line,
            basics.date,
            basics.level,
            basics.channel,
            message,
            url,
        )
    }

    private fun tryParseUnknownLine(json: JsonElement, line: String): UnknownLine? {
        if(json !is JsonObject) return null

        val level = json.getAsString("level_name")?.let {
            Level.parse(it)
        } ?: return null

        val date = json.getAsString("datetime")?.let {
            Date(it)
        } ?: return null

        return UnknownLine(line, date, level)
    }

    private fun fallbackLine(line: String): UnknownLine {
        return UnknownLine(line, INVALID_DATE, Level.UNKNOWN)
    }

    private fun parseBasics(json: JsonElement): Basics? {
        if(json !is JsonObject) return null

        val level = json.getAsString("level_name")?.let {
            Level.parse(it)
        } ?: return null

        val date = json.getAsString("datetime")?.let {
            Date(it)
        } ?: return null

        val channel = json.getAsString("channel") ?: return null

        return Basics(level, date, channel)
    }

    private fun JsonObject.getOrNull(key: String): JsonElement? {
        return this.getOrElse(key, { null })
    }

    private fun JsonObject.getAsString(key: String): String? {
        return this.getOrNull(key)?.let {
            if(it is JsonPrimitive && it.isString)
                it.content
            else
                null
        }
    }

    private fun JsonObject.getAsInt(key: String): Int? {
        return this.getOrNull(key)?.let {
            if(it is JsonPrimitive)
                it.intOrNull
            else
                null
        }
    }

    private fun JsonObject.getAsObj(key: String): JsonObject? {
        return this.getOrNull(key)?.let {
            it as? JsonObject
        }
    }
}

private class Basics(
    val level: Level,
    val date: Date,
    val channel: String,
)
