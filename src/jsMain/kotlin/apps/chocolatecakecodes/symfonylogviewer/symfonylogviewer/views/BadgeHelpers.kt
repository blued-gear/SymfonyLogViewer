package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.Level
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.icon

internal fun Div.levelBadge(level: Level) {
    val (badgeClasses, badgeIcon, badgeText) = when(level.toString()) {
        "ERROR" -> Triple(listOf("bg-red-100", "text-red-800", "border-red-300"), "fas fa-times-circle", "ERROR")
        "WARN" -> Triple(listOf("bg-yellow-100", "text-yellow-800", "border-yellow-300"), "fas fa-exclamation-triangle", "WARN")
        "CRIT" -> Triple(listOf("bg-red-200", "text-red-900", "border-red-400"), "fas fa-exclamation-circle", "CRIT")
        else -> Triple(listOf("bg-blue-100", "text-blue-800", "border-blue-300"), "fas fa-info-circle", level.toString())
    }
    this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold", "border")
    this.addCssClasses(*badgeClasses.toTypedArray())
    icon(badgeIcon)
    +" $badgeText"
}

internal fun Div.statusBadge(status: Int) {
    if (status == 0) {
        this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold", "bg-gray-100", "text-gray-600")
        +"N/A"
        return
    }
    
    val statusClasses = when {
        status < 300 -> listOf("bg-green-100", "text-green-800")
        status < 400 -> listOf("bg-blue-100", "text-blue-800")
        status < 500 -> listOf("bg-yellow-100", "text-yellow-800")
        else -> listOf("bg-red-100", "text-red-800")
    }
    this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold")
    this.addCssClasses(*statusClasses.toTypedArray())
    +status.toString()
}

internal fun Div.retriesBadge(retryCount: Int) {
    this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold", "bg-orange-100", "text-orange-800")
    +retryCount.toString()
}
