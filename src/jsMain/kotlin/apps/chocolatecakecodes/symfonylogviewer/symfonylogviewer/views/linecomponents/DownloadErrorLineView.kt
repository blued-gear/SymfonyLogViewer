package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.DownloadErrorLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun downloadErrorLineView(line: DownloadErrorLine): Div {
    return Div {
        this.addCssClasses("border-1", "border-solid", "p-1")

        h3 { +"Download error" }

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
                +"Url: "
                +line.url
            }
        }

        div {
            +"Message: "
            +line.message
        }

        addBasicContent(line)
    }
}
