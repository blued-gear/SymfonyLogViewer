package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.LogView
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.StartView
import io.kvision.*
import io.kvision.core.Widget
import io.kvision.html.div
import io.kvision.panel.root
import io.kvision.state.ObservableValue
import io.kvision.state.bind

class App : Application() {

    private val currentView = ObservableValue<Widget?>(null)

    override fun start() {
        root("kvapp") {
            div().bind(currentView) { view ->
                this.add(view ?: div{ +"initializing..." })
            }
        }

        setupViews()
    }

    private fun setupViews() {
        currentView.value = StartView(onNext = { data ->
            currentView.value = LogView(data)
        })
    }
}

fun main() {
    startApplication(
        ::App,
        js("import.meta.webpackHot").unsafeCast<Hot?>(),
        DatetimeModule,
        TailwindcssModule,
        CoreModule
    )
}
