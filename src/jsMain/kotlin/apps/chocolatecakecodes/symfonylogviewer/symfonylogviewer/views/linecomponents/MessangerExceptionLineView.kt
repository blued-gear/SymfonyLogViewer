package apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.linecomponents

import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.parser.model.MessangerExceptionLine
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.addCssClasses
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.components.copyButton
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.levelBadge
import apps.chocolatecakecodes.symfonylogviewer.symfonylogviewer.views.retriesBadge
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.h3
import io.kvision.html.icon

internal fun messangerExceptionLineView(line: MessangerExceptionLine, searchQuery: String = "", searchScope: String = "raw"): Div {
    return Div {
        this.addCssClasses("border", "border-gray-300", "rounded-lg", "p-4", "mb-3", "shadow-md", "bg-gray-200", "hover:shadow-lg", "transition-shadow")
        
        h3 {
            this.addCssClasses("text-lg", "font-bold", "mb-3", "pb-2", "border-b", "border-gray-200", "text-gray-800")
            +"Messenger Exception"
        }

        div {
            this.addCssClasses("grid", "gap-3", "mb-4")
            this.setStyle("grid-template-columns", "minmax(0, 3fr) minmax(0, 1fr) minmax(0, 1fr) minmax(0, 3fr) minmax(0, 2fr) minmax(0, 1fr)")

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    icon("fas fa-clock")
                    +" Time"
                }
                div {
                    this.addCssClasses("truncate", "text-sm", "text-gray-900")
                    +line.time.toUTCString()
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    +"Level"
                }
                div {
                    levelBadge(line.level)
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    icon("fas fa-broadcast-tower")
                    +" Channel"
                }
                div {
                    this.addCssClasses("truncate", "text-sm", "text-gray-900", "font-medium")
                    +line.channel
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    icon("fas fa-envelope")
                    +" Message Type"
                }
                div {
                    this.addCssClasses("inline-block", "px-2", "py-1", "rounded", "text-xs", "font-bold", "bg-gray-100", "text-gray-800")
                    +line.messageType
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    icon("fas fa-hashtag")
                    +" Message ID"
                }
                div {
                    this.addCssClasses("truncate", "text-sm", "text-gray-900", "font-mono")
                    +line.messageId
                }
            }

            div {
                div {
                    this.addCssClasses("font-semibold", "text-xs", "text-gray-500", "uppercase", "tracking-wide", "mb-1")
                    icon("fas fa-redo")
                    +" Retries"
                }
                div {
                    retriesBadge(line.retryCount)
                }
            }
        }

        div {
            this.addCssClasses("grid", "grid-cols-8", "gap-3")

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                icon("fas fa-bomb")
                +" Exception Class:"
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-red-50", "p-2", "rounded-md", "border", "border-red-300", "flex", "items-center", "gap-2", "shadow-sm")
                +line.exceptionType
                copyButton(line.exceptionType, compact = true)
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                icon("fas fa-file-alt")
                +" Failed File:"
            }
            div {
                this.addCssClasses("col-span-7", "font-mono", "text-sm", "bg-gray-50", "p-2", "rounded-md", "border", "border-gray-300", "flex", "items-center", "gap-2", "break-all", "shadow-sm")
                +line.file
                copyButton(line.file, compact = true)
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                icon("fas fa-edit")
                +" Message:"
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max", "font-mono", "text-sm", "bg-gray-50", "p-3", "rounded-md", "border", "border-gray-300", "whitespace-pre-wrap", "shadow-sm")
                    +line.message
                }
            }

            div {
                this.addCssClasses("font-semibold", "text-sm", "text-gray-700")
                icon("fas fa-exclamation-triangle")
                +" Exception Message:"
            }
            div {
                this.addCssClasses("col-span-7", "overflow-x-auto", "pb-3")
                div {
                    this.addCssClasses("w-max", "font-mono", "text-sm", "bg-red-50", "p-3", "rounded-md", "border", "border-red-300", "whitespace-pre-wrap", "shadow-sm")
                    +line.errorMessage
                }
            }
        }

        addBasicContent(line, searchQuery, searchScope)
    }
}
