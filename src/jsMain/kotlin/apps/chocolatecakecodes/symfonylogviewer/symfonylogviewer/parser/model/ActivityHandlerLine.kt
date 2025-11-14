package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
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

    override val refHash: Int = -1
    override val references: Int? = null
    override val groups: List<Pair<LogMessageGroup, String>>

    init {
        val groups: MutableList<Pair<LogMessageGroup, String>> = mutableListOf(
            Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
            Pair(LogMessageGroup.FILE, "ActivityHandler"),
            Pair(LogMessageGroup.LEVEL, level.name),
            Pair(LogMessageGroup.CHANNEL, channel),
        )

        extractHttpStatus()?.let { groups.add(Pair(LogMessageGroup.HTTP_RESP_STATUS, it)) }
        extractHttpAddress()?.let { groups.add(Pair(LogMessageGroup.HTTP_ADDRESS, it)) }

        this.groups = groups
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
}
