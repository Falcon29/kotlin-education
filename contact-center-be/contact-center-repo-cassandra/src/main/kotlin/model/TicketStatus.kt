package org.kotlined.model

import org.kotlined.common.models.CCTicketStatus

enum class TicketStatus {
    NEW, IN_PROGRESS, CLOSED
}

fun TicketStatus?.fromTransport() = when(this) {
    null -> CCTicketStatus.NONE
    TicketStatus.NEW -> CCTicketStatus.NEW
    TicketStatus.IN_PROGRESS -> CCTicketStatus.IN_PROGRESS
    TicketStatus.CLOSED -> CCTicketStatus.CLOSED
}

fun CCTicketStatus.toTransport() = when(this) {
    CCTicketStatus.NONE -> null
    CCTicketStatus.NEW -> TicketStatus.NEW
    CCTicketStatus.IN_PROGRESS -> TicketStatus.IN_PROGRESS
    CCTicketStatus.CLOSED -> TicketStatus.CLOSED
}