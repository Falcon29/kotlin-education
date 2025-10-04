package org.kotlined.cc.api.log.mapper

import kotlinx.datetime.Clock
import org.kotlined.cc.api.log.models.CCLogModel
import org.kotlined.cc.api.log.models.CommonLogModel
import org.kotlined.cc.api.log.models.ErrorLogModel
import org.kotlined.cc.api.log.models.TicketLog
import org.kotlined.common.CCContext
import org.kotlined.common.models.*

fun CCContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-contact-center",
    tickets = toCcLog(),
    errors = errors.map { it.toLog() },
)

private fun CCContext.toCcLog(): CCLogModel? {
    val ticketNone = CCTicket()
    return CCLogModel(
        requestId = requestId.takeIf { it != CCTicketRequestId.NONE }?.asString(),
//        operation = operation.takeIf { it != CcLogOperation.NONE }?.name,
        requestTicket = ticketRequest.takeIf { it != ticketNone }?.toLog(),
        responseTicket = ticketResponse.takeIf { it != ticketNone }?.toLog(),
//        responseTickets = ticketListResponse.takeIf { it != CCTicketList() }?.toLog(),
    ).takeIf { it != CCLogModel() }
}

private fun CCError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun CCTicket.toLog() = TicketLog(
    id = id.takeIf { it != CCTicketId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    status = status.takeIf { it != CCTicketStatus.NONE }?.name,
    priority = priority.takeIf { it != CCTicketPriority.NONE }?.name,
    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString(),
//    createdAt = createdAt.takeIf { it.isNotBlank() },
//    updatedAt = updatedAt.takeIf { it.isNotBlank() },
    permissions = permissions.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)