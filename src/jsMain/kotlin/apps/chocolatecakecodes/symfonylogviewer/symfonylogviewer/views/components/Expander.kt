package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.core.Container
import io.kvision.core.onClick
import io.kvision.html.Div
import io.kvision.html.div
import web.window.window

internal inline fun Container.expander(collapsedHeight: String = "8rem", crossinline content: Div.() -> Unit): Div {
    return div {
        this.content()

        this.addCssClasses("overflow-hidden", "border-solid", "border-1", "p-1")
        this.setStyle("max-height", collapsedHeight)
        var expanded = false
        this.onClick {
            it.stopPropagation()

            val selection = window.getSelection()
            if(selection != null && selection.type == "Range") {
                return@onClick
            }

            expanded = !expanded
            this.setStyle("max-height", if(expanded) "" else collapsedHeight)
        }
    }
}
