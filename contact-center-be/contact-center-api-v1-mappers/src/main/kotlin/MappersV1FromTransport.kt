package org.kotlined

import org.kotlined.api.v1.models.IRequest
import org.kotlined.api.v1.models.TicketCreateObject
import org.kotlined.api.v1.models.TicketCreateRequest
import org.kotlined.api.v1.models.TicketDebug
import org.kotlined.api.v1.models.TicketDebugMode
import org.kotlined.api.v1.models.TicketGetObject
import org.kotlined.api.v1.models.TicketGetRequest
import org.kotlined.api.v1.models.TicketListFilter
import org.kotlined.api.v1.models.TicketPriority
import org.kotlined.api.v1.models.TicketSearchObject
import org.kotlined.api.v1.models.TicketSearchRequest
import org.kotlined.api.v1.models.TicketStatus
import org.kotlined.api.v1.models.TicketUpdateObject
import org.kotlined.api.v1.models.TicketUpdateRequest
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketFilter
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus
import org.kotlined.common.models.CCWorkMode

fun CCContext.fromTransport(request: IRequest) = when (request) {
    is TicketCreateRequest -> fromTransport(request)
    is TicketGetRequest -> fromTransport(request)
    is TicketUpdateRequest -> fromTransport(request)
    is TicketSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTicketId() = this?.let { CCTicketId(it) } ?: CCTicketId.NONE
private fun String?.toTicketClientId() = this?.let { CCTicketClientId(it) } ?: CCTicketClientId.NONE
private fun String?.toTicketOperatorId() = this?.let { CCTicketOperatorId(it) } ?: CCTicketOperatorId.NONE

private fun TicketDebug?.transportToWorkMode(): CCWorkMode = when (this?.mode) {
    TicketDebugMode.PROD -> CCWorkMode.PROD
    TicketDebugMode.TEST -> CCWorkMode.TEST
    TicketDebugMode.STUB -> CCWorkMode.STUB
    null -> CCWorkMode.PROD
}

fun CCContext.fromTransport(request: TicketCreateRequest) {
    command = CCCommand.CREATE
    ticketRequest = request.ticket?.toInternal() ?: CCTicket()
//    workMode = request.debug.transportToWorkMode()
}

fun CCContext.fromTransport(request: TicketGetRequest) {
    command = CCCommand.GET
    ticketRequest = request.ticket.toInternal()
//    workMode = request.debug.transportToWorkMode()
}

private fun TicketGetObject?.toInternal(): CCTicket = if (this != null) {
    CCTicket(id = id.toTicketId())
} else {
    CCTicket()
}

fun CCContext.fromTransport(request: TicketUpdateRequest) {
    command = CCCommand.UPDATE
    ticketRequest = request.ticket?.toInternal() ?: CCTicket()
//    workMode = request.debug.transportToWorkMode()
}


fun CCContext.fromTransport(request: TicketSearchRequest) {
    command = CCCommand.SEARCH
//    ticketFilterRequest = request.t.toInternal()
//    workMode = request.debug.transportToWorkMode()
}

private fun TicketListFilter?.toInternal(): CCTicketFilter = CCTicketFilter(
    status = this?.status?.toStatus() ?: CCTicketStatus.NONE,
    priority = this?.priority?.toPriority() ?: CCTicketPriority.NONE,
    clientId = this?.clientId.toTicketClientId(),
    operatorId = this?.operatorId.toTicketOperatorId()
)

private fun TicketCreateObject.toInternal(): CCTicket = CCTicket(
    title = this.title ?: "",
    description = this.description ?: "",
    status = CCTicketStatus.NEW,
//    clientId = this.clientId.toTicketClientId(),
    priority = this.priority.toPriority(),
)

private fun TicketUpdateObject.toInternal(): CCTicket = CCTicket(
    id = this.id.toTicketId(),
    title = this.title ?: "",
    description = this.description ?: "",
    status = this.status.toStatus(),
//    clientId = this.clientId.toTicketClientId(),
//    operatorId = this.operatorId.toTicketOperatorId(),
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
