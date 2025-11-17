package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import kotlin.js.Date

internal data class DownloadErrorLine(
    override val rawLine: String,
    override val time: Date,
    override val level: Level,
    val channel: String,
    val message: String,
    val url: String,
) : LogLine {

    override val groups: List<Pair<LogMessageGroup, String>> = listOf(
        Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
        Pair(LogMessageGroup.LEVEL, level.name),
        Pair(LogMessageGroup.CHANNEL, channel),
    )
}
