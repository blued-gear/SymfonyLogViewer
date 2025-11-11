package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.MessangerExceptionLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun messangerExceptionLineView(line: MessangerExceptionLine): Div {
    return Div {
        h3 { +"Messanger Exception" }

        div {
            this.addCssClasses("grid", "grid-cols-6", "gap-2")

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
                +"Message type: "
                +line.messageType
            }

            div {
                +"Message id: "
                +line.messageId
            }

            div {
                +"Retries: "
                +line.retryCount.toString()
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
                +"Failed file: "
            }
            div {
                this.addCssClasses("col-span-7")
                +line.file
            }

            div {
                +"Message: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max")
                    +line.message
                }
            }

            div {
                +"Exception message: "
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max")
                    +line.errorMessage
                }
            }
        }

        addBasicContent(line)
    }
}
