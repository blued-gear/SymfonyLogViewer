package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.core.Container
import io.kvision.core.onClickLaunch
import io.kvision.html.Button
import io.kvision.html.button
import web.clipboard.writeText
import web.navigator.navigator

internal fun Container.copyButton(text: String, compact: Boolean = false): Button {
    return button(if (compact) "📋" else "Copy") {
        this.addCssClasses(if (compact) "copy-compact" else "boxedBtn")
        this.onClickLaunch {
            it.stopPropagation()
            navigator.clipboard.writeText(text)
        }
    }
}
