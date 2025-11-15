package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import io.kvision.core.Container
import io.kvision.core.Widget
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.state.ObservableList
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlin.math.min

internal class PaginatedList <T> (
    private val list: ObservableList<T>,
    private val renderItem: (T) -> Widget,
) : Div() {

    companion object {
        const val PAGE_SIZE = 100
    }

    private var page = ObservableValue(0)

    init {
        pageBar(this)
        list(this)

        list.subscribe {
            this.page.value = -1
            this.page.value = 0
        }
    }

    private fun pageBar(parent: Container) {
        parent.div {
            this.addCssClasses("flex", "gap-3", "justify-center-safe", "mb-4")

            button("<<") {
                this.onClick {
                    this@PaginatedList.page.value = 0
                }
            }.bind(this@PaginatedList.page) {
                this.disabled = it == 0
            }

            button("<") {
                this.onClick {
                    this@PaginatedList.page.value -= 1
                }
            }.bind(this@PaginatedList.page) {
                this.disabled = it == 0
            }

            div().bind(this@PaginatedList.page) {
                +"${it + 1} / ${this@PaginatedList.pages()}"
            }

            button(">") {
                this.onClick {
                    this@PaginatedList.page.value += 1
                }
            }.bind(this@PaginatedList.page) {
                this.disabled = it == this@PaginatedList.pages() - 1
            }

            button(">>") {
                this.onClick {
                    this@PaginatedList.page.value = this@PaginatedList.pages() - 1
                }
            }.bind(this@PaginatedList.page) {
                this.disabled = it == this@PaginatedList.pages() - 1
            }
        }
    }

    private fun list(parent: Container) {
        parent.div {
            this.addCssClasses("flex", "flex-col", "gap-3")
        }.bind(this.page) { page ->
            if(page == -1) return@bind

            val start = page*PAGE_SIZE
            val end = min(start + PAGE_SIZE, this@PaginatedList.list.size)
            for(i in start ..< end) {
                add(this@PaginatedList.renderItem(this@PaginatedList.list[i]))
            }
        }
    }

    private fun pages(): Int {
        if(list.isEmpty()) return 0
        return ((list.size - 1) / PAGE_SIZE) + 1
    }
}

internal fun <T> Container.paginatedList(
    list: ObservableList<T>,
    renderItem: (T) -> Widget,
): PaginatedList<T> {
    return PaginatedList(list, renderItem).also {
        this.add(it)
    }
}
