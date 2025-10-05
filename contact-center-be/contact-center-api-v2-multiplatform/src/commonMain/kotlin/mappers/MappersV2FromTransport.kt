package org.kotlined.cc.api.v2.mappers

import org.kotlined.cc.api.v2.models.*
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCStubs
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketList
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus
import org.kotlined.common.models.CCWorkMode

fun CCContext.fromTransport(request: IRequest) = when (request) {
    is TicketCreateRequest -> fromTransport(request)
    is TicketGetRequest -> fromTransport(request)
    is TicketUpdateRequest -> fromTransport(request)
    is TicketListRequest -> fromTransport(request)
    is TicketAssignRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun CCContext.fromTransport(request: TicketCreateRequest) {
    command = CCCommand.CREATE
    ticketRequest = request.ticket?.toInternal() ?: CCTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CCContext.fromTransport(request: TicketGetRequest) {
    command = CCCommand.GET
    ticketRequest = request.ticket.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CCContext.fromTransport(request: TicketListRequest) {
    command = CCCommand.LIST
    ticketListRequest = request.filter?.toInternal() ?: CCTicketList()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CCContext.fromTransport(request: TicketAssignRequest) {
    command = CCCommand.ASSIGN
    ticketRequest = request.assignment?.toInternal() ?: CCTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CCContext.fromTransport(request: TicketUpdateRequest) {
    command = CCCommand.UPDATE
    ticketRequest = request.ticket?.toInternal() ?: CCTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun String?.toTicketId() = this?.let { CCTicketId(it) } ?: CCTicketId.NONE
private fun String?.toTicketClientId() = this?.let { CCTicketClientId(it) } ?: CCTicketClientId.NONE
private fun String?.toTicketOperatorId() = this?.let { CCTicketOperatorId(it) } ?: CCTicketOperatorId.NONE
private fun String?.toTicketLock() = this?.let {CCTicketLock(it)} ?: CCTicketLock.NONE

private fun TicketGetObject?.toInternal(): CCTicket = if (this != null) {
    CCTicket(id = id.toTicketId())
} else {
    CCTicket()
}

private fun TicketDebug?.transportToWorkMode(): CCWorkMode = when (this?.mode) {
    TicketRequestDebugMode.PROD -> CCWorkMode.PROD
    TicketRequestDebugMode.TEST -> CCWorkMode.TEST
    TicketRequestDebugMode.STUB -> CCWorkMode.STUB
    null -> CCWorkMode.PROD
}

private fun TicketDebug?.transportToStubCase(): CCStubs = when (this?.stub) {
    null -> CCStubs.NONE
    TicketRequestDebugStubs.SUCCESS -> CCStubs.SUCCESS
    TicketRequestDebugStubs.BAD_ID -> CCStubs.BAD_ID
    TicketRequestDebugStubs.BAD_TITLE -> CCStubs.BAD_TITLE
    TicketRequestDebugStubs.BAD_DESCRIPTION -> CCStubs.BAD_DESCRIPTION
    TicketRequestDebugStubs.DB_ERROR -> CCStubs.DB_ERROR
}

private fun TicketList?.toInternal(): CCTicketList = CCTicketList(
    status = this?.status?.toStatus() ?: CCTicketStatus.NONE,
    priority = this?.priority?.toPriority() ?: CCTicketPriority.NONE,
    clientId = this?.clientId.toTicketClientId(),
    operatorId = this?.operatorId.toTicketOperatorId()
)

private fun TicketCreateObject.toInternal(): CCTicket = CCTicket(
    title = this.title ?: "",
    description = this.description ?: "",
    status = CCTicketStatus.NEW,
    priority = this.priority.toPriority(),
)

private fun TicketUpdateObject.toInternal(): CCTicket = CCTicket(
    id = this.id.toTicketId(),
    lock = lock.toTicketLock(),
    title = this.title ?: "",
    description = this.description ?: "",
    status = this.status.toStatus(),
    priority = this.priority.toPriority()
)

private fun TicketStatus?.toStatus(): CCTicketStatus = when (this) {
    TicketStatus.NEW -> CCTicketStatus.NEW
    TicketStatus.IN_PROGRESS -> CCTicketStatus.IN_PROGRESS
    TicketStatus.CLOSED -> CCTicketStatus.CLOSED
    null -> CCTicketStatus.NONE
}

private fun TicketPriority?.toPriority(): CCTicketPriority = when (this) {
    TicketPriority.LOW -> CCTicketPriority.LOW
    TicketPriority.MEDIUM -> CCTicketPriority.MEDIUM
    TicketPriority.HIGH -> CCTicketPriority.HIGH
    null -> CCTicketPriority.NONE
}

private fun TicketAssignObject?.toInternal(): CCTicket = if (this != null) {
    CCTicket(
        id = ticketId.toTicketId(),
        operatorId = operatorId.toTicketOperatorId()
    )
} else {
    CCTicket()
}