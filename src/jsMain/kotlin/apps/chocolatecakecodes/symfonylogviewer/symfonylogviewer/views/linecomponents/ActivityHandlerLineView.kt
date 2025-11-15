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

        addBasicContent(line)
    }
}
