package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import io.kvision.core.Widget
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*

@OptIn(ExperimentalSerializationApi::class)
internal val prettyJson = Json {
    this.prettyPrint = true
    this.prettyPrintIndent = "  "
}

internal fun Widget.addCssClasses(vararg classes: String) {
    classes.forEach {
        this.addCssClass(it)
    }
}

internal fun JsonObject.getOrNull(key: String): JsonElement? {
    return this.getOrElse(key, { null })
}

internal fun JsonObject.getAsString(key: String): String? {
    return this.getOrNull(key)?.let {
        if(it is JsonPrimitive && it.isString)
            it.content
        else
            null
    }
}

internal fun JsonObject.getAsInt(key: String): Int? {
    return this.getOrNull(key)?.let {
        if(it is JsonPrimitive)
            it.intOrNull
        else
            null
    }
}

internal fun JsonObject.getAsObj(key: String): JsonObject? {
    return this.getOrNull(key)?.let {
        it as? JsonObject
    }
}
