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
        this.addCssClasses("border-1", "border-solid", "p-1")

        h3 { +"Http Exception" }

        div {
            this.addCssClasses("grid", "grid-cols-5", "gap-2")

            div {
                +"Time: "
                +line.time.toUTCString()
            }

            div {
                +"Level: "
                +line.level.toString()
            }

            div {
                +"Channel: "
                +line.channel
            }

            div {
                +"Method: "
                +line.httpType
            }

            div {
                +"Status: "
                +line.httpRespStatus.toString()
            }
        }

        div {
            this.addCssClasses("grid", "grid-cols-8", "gap-2")

            div {
                +"Exception Class: "
            }
            div {
                this.addCssClasses("col-span-7")
                +line.exceptionType
            }

            div {
                +"Address: "
            }
            div {
                this.addCssClasses("col-span-7")
                +line.httpAddress
            }

            div {
                +"Response body: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                +line.httpRespBody
            }

            div {
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

                    this.copyButton(text)
                    div {
                        this.addCssClasses("w-max", "whitespace-pre")
                        +text
                    }
                }
            }
        }

        addBasicContent(line)
    }
}
