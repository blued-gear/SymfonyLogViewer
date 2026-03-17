package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.UnknownLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.levelBadge
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun unknownLineView(line: UnknownLine, searchQuery: String = "", searchScope: String = "raw"): Div {
    return Div {
        this.addCssClasses("border", "border-gray-300", "rounded-lg", "p-4", "mb-3", "shadow-md", "bg-gray-200", "hover:shadow-lg", "transition-shadow")
        
        h3 {
            this.addCssClasses("text-lg", "font-bold", "mb-3", "pb-2", "border-b", "border-gray-200", "text-gray-800")
            +"Unknown Log Event"
        }

        div {
            this.addCssClasses("grid", "gap-3", "mb-4")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr)")

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"⏰ Time"
                }
                div {
                    this.addCssClasses("truncate", "text-sm", "text-gray-900")
                    +line.time.toUTCString()
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"Level"
                }
                div {
                    levelBadge(line.level)
                }
            }
        }

        addBasicContent(line, searchQuery, searchScope)
    }
}
