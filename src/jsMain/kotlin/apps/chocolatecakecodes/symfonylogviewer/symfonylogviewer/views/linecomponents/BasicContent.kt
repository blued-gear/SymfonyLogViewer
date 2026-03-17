package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.Level
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.LogLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.expander
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import io.kvision.core.onClick
import io.kvision.html.Div
import io.kvision.html.div
import kotlinx.serialization.json.Json

internal fun Div.addBasicContent(line: LogLine) {
    this.addCssClasses("p-2")

    expander("0px") {
        this.addCssClasses("mt-4", "pb-3")

        val text = try {
            prettyJson.encodeToString(Json.parseToJsonElement(line.rawLine))
        } catch(_: Throwable) {
            line.rawLine
        }

        copyButton(text, compact = true)
        div {
            this.addCssClasses("whitespace-pre", "overflow-x-auto", "font-mono", "text-sm", "bg-gray-900", "text-gray-100", "p-3", "rounded-lg", "border", "border-gray-600")
            +text
        }
    }.also { expander ->
        this.onClick {
            expander.getElement()?.click()
        }
    }

    /*when(line.level) {
        Level.UNKNOWN, Level.INFO -> ""
        Level.WARN -> "bg-amber-100"
        Level.ERROR -> "bg-rose-200"
        Level.CRIT -> "bg-red-300"
    }.let {
        this.addCssClass(it)
    }*/
}
