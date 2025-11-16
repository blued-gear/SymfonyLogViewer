package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlin.js.Date

internal data class HttpExceptionLine(
    override val rawLine: String,
    override val time: Date,
    override val level: Level,
    val channel: String,
    val exceptionType: String,
    val httpType: String,
    val httpAddress: String,
    val httpReqBody: String,
    val httpRespStatus: Int,
    val httpRespBody: String,
    val ctxMessage: String,
) : LogLine {

    override val refHash: Int = ctxMessage.hashCode()

    override val groups: List<Pair<LogMessageGroup, String>>

    init {
        val groups = mutableListOf(
            Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
            Pair(LogMessageGroup.LEVEL, level.name),
            Pair(LogMessageGroup.CHANNEL, channel),
            Pair(LogMessageGroup.EXCEPTION_TYPE, exceptionType),
            Pair(LogMessageGroup.HTTP_ADDRESS, httpAddress),
            Pair(LogMessageGroup.HTTP_RESP_STATUS, httpRespStatus.toString()),
        )

        if(refHash != -1)
            groups.add(Pair(LogMessageGroup.RELATED, refHash.toString()))

        if(httpRespBody.startsWith("{\"error\":\"")) {
            try {
                val json = Json.parseToJsonElement(httpRespBody)
                (json as? JsonObject)?.get("error")?.let {
                    (it as? JsonPrimitive)?.contentOrNull
                }?.let {
                    groups.add(Pair(LogMessageGroup.LEMMY_ERROR, it))
                }
            } catch(_: Throwable) {}
        }

        this.groups = groups
    }

}
