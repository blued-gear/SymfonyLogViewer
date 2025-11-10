package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import io.kvision.html.Div

internal class LogView (
    private val data: String,
) : Div() {

    init {
        +data
    }

}
