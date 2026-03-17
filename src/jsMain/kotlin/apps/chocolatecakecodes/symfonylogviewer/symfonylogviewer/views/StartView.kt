package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import io.kvision.core.onChange
import io.kvision.html.Div
import io.kvision.html.InputType
import io.kvision.html.div
import io.kvision.html.input
import js.buffer.ArrayBuffer
import org.w3c.files.File
import org.w3c.files.FileReader

internal class StartView(
    private val onNext: (ArrayBuffer) -> Unit,
) : Div() {

    private lateinit var msg: Div

    init {
        this.addCssClasses("max-w-8xl", "mx-auto", "p-4")

        div {
            this.addCssClasses("m-6", "flex", "flex-col", "gap-2")

            div {
                + "Select a logfile"
            }
            input {
                this.type = InputType.FILE
                this.addCssClasses("block", "w-full", "text-base", "text-gray-900", "border", "border-gray-300", "rounded-lg", "cursor-pointer", "bg-gray-50", "py-2", "px-4", "dark:text-gray-400", "focus:outline-none", "dark:border-gray-600", "dark:focus:border-blue-500", "dark:bg-gray-700")
                this.onChange {
                    this@StartView.onFileSelected(it.target.asDynamic().files[0] as File)
                }
            }

            this@StartView.msg = div {
                this.addCssClasses("color-red-600")
            }
        }
    }

    private fun onFileSelected(file: File) {
        msg.removeAll()
        msg.apply { +"Loading file..." }

        val reader = FileReader()
        reader.onload = {
            onNext(reader.result)
        }
        reader.onerror = {
            console.error(reader.error)
            msg.apply { +"unable to load file" }
        }
        reader.readAsArrayBuffer(file)
    }
}
