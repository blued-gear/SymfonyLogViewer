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

    private val msg: Div

    init {
        this.addCssClasses("m-6", "flex", "flex-col", "gap-2")

        div {
            + "Select a logfile"
        }
        input {
            this.type = InputType.FILE
            this.onChange {
                onFileSelected(it.target.asDynamic().files[0] as File)
            }
        }

        msg = div {
            this.addCssClasses("color-red-600")
        }
    }

    private fun onFileSelected(file: File) {
        msg.removeAll()
        msg.apply { +"loading file..." }

        val reader = FileReader()
        reader.onload = {
            onNext(reader.result)
        }
        reader.onerror = {
            console.error(reader.error)
            msg.apply { +"unable to load file" }
        }
        reader.readAsText(file)
    }
}
