package org.kotlined.cc.api.v2.mappers

import org.kotlined.cc.api.v2.models.*
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketList
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.models.CCTicketOperatorId
import kotlin.reflect.KClass

fun CCTicket.toTransportCreateTicket() = TicketCreateObject(
    title = title,
    description = description,
    priority = priority.toTransportPriority()
)

fun CCTicket.toTransportGetTicket() = TicketGetObject(
    id = id.toTransportTicket()
)

fun CCTicketList.toTransportList() = TicketList(
    status = status.toTransportStatus(),
    priority = priority.toTransportPriority(),
    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString()
)

fun CCTicket.toTransportAssign() = TicketAssignObject(
    ticketId = id.toTransportTicket(),
    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString()
)

fun CCTicket.toTransportUpdateTicket() = TicketUpdateObject(
    id = id.toTransportTicket(),
    title = title,
    description = description,
    status = status.toTransportStatus(),
//    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
//    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString(),
    lock = lock.toTransportTicket(),
    priority = priority.toTransportPriority()
)

internal fun CCTicketLock.toTransportTicket() = takeIf { it != CCTicketLock.NONE }?.asString()

class UnknownRequestClass(clazz: KClass<*>) : RuntimeException("Unknown request class: $clazz")
class UnknownCCCommand(command: CCCommand) : RuntimeException("Unknown command: $command")