package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.UnknownLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun unknownLineView(line: UnknownLine): Div {
    return Div {
        h3 { +"Unknown log event" }

        div {
            this.addCssClasses("grid", "grid-cols-4", "gap-2")

            div { +"Time:" }
            div { +line.time.toTimeString() }

            div { +"Level:" }
            div { +line.level.toString() }
        }

        addBasicContent(line)
    }
}
