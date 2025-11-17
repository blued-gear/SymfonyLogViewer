package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import kotlin.js.Date

internal data class UnknownLine(
    override val rawLine: String,
    override val time: Date,
    override val level: Level,
) : LogLine {

    override val groups: List<Pair<LogMessageGroup, String>> = emptyList()
}
