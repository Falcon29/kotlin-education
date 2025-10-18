package org.kotlined.common.repository

import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock

data class DBTicketIdRequest(
    val id: CCTicketId,
    val lock: CCTicketLock = CCTicketLock.NONE,
) {
    constructor(ticket: CCTicket): this(ticket.id, ticket.lock)
}

