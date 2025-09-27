package org.kotlined.common

import kotlinx.datetime.Instant
import org.kotlined.common.models.*
import org.kotlined.common.ws.ICCWsSession

data class CCContext(
    var command: CCCommand = CCCommand.NONE,
    var state: CCTicketStatus = CCTicketStatus.NONE,
    val errors: MutableList<CCError> = mutableListOf(),
//    var stubCase: CCStubs = CCStubs.NONE,
    var wsSession: ICCWsSession = ICCWsSession.NONE,
    var requestId: CCRequestId = CCRequestId.NONE,
    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),
    var ticketRequest: CCTicket = CCTicket(),
    var ticketListRequest: CCTicketList = CCTicketList(),
    var ticketResponse: CCTicket = CCTicket(),
    var ticketListResponse: MutableList<CCTicket> = mutableListOf(),
)