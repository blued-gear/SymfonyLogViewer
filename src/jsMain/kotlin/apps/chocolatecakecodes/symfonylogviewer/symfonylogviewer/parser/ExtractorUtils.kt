package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import web.url.URL

internal object ExtractorUtils {

    // https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url/3809435#3809435
    val REGEX_URL = Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")

    fun extractUrls(str: String): List<String> {
        return REGEX_URL.findAll(str).map {
            it.value
        }.filter {
            // filter out truncated URLs
            it.count { it == '/' } > 2
        }.toList()
    }

    internal fun domainFromUrl(url: String): String? {
        try {
            return URL(url).host.ifEmpty { null }
        } catch (e: Throwable) {
            return null
        }
    }

    fun allStringsInJson(elm: JsonElement): List<String> {
        val list = mutableListOf<String>()
        allStringsInJson(elm, list)
        return list
    }

    private fun allStringsInJson(elm: JsonElement, list: MutableList<String>) {
        when(elm) {
            is JsonPrimitive -> {
                if (elm.isString) {
                    list.add(elm.content)
                }
            }
            is JsonArray -> {
                elm.forEach {
                    allStringsInJson(it, list)
                }
            }
            is JsonObject -> {
                elm.values.forEach {
                    allStringsInJson(it, list)
                }
            }
        }
    }
}
