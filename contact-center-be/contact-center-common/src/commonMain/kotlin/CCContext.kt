package org.kotlined.common

import kotlinx.datetime.Instant
import org.kotlined.common.models.*
import org.kotlined.common.ws.ICCWsSession

data class CCContext(
    var command: CCCommand = CCCommand.NONE,
    var state: CCState = CCState.NONE,
    val errors: MutableList<CCError> = mutableListOf(),

    var corSettings: CCCorSettings = CCCorSettings(),
    var workMode: CCWorkMode = CCWorkMode.PROD,
    var wsSession: ICCWsSession = ICCWsSession.NONE,

    var requestId: CCTicketRequestId = CCTicketRequestId.NONE,
    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),

    var ticketRequest: CCTicket = CCTicket(),
    var ticketListRequest: CCTicketList = CCTicketList(),

    var ticketValidating: CCTicket = CCTicket(),
    var ticketValidated: CCTicket = CCTicket(),
//    var ticketListValidating: CCTicketList = CCTicketList(),

    var ticketResponse: CCTicket = CCTicket(),
    var ticketListResponse: MutableList<CCTicket> = mutableListOf(),
)