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
            this.addCssClasses("grid", "gap-2")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr)")

            div {
                div {
                    this.addCssClasses("font-semibold", "text-sm")
                    +"Time:"
                }
                div {
                    this.addCssClasses("truncate")
                    +line.time.toUTCString()
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-sm")
                    +"Level:"
                }
                div {
                    this.addCssClasses("truncate")
                    +line.level.toString()
                }
            }
        }

        addBasicContent(line)
    }
}
