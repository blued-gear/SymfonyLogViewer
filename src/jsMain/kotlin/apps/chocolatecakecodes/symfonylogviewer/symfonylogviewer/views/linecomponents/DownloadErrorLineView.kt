package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.DownloadErrorLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.levelBadge
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun downloadErrorLineView(line: DownloadErrorLine): Div {
    return Div {
        this.addCssClasses("border", "border-gray-300", "rounded-lg", "p-4", "mb-3", "shadow-md", "bg-white", "hover:shadow-lg", "transition-shadow")

        h3 {
            this.addCssClasses("text-lg", "font-bold", "mb-3", "pb-2", "border-b", "border-gray-200", "text-gray-800")
            +"Download Error"
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

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"🔗 URL"
                }
                div {
                    this.addCssClasses("font-mono", "text-sm", "bg-blue-50", "p-2", "rounded-md", "border", "border-blue-200", "break-all", "shadow-sm")
                    +line.url
                }
            }

            // Empty filler columns for alignment
            div { }
            div { }
        }

        div {
            this.addCssClasses("font-semibold", "text-sm", "text-gray-700", "mb-2")
            +"❗ Message:"
        }
        div {
            this.addCssClasses("font-mono", "text-sm", "bg-red-50", "p-3", "rounded-md", "border", "border-red-300", "whitespace-pre-wrap", "shadow-sm")
            +line.message
        }

        addBasicContent(line)
    }
}
