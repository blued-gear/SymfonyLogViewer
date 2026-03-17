package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.Level
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.LogLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.expander
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.prettyJson
import io.kvision.core.onClick
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.span
import kotlinx.serialization.json.Json

internal fun highlightText(text: String, query: String): String {
    if (query.isBlank()) return text
    
    // Use simple case-insensitive replace instead of regex (this is faster)
    return try {
        val queryLower = query.lowercase()
        val textLower = text.lowercase()
        var result = StringBuilder()
        var lastIndex = 0
        var searchIndex = 0
        
        while (searchIndex < text.length) {
            val foundIndex = textLower.indexOf(queryLower, searchIndex)
            if (foundIndex == -1) {
                result.append(text.substring(lastIndex))
                break
            }
            
            // Add text before match
            result.append(text.substring(lastIndex, foundIndex))
            // Add highlighted match
            result.append("<mark class=\"bg-yellow-300 text-black px-1 rounded\">")
            result.append(text.substring(foundIndex, foundIndex + query.length))
            result.append("</mark>")
            
            lastIndex = foundIndex + query.length
            searchIndex = foundIndex + 1
        }
        
        result.toString()
    } catch (e: Exception) {
        // Fallback to simple replacement
        text.replace(query, "<mark class=\"bg-yellow-300 text-black px-1 rounded\">$query</mark>", ignoreCase = true)
    }
}

internal fun Div.addBasicContent(line: LogLine, searchQuery: String = "", searchScope: String = "raw") {
    this.addCssClasses("p-2")

    expander("0px") {
        this.addCssClasses("mt-4", "pb-3")

        val text = try {
            prettyJson.encodeToString(Json.parseToJsonElement(line.rawLine))
        } catch(_: Throwable) {
            line.rawLine
        }

        copyButton(text, compact = true)
        div {
            this.addCssClasses("whitespace-pre", "overflow-x-auto", "font-mono", "text-sm", "bg-gray-900", "text-gray-100", "p-3", "rounded-lg", "border", "border-gray-600")
            
            if (searchQuery.isNotBlank()) {
                // Optimized: use manual string search instead of regex for better performance
                val queryLower = searchQuery.lowercase()
                val textLower = text.lowercase()
                var lastIndex = 0
                var searchIndex = 0
                
                while (searchIndex < text.length) {
                    val foundIndex = textLower.indexOf(queryLower, searchIndex)
                    if (foundIndex == -1) {
                        +text.substring(lastIndex)
                        break
                    }
                    
                    // Add text before match
                    if (foundIndex > lastIndex) {
                        +text.substring(lastIndex, foundIndex)
                    }
                    
                    // Add highlighted match
                    span {
                        this.addCssClasses("bg-yellow-300", "text-black", "px-1", "rounded")
                        +text.substring(foundIndex, foundIndex + searchQuery.length)
                    }
                    
                    lastIndex = foundIndex + searchQuery.length
                    searchIndex = foundIndex + 1
                }
            } else {
                +text
            }
        }
    }.also { expander ->
        this.onClick {
            expander.getElement()?.click()
        }
    }

    /*when(line.level) {
        Level.UNKNOWN, Level.INFO -> ""
        Level.WARN -> "bg-amber-100"
        Level.ERROR -> "bg-rose-200"
        Level.CRIT -> "bg-red-300"
    }.let {
        this.addCssClass(it)
    }*/
}
