package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import io.kvision.core.Widget
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
internal val prettyJson = Json {
    this.prettyPrint = true
    this.prettyPrintIndent = "  "
}

internal fun Widget.addCssClasses(vararg classes: String) {
    classes.forEach {
        this.addCssClass(it)
    }
}
