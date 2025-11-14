package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.core.Container
import io.kvision.core.onClickLaunch
import io.kvision.html.Button
import io.kvision.html.button
import web.clipboard.writeText
import web.navigator.navigator

internal fun Container.copyButton(text: String): Button {
    return button("Copy") {
        this.addCssClasses("boxedBtn")
        this.onClickLaunch {
            it.stopPropagation()
            navigator.clipboard.writeText(text)
        }
    }
}
