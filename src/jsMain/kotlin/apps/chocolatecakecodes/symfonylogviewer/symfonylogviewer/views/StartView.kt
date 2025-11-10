package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import io.kvision.core.onChange
import io.kvision.html.Div
import io.kvision.html.InputType
import io.kvision.html.div
import io.kvision.html.input
import org.w3c.files.File
import org.w3c.files.FileReader

internal class StartView(
    private val onNext: (String) -> Unit,
) : Div() {

    init {
        div {
            + "Select a logfile"
        }
        input {
            this.type = InputType.FILE
            this.onChange {
                onFileSelected(it.target.asDynamic().files[0] as File)
            }
        }
    }

    private fun onFileSelected(file: File) {
        val reader = FileReader()
        reader.onload = {
            onNext(reader.result)
        }
        reader.readAsText(file)
    }
}
