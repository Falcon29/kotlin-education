package org.kotlined

import org.kotlined.common.CCContext
import org.kotlined.common.CCCorSettings
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketStatus

@Suppress("unused", "RedundantSuspendModifier")
class CCProcessor(val corSettings: CCCorSettings) {

    suspend fun exec(ctx: CCContext) {
        ctx.ticketRequest = CCTicket()
        ctx.ticketResponse = CCTicket()
        ctx.state = CCTicketStatus.NEW
    }
}