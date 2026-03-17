package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.HttpExceptionLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.expander
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.externalLinkButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.levelBadge
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.statusBadge
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3
import kotlinx.serialization.json.Json

internal fun httpExceptionLineView(line: HttpExceptionLine, searchQuery: String = "", searchScope: String = "raw"): Div {
    return Div {
        this.addCssClasses("border", "border-gray-300", "rounded-lg", "p-4", "mb-3", "shadow-md", "bg-gray-200", "hover:shadow-lg", "transition-shadow")

        h3 {
            this.addCssClasses("text-lg", "font-bold", "mb-3", "pb-2", "border-b", "border-gray-200", "text-gray-800")
            +"HTTP Exception"
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
                    +"Method"
                }
                div {
                    this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold", "bg-gray-100", "text-gray-800")
                    +line.httpType
                }
            }

            // Empty filler column for alignment
            div { }

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
            this.addCssClasses("grid", "grid-cols-8", "gap-3")

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                +"💥 Exception Class:"
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-red-50", "p-2", "rounded-md", "border", "border-red-300", "flex", "items-center", "gap-2", "shadow-sm")
                +line.exceptionType
                copyButton(line.exceptionType, compact = true)
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                +"🔗 Address:"
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-blue-50", "p-2", "rounded-md", "border", "border-blue-200", "flex", "items-center", "gap-2", "break-all", "shadow-sm")
                +line.httpAddress
                externalLinkButton(line.httpAddress, compact = true)
                copyButton(line.httpAddress, compact = true)
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                +"📤 Response body:"
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("font-mono", "text-sm", "bg-gray-50", "p-3", "rounded-md", "border", "border-gray-300", "whitespace-pre-wrap", "shadow-sm")
                    +line.httpRespBody
                }
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                +"📥 Request body:"
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                expander("5rem") {
                    this.addCssClasses("w-max")

                    val text = if(line.httpReqBody.isBlank()) {
                        "<empty>"
                    } else {
                        try {
                            prettyJson.encodeToString(Json.parseToJsonElement(line.httpReqBody))
                        } catch(_: Throwable) {
                            console.warn("could not pretty-print httpReqBody")
                            line.httpReqBody
                        }
                    }

                    this.copyButton(text, compact = true)
                    div {
                        this.addCssClasses("w-max", "whitespace-pre", "font-mono", "text-sm", "bg-gray-50", "p-3", "rounded-md", "border", "border-gray-300", "shadow-sm")
                        +text
                    }
                }
            }
        }

        addBasicContent(line, searchQuery, searchScope)
    }
}
