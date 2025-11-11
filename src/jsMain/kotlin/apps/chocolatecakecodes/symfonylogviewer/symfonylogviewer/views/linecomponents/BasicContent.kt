package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.Level
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.LogLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import io.kvision.core.onClick
import io.kvision.html.Div
import io.kvision.html.div
import kotlinx.serialization.json.Json

internal fun Div.addBasicContent(line: LogLine) {
    this.addCssClasses("p-2")

    val rawLine = div {
        this.addCssClasses("whitespace-pre", "mt-4", "overflow-x-auto", "pb-3")
        this.setStyle("display", "none")

        try {
            +prettyJson.encodeToString(Json.parseToJsonElement(line.rawLine))
        } catch(_: Throwable) {
            +line.rawLine
        }
    }

    var expanded = false
    this.onClick {
        expanded = !expanded
        rawLine.setStyle("display", if(expanded) "" else "none")
    }

    when(line.level) {
        Level.UNKNOWN, Level.INFO -> ""
        Level.WARN -> "bg-yellow-300"
        Level.ERROR -> "bg-red-300"
        Level.CRIT -> "bg-red-400"
    }.let {
        this.addCssClass(it)
    }
}
