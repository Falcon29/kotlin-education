package org.kotlined.cc.api.v2.mappers

import org.kotlined.cc.api.v2.models.*
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPermission
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus

fun CCContext.toTransportTicket(): IResponse = when (command) {
    CCCommand.CREATE -> toTransportCreate()
    CCCommand.GET -> toTransportGet()
    CCCommand.UPDATE -> toTransportUpdate()
    CCCommand.LIST -> toTransportList()
    CCCommand.ASSIGN -> toTransportAssign()
    CCCommand.NONE -> throw UnknownCCCommand(command)
}

fun CCContext.toTransportCreate() = TicketCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun CCContext.toTransportGet() = TicketGetResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun CCContext.toTransportAssign() = TicketAssignResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun CCContext.toTransportUpdate() = TicketUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun CCContext.toTransportList() = TicketListResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    tickets = ticketListResponse.toTransportTicket()
)

fun List<CCTicket>.toTransportTicket(): List<TicketObject>? = this
    .map { it.toTransportTicket() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun CCTicket.toTransportTicket(): TicketObject = TicketObject(
    id = id.toTransportTicket(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    status = status.toTransportStatus(),
    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString(),
    priority = priority.toTransportPriority(),
    createdAt = createdAt.takeIf { it.isNotBlank() },
    updatedAt = updatedAt.takeIf { it.isNotBlank() },
    permissions = permissions.toTransportPermissions()
)

internal fun CCTicketId.toTransportTicket() = takeIf { it != CCTicketId.NONE }?.asString()

private fun List<CCTicketPermission>.toTransportPermissions(): List<TicketPermissions>? = this
    .map { it.toTransportPermission() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun CCTicketPermission.toTransportPermission() = when (this) {
    CCTicketPermission.READ -> TicketPermissions.READ
    CCTicketPermission.UPDATE -> TicketPermissions.UPDATE
    CCTicketPermission.CLOSE -> TicketPermissions.CLOSE
    CCTicketPermission.ASSIGN -> TicketPermissions.ASSIGN
    CCTicketPermission.CHANGE_PRIORITY -> TicketPermissions.CHANGE_PRIORITY
    CCTicketPermission.NONE -> null
} ?: throw IllegalArgumentException("Unknown permission type")

internal fun CCTicketStatus.toTransportStatus(): TicketStatus? = when (this) {
    CCTicketStatus.NEW -> TicketStatus.NEW
    CCTicketStatus.IN_PROGRESS -> TicketStatus.IN_PROGRESS
    CCTicketStatus.CLOSED -> TicketStatus.CLOSED
    CCTicketStatus.NONE -> null
}

internal fun CCTicketPriority.toTransportPriority(): TicketPriority? = when (this) {
    CCTicketPriority.LOW -> TicketPriority.LOW
    CCTicketPriority.MEDIUM -> TicketPriority.MEDIUM
    CCTicketPriority.HIGH -> TicketPriority.HIGH
    CCTicketPriority.NONE -> null
}

internal fun List<CCError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() } as List<Error>?

internal fun CCError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

internal fun CCTicketStatus.toResult(): ResponseResult? = when (this) {
    CCTicketStatus.NEW, CCTicketStatus.IN_PROGRESS -> ResponseResult.SUCCESS
    CCTicketStatus.CLOSED -> ResponseResult.SUCCESS
    CCTicketStatus.NONE -> null
}