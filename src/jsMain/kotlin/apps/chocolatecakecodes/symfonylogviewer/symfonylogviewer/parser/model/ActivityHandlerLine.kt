package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import kotlinx.serialization.json.JsonObject
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
    override val groups: List<Pair<LogMessageGroup, String>> = listOf(
        Pair(LogMessageGroup.TYPE, this::class.simpleName!!),
        Pair(LogMessageGroup.FILE, "ActivityHandler"),
        Pair(LogMessageGroup.LEVEL, level.name),
        Pair(LogMessageGroup.CHANNEL, channel),
    )
}
