package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.getAsObj
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.getAsString
import kotlinx.serialization.json.JsonObject
import kotlin.js.Date

internal data class MessangerExceptionLine(
    override val rawLine: String,
    override val time: Date,
    override val level: Level,
    val channel: String,
    val message: String,
    val errorMessage: String,
    val exceptionType: String,
    val messageType: String,
    val messageId: String,
    val retryCount: Int,
    val file: String,
    val context: JsonObject?,
) : LogLine {

    override val groups: List<Pair<LogMessageGroup, String>>

    init {
        val groups: MutableList<Pair<LogMessageGroup, String>> = mutableListOf(
            Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
            Pair(LogMessageGroup.LEVEL, level.name),
            Pair(LogMessageGroup.CHANNEL, channel),
            Pair(LogMessageGroup.EXCEPTION_TYPE, exceptionType),
            Pair(LogMessageGroup.MESSAGE_TYPE, messageType),
            Pair(LogMessageGroup.FILE, formatFile()),
        )

        extractPrevErrorMessage()?.let { groups.add(Pair(LogMessageGroup.RELATED, it) )}
        extractExceptionMessage()?.let { groups.add(Pair(LogMessageGroup.EXCEPTION_MESSAGE, it) )}

        this.groups = groups
    }

    private fun formatFile(): String {
        var prefixPromoter = "/src/"
        var prefixIdx = file.indexOf(prefixPromoter)

        if(prefixIdx == -1) {
            prefixPromoter = "/vendor/"
            prefixIdx = file.indexOf(prefixPromoter)
        }

        return if(prefixIdx != -1) file.substring(prefixIdx + prefixPromoter.length) else file
    }

    private fun extractPrevErrorMessage(): String? {
        return context?.getAsObj("exception")?.getAsObj("previous")?.let {
            it.getAsObj("previous") ?: it
        }?.getAsString("message")
    }

    private fun extractExceptionMessage(): String? {
        return context?.getAsObj("exception")?.getAsString("message")
    }
}
