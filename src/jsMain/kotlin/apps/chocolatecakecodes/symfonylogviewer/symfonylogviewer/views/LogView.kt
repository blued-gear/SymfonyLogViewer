package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogParser
import io.kvision.html.Div

internal class LogView (
    private val data: String,
) : Div() {

    init {
        +data

        console.log(LogParser(data).parse())
    }

}
