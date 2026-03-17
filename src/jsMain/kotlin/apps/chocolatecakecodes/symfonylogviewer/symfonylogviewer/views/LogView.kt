package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogMessageGroup
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.LogParser
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.*
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.paginatedList
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents.*
import io.kvision.core.Container
import io.kvision.form.select.select
import io.kvision.form.text.text
import io.kvision.html.button
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.state.bindTo
import js.buffer.ArrayBuffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class LogView (
    private val data: ArrayBuffer,
) : Div() {

    private lateinit var allLines: List<LogLine>
    private lateinit var groupIndexedLines: Map<Pair<LogMessageGroup, String>, List<LogLine>>
    private val groups = ObservableValue<Map<LogMessageGroup, Set<String>>>(emptyMap())
    private val currentGroup = ObservableValue<String>("")
    private val currentValue = ObservableValue<String>("")
    private val searchQuery = ObservableValue<String>("")
    private val searchScope = ObservableValue<String>("all")
    private val filteredLines = ObservableListWrapper<LogLine>(mutableListOf())
    private var searchDebounceJob: Job? = null

    init {
        this.addCssClasses("max-w-8xl", "mx-auto", "p-4")

        div {
            this.addCssClasses("bg-white", "rounded-lg", "shadow-md", "p-4", "mb-4")
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
            
            searchQuery.subscribe {
                // Cancel previous debounce job
                searchDebounceJob?.cancel()
                
                // Start new debounce job
                searchDebounceJob = CoroutineScope(Dispatchers.Default).launch {
                    delay(300) // 300ms debounce delay
                    filter(currentGroup.value, currentValue.value)
                }
            }
            
            searchScope.subscribe {
                CoroutineScope(Dispatchers.Default).launch {
                    filter(currentGroup.value, currentValue.value)
                }
            }
        }
    }

    private fun searchBar(container: Container) {
        container.div().apply {
            this.addCssClasses("mb-3")
            div {
                this.addCssClasses("flex", "justify-between", "items-center", "mb-2")
                div {
                    this.addCssClasses("text-sm", "font-semibold", "text-gray-700")
                    +"🔍 Filter Options"
                }
                button("Reset All") {
                    this.addCssClasses("text-xs", "px-3", "py-1", "bg-gray-500", "text-white", "border", "border-gray-500", "rounded", "hover:bg-gray-600", "focus:outline-none", "focus:ring-2", "focus:ring-gray-400")
                    this.onClick {
                        this@LogView.resetAllFilters()
                    }
                }
            }
        }

        container.div().apply {
            this.addCssClasses("flex", "flex-col", "md:flex-row", "gap-4")
            
            // Left Column - Filter Section (60%)
            div {
                this.addCssClasses("w-full", "md:w-[60%]", "space-y-4")
                
                div {
                    div {
                        this.addCssClasses("text-xs", "font-semibold", "text-gray-600", "uppercase", "tracking-wide", "mb-2")
                        +"Filter Attribute:"
                    }
                    select {
                        this.addCssClasses("filterSelect")

                        this.subscribe {
                            this@LogView.currentGroup.value = it ?: ""
                        }
                    }.bind(this@LogView.groups) { groups ->
                        this.options = listOf(Pair("", "ALL")) + groups.keys.map { Pair(it.toString(), it.toString()) }.toList()
                    }.bindTo(this@LogView.currentGroup)
                }

                div {
                    div {
                        this.addCssClasses("text-xs", "font-semibold", "text-gray-600", "uppercase", "tracking-wide", "mb-2")
                        +"Attribute Value:"
                    }
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
            
            // Right Column - Search Section (40%)
            div {
                this.addCssClasses("w-full", "md:w-[40%]", "space-y-4")
                
                div {
                    div {
                        this.addCssClasses("text-xs", "font-semibold", "text-gray-600", "uppercase", "tracking-wide", "mb-2")
                        +"Text Search:"
                    }
                    text {
                        this.addCssClasses("filterSelect", "mb-2")
                        this.placeholder = "Search in logs..."
                        this.subscribe { query ->
                            this@LogView.searchQuery.value = query ?: ""
                        }
                    }.bindTo(this@LogView.searchQuery)
                }
                
                div {
                    div {
                        this.addCssClasses("text-xs", "font-semibold", "text-gray-600", "uppercase", "tracking-wide", "mb-2")
                        +"Search Scope:"
                    }
                    select {
                        this.addCssClasses("filterSelect")
                        this.subscribe { scope ->
                            this@LogView.searchScope.value = scope ?: "all"
                        }
                    }.bindTo(this@LogView.searchScope).options = listOf(
                        Pair("raw", "Raw Line"),
                        Pair("message", "Message Only"),
                        Pair("all", "All Fields")
                    )
                }
            }
        }
    }

    private fun listView(container: Container) {
        container.div {
            this.addCssClasses("bg-white", "p-4", "rounded-lg")
            this.paginatedList(this@LogView.filteredLines) { line ->
                when(line) {
                    is HttpExceptionLine -> httpExceptionLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
                    is MessangerExceptionLine -> messangerExceptionLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
                    is ActivityHandlerLine -> activityHandlerLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
                    is ActivityPubManagerLine -> activityPubManagerLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
                    is DownloadErrorLine -> downloadErrorLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
                    is UnknownLine -> unknownLineView(line, this@LogView.searchQuery.value, this@LogView.searchScope.value)
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

        val baseLines = if(group == "") {
            allLines
        } else {
            val grp = LogMessageGroup.valueOf(group)
            groupIndexedLines[Pair(grp, value)] ?: emptyList()
        }
        
        val searchResults = if(searchQuery.value.isBlank()) {
            baseLines
        } else {
            val query = searchQuery.value
            val scope = searchScope.value
            
            baseLines.filter { line ->
                when(scope) {
                    "raw" -> line.rawLine.contains(query, ignoreCase = true)
                    "message" -> {
                        // Extract message content from groups or raw line
                        val messageContent = line.groups
                            .find { it.first == LogMessageGroup.EXCEPTION_MESSAGE || it.first == LogMessageGroup.MESSAGE_TYPE }
                            ?.second 
                            ?: extractMessageFromRaw(line.rawLine)
                        messageContent.contains(query, ignoreCase = true)
                    }
                    "all" -> {
                        line.rawLine.contains(query, ignoreCase = true) ||
                        line.groups.any { (_, groupValue) ->
                            groupValue.contains(query, ignoreCase = true)
                        }
                    }
                    else -> line.rawLine.contains(query, ignoreCase = true)
                }
            }
        }
        
        filteredLines.addAll(searchResults)
    }
    
    private fun extractMessageFromRaw(rawLine: String): String {
        // Simple extraction - look for message part after common log patterns
        val messageStart = rawLine.indexOf("]: ")
        return if (messageStart != -1) {
            rawLine.substring(messageStart + 3)
        } else {
            rawLine
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
            val valueTruncated = if(value.length <= 128) value else value.substring(0, 128)
            Pair(value, "$valueTruncated ($lineCount)")
        }
    }

    private fun resetAllFilters() {
        CoroutineScope(Dispatchers.Default).launch {
            // Reset search
            searchQuery.value = ""
            searchScope.value = "all"
            
            // Reset attribute filters
            currentGroup.value = ""
            currentValue.value = ""
            
            // Apply reset (will show all logs)
            filter("", "")
        }
    }
}
