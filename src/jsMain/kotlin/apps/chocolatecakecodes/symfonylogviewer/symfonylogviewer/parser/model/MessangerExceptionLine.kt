package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
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
    val prevErrHash: Int,
) : LogLine {

    override val refHash: Int = -1
    override val references: Int = prevErrHash

    override val groups: List<Pair<LogMessageGroup, String>> = listOf(
        Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
        Pair(LogMessageGroup.LEVEL, level.name),
        Pair(LogMessageGroup.CHANNEL, channel),
        Pair(LogMessageGroup.EXCEPTION_TYPE, exceptionType),
        Pair(LogMessageGroup.MESSAGE_TYPE, messageType),
        Pair(LogMessageGroup.FILE, formatFile()),
    )

    private fun formatFile(): String {
        var prefixPromoter = "/src/"
        var prefixIdx = file.indexOf(prefixPromoter)

        if(prefixIdx == -1) {
            prefixPromoter = "/vendor/"
            prefixIdx = file.indexOf(prefixPromoter)
        }

        return if(prefixIdx != -1) file.substring(prefixIdx + prefixPromoter.length) else file
    }
}
