package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.MessangerExceptionLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun messangerExceptionLineView(line: MessangerExceptionLine): Div {
    return Div {
        h3 { +"Messanger Exception" }

        div {
            this.addCssClasses("grid", "grid-cols-6", "gap-2")

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Time: "
                }
                +line.time.toUTCString()
            }

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Level: "
                }
                +line.level.toString()
            }

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Channel: "
                }
                +line.channel
            }

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Message type: "
                }
                +line.messageType
            }

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Message id: "
                }
                +line.messageId
            }

            div {
                div {
                    this.addCssClasses("font-semibold")
                    +"Retries: "
                }
                +line.retryCount.toString()
            }
        }

        div {
            this.addCssClasses("grid", "grid-cols-8", "gap-2")

            div {
                this.addCssClasses("font-semibold")
                +"Exception Class: "
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-gray-50", "p-1", "rounded", "border", "border-gray-200", "flex", "items-center", "gap-2")
                +line.exceptionType
                copyButton(line.exceptionType, compact = true)
            }

            div {
                this.addCssClasses("font-semibold")
                +"Failed file: "
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-gray-50", "p-1", "rounded", "border", "border-gray-200", "flex", "items-center", "gap-2", "break-all")
                +line.file
                copyButton(line.file, compact = true)
            }

            div {
                this.addCssClasses("font-semibold")
                +"Message: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max", "font-mono", "text-sm", "bg-gray-50", "p-2", "rounded", "border", "border-gray-200", "whitespace-pre-wrap")
                    +line.message
                }
            }

            div {
                this.addCssClasses("font-semibold")
                +"Exception message: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max", "font-mono", "text-sm", "bg-red-50", "p-2", "rounded", "border", "border-red-200", "whitespace-pre-wrap")
                    +line.errorMessage
                }
            }
        }

        addBasicContent(line)
    }
}
