package org.kotlined.common

import kotlinx.datetime.Instant
import org.kotlined.common.models.*

data class CCContext(
    var command: CCCommand = CCCommand.NONE,
    var state: CCTicketStatus = CCTicketStatus.NONE,
    val errors: MutableList<CCError> = mutableListOf(),
//    var stubCase: CCStubs = CCStubs.NONE,
    var requestId: CCRequestId = CCRequestId.NONE,
    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),
    var ticketRequest: CCTicket = CCTicket(),
    var ticketFilterRequest: CCTicketFilter = CCTicketFilter(),
    var ticketResponse: CCTicket = CCTicket(),
    var ticketsResponse: MutableList<CCTicket> = mutableListOf(),
)