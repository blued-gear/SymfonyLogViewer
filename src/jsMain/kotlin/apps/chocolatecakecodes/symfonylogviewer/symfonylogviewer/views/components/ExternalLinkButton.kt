package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.core.Container
import io.kvision.core.onClickLaunch
import io.kvision.html.Button
import io.kvision.html.button
import kotlin.js.js

internal fun Container.externalLinkButton(url: String, compact: Boolean = false): Button {
    return button(if (compact) "🔗" else "Open") {
        this.addCssClasses(if (compact) "copy-compact" else "boxedBtn")
        this.onClickLaunch {
            it.stopPropagation()
            js("window.open(url, '_blank')")
        }
    }
}
