package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.HttpExceptionLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.expander
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3
import kotlinx.serialization.json.Json

internal fun httpExceptionLineView(line: HttpExceptionLine): Div {
    return Div {
        this.addCssClasses("border", "border-gray-400", "rounded-lg", "p-2", "mb-2")

        h3 { +"Http Exception" }

        div {
            this.addCssClasses("grid", "gap-2")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr) minmax(0, 1fr) minmax(0, 2fr) minmax(0, 1fr)")

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

            div {
                div {
                    this.addCssClasses("font-semibold", "text-sm")
                    +"Method:"
                }
                div {
                    this.addCssClasses("truncate")
                    +line.httpType
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-sm")
                    +"Status:"
                }
                div {
                    this.addCssClasses("truncate")
                    +line.httpRespStatus.toString()
                }
            }
        }

        div {
            this.addCssClasses("grid", "grid-cols-8", "gap-2")

            div {
                this.addCssClasses("font-semibold")
                +"Exception Class: "
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-red-50", "p-1", "rounded", "border", "border-red-200", "flex", "items-center", "gap-2")
                +line.exceptionType
                copyButton(line.exceptionType, compact = true)
            }

            div {
                this.addCssClasses("font-semibold")
                +"Address: "
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-gray-50", "p-1", "rounded", "border", "border-gray-200", "flex", "items-center", "gap-2", "break-all")
                +line.httpAddress
                copyButton(line.httpAddress, compact = true)
            }

            div {
                this.addCssClasses("font-semibold")
                +"Response body: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("font-mono", "text-sm", "bg-gray-50", "p-2", "rounded", "border", "border-gray-200", "whitespace-pre-wrap")
                    +line.httpRespBody
                }
            }

            div {
                this.addCssClasses("font-semibold")
                +"Request body: "
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
                        this.addCssClasses("w-max", "whitespace-pre", "font-mono", "text-sm", "bg-gray-50", "p-2", "rounded", "border", "border-gray-200")
                        +text
                    }
                }
            }
        }

        addBasicContent(line)
    }
}
