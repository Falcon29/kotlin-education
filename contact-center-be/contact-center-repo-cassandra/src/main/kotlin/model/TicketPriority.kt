package org.kotlined.model

import org.kotlined.common.models.CCTicketPriority

enum class TicketPriority {
    LOW, MEDIUM, HIGH
}

fun TicketPriority?.fromTransport() = when(this) {
    null -> CCTicketPriority.NONE
    TicketPriority.LOW -> CCTicketPriority.LOW
    TicketPriority.MEDIUM -> CCTicketPriority.MEDIUM
    TicketPriority.HIGH -> CCTicketPriority.HIGH
}

fun CCTicketPriority.toTransport() = when(this) {
    CCTicketPriority.NONE -> null
    CCTicketPriority.LOW -> TicketPriority.LOW
    CCTicketPriority.MEDIUM -> TicketPriority.MEDIUM
    CCTicketPriority.HIGH -> TicketPriority.HIGH
}