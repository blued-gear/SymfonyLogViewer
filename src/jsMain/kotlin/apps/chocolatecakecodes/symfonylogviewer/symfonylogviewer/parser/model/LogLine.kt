package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import kotlin.js.Date

internal sealed interface LogLine {

    val rawLine: String
    val time: Date
    val level: Level
    val refHash: Int
    val groups: List<Pair<LogMessageGroup, String>>

    val references: Int?
        get() = null

}

internal enum class Level {
    INFO, WARN, ERROR, CRIT, UNKNOWN;

    companion object {

        fun parse(name: String): Level {
            return when(name) {
                "INFO" -> INFO
                "WARNING" -> WARN
                "ERROR" -> ERROR
                "CRITICAL" -> CRIT
                else -> UNKNOWN
            }
        }
    }

}
