package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogParser
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.*
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.paginatedList
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents.*
import io.kvision.core.Container
import io.kvision.form.select.select
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.state.bindTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class LogView (
    private val data: String,
) : Div() {

    private lateinit var allLines: List<LogLine>
    private lateinit var groupIndexedLines: Map<Pair<LogMessageGroup, String>, List<LogLine>>
    private val groups = ObservableValue<Map<LogMessageGroup, Set<String>>>(emptyMap())
    private val currentGroup = ObservableValue<String>("")
    private val currentValue = ObservableValue<String>("")
    private val filteredLines = ObservableListWrapper<LogLine>(mutableListOf())

    init {
        this.addCssClasses("m-2")

        div {
            this@LogView.searchBar(this)
        }
        div {
            this@LogView.listView(this)
        }

        CoroutineScope(Dispatchers.Default).launch {
            loadLines()

            currentValue.subscribe {
                CoroutineScope(Dispatchers.Default).launch {
                    filter(currentGroup.value, it)
                }
            }
        }
    }

    private fun searchBar(container: Container) {
        container.div().apply {
            +"Filter attribute: "
            select {
                this.addCssClasses("filterSelect")

                this.subscribe {
                    this@LogView.currentGroup.value = it ?: ""
                }
            }.bind(this@LogView.groups) { groups ->
                this.options = listOf(Pair("", "ALL")) + groups.keys.map { Pair(it.toString(), it.toString()) }.toList()
            }
        }

        container.div().apply {
            +"Attribute value: "
            select {
                this.addCssClasses("filterSelect")
            }.bind(this@LogView.currentGroup) { group ->
                if(group == "") {
                    this.options = emptyList()
                    this@LogView.currentValue.value = ""
                } else {
                    this.options = this@LogView.attributeFilterValues(group)
                    this@LogView.currentValue.value = this.options!!.first().first
                }
            }.bindTo(this@LogView.currentValue)
        }
    }

    private fun listView(container: Container) {
        container.div {
            this.addCssClasses("p-4", "border-2", "border-solid")
            this.paginatedList(this@LogView.filteredLines) { line ->
                when(line) {
                    is HttpExceptionLine -> httpExceptionLineView(line)
                    is MessangerExceptionLine -> messangerExceptionLineView(line)
                    is ActivityHandlerLine -> activityHandlerLineView(line)
                    is ActivityPubManagerLine -> activityPubManagerLineView(line)
                    is DownloadErrorLine -> downloadErrorLineView(line)
                    is UnknownLine -> unknownLineView(line)
                }
            }
        }
    }

    private suspend fun loadLines() {
        val parser = LogParser(data)
        allLines = parser.processLines()

        val groupIndex = mutableMapOf<Pair<LogMessageGroup, String>, MutableList<LogLine>>()
        val groups = mutableMapOf<LogMessageGroup, MutableSet<String>>()
        allLines.forEach { line ->
            line.groups.forEach { (group, value) ->
                groupIndex.getOrPut(Pair(group, value), { mutableListOf() }).add(line)
                groups.getOrPut(group, { mutableSetOf() }).add(value)
            }
        }
        this.groupIndexedLines = groupIndex
        this.groups.value = groups

        filteredLines.addAll(allLines)
    }

    private suspend fun filter(group: String, value: String) {
        filteredLines.clear()

        if(group == "") {
            filteredLines.addAll(allLines)
        } else {
            val grp = LogMessageGroup.valueOf(group)
            filteredLines.addAll(groupIndexedLines[Pair(grp, value)]!!)
        }
    }

    private fun attributeFilterValues(group: String): List<Pair<String, String>> {
        val grp = LogMessageGroup.valueOf(group)
        return groups.value.get(grp)!!.map {
            val lineCount = this@LogView.groupIndexedLines[Pair(grp, it)]!!.size
            Pair(lineCount, it)
        }.sortedByDescending {
            it.first
        }.map { (lineCount, value) ->
            Pair(value, "$value ($lineCount)")
        }
    }
}
