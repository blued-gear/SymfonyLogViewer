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
            this.addCssClasses("flex", "gap-3", "items-center", "mb-4")

            // Results count on the left
            div().bind(this@PaginatedList.list) {
                this.addCssClasses("text-sm", "text-gray-600", "flex-shrink-0")
                val total = this@PaginatedList.list.size
                +if(total == 0) {
                    "No results found"
                } else if(total == 1) {
                    "1 result"
                } else {
                    "$total results"
                }
            }

            // Spacer to push pagination to center
            div {
                this.addCssClasses("flex-grow")
            }

            // Pagination controls in the center
            div {
                this.addCssClasses("flex", "gap-3", "justify-center-safe")

                button("<<") {
                    this.addCssClasses("px-3", "py-1", "bg-sky-600", "text-white", "border", "border-sky-600", "rounded", "hover:bg-sky-700", "focus:outline-none", "focus:ring-2", "focus:ring-sky-500", "disabled:bg-gray-400", "disabled:border-gray-400", "disabled:cursor-not-allowed")
                    this.onClick {
                        this@PaginatedList.page.value = 0
                    }
                }.bind(this@PaginatedList.page) {
                    this.disabled = it == 0
                }

                button("<") {
                    this.addCssClasses("px-3", "py-1", "bg-sky-600", "text-white", "border", "border-sky-600", "rounded", "hover:bg-sky-700", "focus:outline-none", "focus:ring-2", "focus:ring-sky-500", "disabled:bg-gray-400", "disabled:border-gray-400", "disabled:cursor-not-allowed")
                    this.onClick {
                        this@PaginatedList.page.value -= 1
                    }
                }.bind(this@PaginatedList.page) {
                    this.disabled = it == 0
                }

                div().bind(this@PaginatedList.page) {
                    this.addCssClasses("px-3", "py-1", "text-gray-700", "font-medium")
                    val currentPage = if (this@PaginatedList.pages() == 0) 0 else it + 1
                    +"$currentPage / ${this@PaginatedList.pages()}"
                }

                button(">") {
                    this.addCssClasses("px-3", "py-1", "bg-sky-600", "text-white", "border", "border-sky-600", "rounded", "hover:bg-sky-700", "focus:outline-none", "focus:ring-2", "focus:ring-sky-500", "disabled:bg-gray-400", "disabled:border-gray-400", "disabled:cursor-not-allowed")
                    this.onClick {
                        this@PaginatedList.page.value += 1
                    }
                }.bind(this@PaginatedList.page) {
                    this.disabled = it >= this@PaginatedList.pages() - 1 || this@PaginatedList.pages() == 0
                }

                button(">>") {
                    this.addCssClasses("px-3", "py-1", "bg-sky-600", "text-white", "border", "border-sky-600", "rounded", "hover:bg-sky-700", "focus:outline-none", "focus:ring-2", "focus:ring-sky-500", "disabled:bg-gray-400", "disabled:border-gray-400", "disabled:cursor-not-allowed")
                    this.onClick {
                        this@PaginatedList.page.value = this@PaginatedList.pages() - 1
                    }
                }.bind(this@PaginatedList.page) {
                    this.disabled = it >= this@PaginatedList.pages() - 1 || this@PaginatedList.pages() == 0
                }
            }

            // Spacer on the right to balance
            div {
                this.addCssClasses("flex-grow")
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
