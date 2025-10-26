package org.kotlined.common

import kotlinx.datetime.Instant
import org.kotlined.common.models.*
import org.kotlined.common.repository.IRepoTicket
import org.kotlined.common.ws.ICCWsSession

data class CCContext(
    var command: CCCommand = CCCommand.NONE,
    var state: CCState = CCState.NONE,
    val errors: MutableList<CCError> = mutableListOf(),

    var corSettings: CCCorSettings = CCCorSettings(),
    var stubCase: CCStubs = CCStubs.NONE,
    var workMode: CCWorkMode = CCWorkMode.PROD,
    var wsSession: ICCWsSession = ICCWsSession.NONE,

    var requestId: CCTicketRequestId = CCTicketRequestId.NONE,
    var timeStart: Instant = Instant.fromEpochMilliseconds(Long.MIN_VALUE),

    var ticketRequest: CCTicket = CCTicket(),
    var ticketListRequest: CCTicketList = CCTicketList(),

    var ticketValidating: CCTicket = CCTicket(),
    var ticketValidated: CCTicket = CCTicket(),
//    var ticketListValidating: CCTicketList = CCTicketList(),

    var ticketRepo: IRepoTicket = IRepoTicket.NONE,
    var ticketRepoGet: CCTicket = CCTicket(), // То, что прочитали из репозитория
    var ticketRepoPrepare: CCTicket = CCTicket(), // То, что готовим для сохранения в БД
    var ticketRepoDone: CCTicket = CCTicket(),  // Результат, полученный из БД
    var ticketsRepoDone: MutableList<CCTicket> = mutableListOf(),

    var ticketResponse: CCTicket = CCTicket(),
    var ticketListResponse: MutableList<CCTicket> = mutableListOf(),
)