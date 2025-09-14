package org.kotlined.cc.biz


import org.kotlined.common.CCContext
import org.kotlined.common.CCCorSettings
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketList
import org.kotlined.common.models.CCTicketStatus

@Suppress("unused", "RedundantSuspendModifier")
class CCProcessor(val corSettings: CCCorSettings) {

    suspend fun exec(ctx: CCContext) {
        ctx.ticketRequest = CCTicket()
        ctx.ticketResponse = CCTicket()
        ctx.ticketListRequest = CCTicketList()
        ctx.ticketListResponse = mutableListOf()
        ctx.state = CCTicketStatus.NEW
    }
}