package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.ActivityPubManagerLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3

internal fun activityPubManagerLineView(line: ActivityPubManagerLine): Div {
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
            +"Message: "
            +line.message
        }

        div {
            this.addCssClasses("grid", "grid-cols-5", "gap-2")

            div {
                +"Activity id: "
                +line.activityId
            }

            div {
                +"Activity type: "
                +line.activityType
            }

            div {
                +"Activity object: "
                +line.activityObject
            }

            div {
                +"Activity actor: "
                +line.activityActor
            }

            div {
                +"Activity audience: "
                +line.activityAudience
            }
        }

        addBasicContent(line)
    }
}
