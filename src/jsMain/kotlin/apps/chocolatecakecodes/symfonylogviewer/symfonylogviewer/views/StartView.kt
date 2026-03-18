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
                this.addCssClasses("ai-rainbow-text", "text-4xl", "text-center", "mb-8", "ai-float")
                + "🌈 AI Rainbow Log Viewer 🤖✨"
            }
            div {
                this.addCssClasses("neon-text", "text-xl", "text-center", "mb-6")
                + "Select a logfile to begin your magical journey"
            }
            input {
                this.type = InputType.FILE
                this.addCssClasses("ai-rainbow-btn", "block", "w-full", "text-base", "rounded-lg", "cursor-pointer", "py-4", "px-6", "text-lg", "ai-glow")
                this.onChange {
                    this@StartView.onFileSelected(it.target.asDynamic().files[0] as File)
                }
            }

            this@StartView.msg = div {
                this.addCssClasses("text-white", "text-center", "mt-4", "font-bold", "ai-rainbow-text")
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
