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
    
    return try {
        val regex = Regex(query, RegexOption.IGNORE_CASE)
        regex.replace(text) { match ->
            "<mark class=\"bg-yellow-300 text-black px-1 rounded\">${match.value}</mark>"
        }
    } catch (e: Exception) {
        // If regex fails, fall back to simple text replacement
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
                // Split text by matches and create spans with highlighting
                val regex = try {
                    Regex(searchQuery, RegexOption.IGNORE_CASE)
                } catch (e: Exception) {
                    null
                }
                
                if (regex != null) {
                    val matches = regex.findAll(text)
                    var lastIndex = 0
                    
                    matches.forEach { match ->
                        // Add text before match
                        if (match.range.first > lastIndex) {
                            +text.substring(lastIndex, match.range.first)
                        }
                        
                        // Add highlighted match
                        span {
                            this.addCssClasses("bg-yellow-300", "text-black", "px-1", "rounded")
                            +match.value
                        }
                        
                        lastIndex = match.range.last + 1
                    }
                    
                    // Add remaining text
                    if (lastIndex < text.length) {
                        +text.substring(lastIndex)
                    }
                } else {
                    // Fallback: simple text without highlighting
                    +text
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
