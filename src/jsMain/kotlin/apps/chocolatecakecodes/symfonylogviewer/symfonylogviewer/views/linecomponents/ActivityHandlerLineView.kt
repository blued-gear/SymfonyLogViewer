package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.ActivityHandlerLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.levelBadge
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.statusBadge
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun activityHandlerLineView(line: ActivityHandlerLine): Div {
    return Div {
        this.addCssClasses("border", "border-gray-300", "rounded-lg", "p-4", "mb-3", "shadow-md", "bg-white", "hover:shadow-lg", "transition-shadow")
        
        h3 {
            this.addCssClasses("text-lg", "font-bold", "mb-3", "pb-2", "border-b", "border-gray-200", "text-gray-800")
            +"Activity Handler Error"
        }

        div {
            this.addCssClasses("grid", "gap-3", "mb-4")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr) minmax(0, 1fr) minmax(0, 3fr) minmax(0, 2fr) minmax(0, 1fr)")

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

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"📡 Channel"
                }
                div {
                    this.addCssClasses("truncate", "text-sm", "text-gray-900", "font-medium")
                    +line.channel
                }
            }

            // Empty filler columns for alignment
            div { }
            
            div { }

            // Status column
            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"Status"
                }
                div {
                    statusBadge(line.httpRespStatus)
                }
            }
        }

        div {
            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700", "mb-2")
                +"📝 Message:"
            }
            div {
                this.addCssClasses("font-mono", "text-sm", "bg-gray-50", "p-3", "rounded-md", "border", "border-gray-300", "whitespace-pre-wrap", "shadow-sm")
                +line.message
            }
        }

        addBasicContent(line)
    }
}
