package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.domainFromUrl
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.getAsObj
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.getAsString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.js.Date

internal data class ActivityHandlerLine(
    override val rawLine: String,
    override val time: Date,
    override val level: Level,
    val channel: String,
    val message: String,
    val context: JsonObject,
) : LogLine {

    override val groups: List<Pair<LogMessageGroup, String>>

    init {
        val groups: MutableList<Pair<LogMessageGroup, String>> = mutableListOf(
            Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
            Pair(LogMessageGroup.FILE, extractFile()),
            Pair(LogMessageGroup.LEVEL, level.name),
            Pair(LogMessageGroup.CHANNEL, channel),
        )

        extractHttpStatus()?.let { groups.add(Pair(LogMessageGroup.HTTP_RESP_STATUS, it)) }
        extractHttpAddress()?.let {
            groups.add(Pair(LogMessageGroup.HTTP_ADDRESS, it))

            domainFromUrl(it)?.let {
                groups.add(Pair(LogMessageGroup.HTTP_DOMAIN, it))
            }
        }
        extractActivityObject()?.let { groups.add(Pair(LogMessageGroup.ACTIVITY_OBJECT, it) )}
        extractExceptionMessage()?.let { groups.add(Pair(LogMessageGroup.EXCEPTION_MESSAGE, it) )}

        this.groups = groups
    }

    private fun extractFile(): String {
        return context.getAsObj("exception")?.getAsString("file")?.let {
            var prefixPromoter = "/src/"
            var prefixIdx = it.indexOf(prefixPromoter)

            if(prefixIdx == -1) {
                prefixPromoter = "/vendor/"
                prefixIdx = it.indexOf(prefixPromoter)
            }

            return@let if(prefixIdx != -1) it.substring(prefixIdx + prefixPromoter.length) else it
        } ?: "<ActivityHandler>"
    }

    private fun extractHttpStatus(): String? {
        val msgPromoter = "Invalid status code while getting:"
        val msgPromoterIdx = message.indexOf(msgPromoter)
        if(msgPromoterIdx == -1)
            return null
        val statusPromoter = ", status code: "
        var startIdx = message.indexOf(statusPromoter)
        if(startIdx == -1)
            return null
        startIdx += statusPromoter.length
        var endIdx = message.indexOf('.', startIdx)
        if(endIdx == -1)
            endIdx = message.indexOf(' ', startIdx)
        if(endIdx == -1)
            endIdx = message.length
        return message.substring(startIdx, endIdx)
    }

    private fun extractHttpAddress(): String? {
        return context.get("address")?.let {
            it as? JsonPrimitive
        }?.let {
            if(it.isString) it.content else null
        }
    }

    private fun extractActivityObject(): String? {
        return context.getAsString("o")
            ?: context.getAsString("url")
            ?: context.getAsString("address")
            ?: context.getAsString("parent")
    }

    private fun extractExceptionMessage(): String? {
        return context.getAsObj("exception")?.getAsString("message")
    }
}
