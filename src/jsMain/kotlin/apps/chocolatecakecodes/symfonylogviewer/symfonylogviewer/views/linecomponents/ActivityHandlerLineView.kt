package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.ActivityHandlerLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import io.kvision.core.onClick
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun activityHandlerLineView(line: ActivityHandlerLine): Div {
    return Div {
        h3 { +"Activity Handler error" }

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
        }

        div {
            this.addCssClasses("overflow-x-auto", "pb-4")

            div {
                this.addCssClasses("w-max", "h-4")

                +"Message: "
                +line.message
            }
        }

        div {
            this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
            div {
                this.addCssClasses("w-max", "whitespace-pre")

                this.setStyle("max-height", "8rem")
                var expanded = false
                this.onClick {
                    it.stopPropagation()
                    expanded = !expanded
                    this.setStyle("max-height", if(expanded) "" else "8rem")
                }

                try {
                    +prettyJson.encodeToString(line.context)
                } catch(_: Throwable) {
                    console.warn("could not pretty-print httpReqBody")
                    +line.context.toString()
                }
            }
        }

        addBasicContent(line)
    }
}
