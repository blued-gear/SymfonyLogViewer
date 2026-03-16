package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.ActivityHandlerLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun activityHandlerLineView(line: ActivityHandlerLine): Div {
    return Div {
        h3 { +"Activity Handler error" }

        div {
            this.addCssClasses("grid", "gap-2")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr) minmax(0, 1fr)")

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

            div {
                div {
                    this.addCssClasses("font-semibold", "text-sm")
                    +"Channel:"
                }
                div {
                    this.addCssClasses("truncate")
                    +line.channel
                }
            }
        }

        div {
            div {
                this.addCssClasses("font-semibold")
                +"Message:"
            }
            div {
                this.addCssClasses("font-mono", "text-sm", "bg-gray-50", "p-2", "rounded", "border", "border-gray-200", "whitespace-pre-wrap")
                +line.message
            }
        }

        addBasicContent(line)
    }
}
