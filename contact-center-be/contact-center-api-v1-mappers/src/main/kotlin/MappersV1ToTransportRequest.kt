package org.kotlined

import org.kotlined.api.v1.models.TicketCreateObject
import org.kotlined.api.v1.models.TicketGetObject
import org.kotlined.api.v1.models.TicketUpdateObject
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCTicket
fun CCTicket.toTransportCreateTicket() = TicketCreateObject(
    title = title,
    description = description,
//    status = status.toTransportStatus(),
//    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
    priority = priority.toTransportPriority()
)

fun CCTicket.toTransportReadTicket() = TicketGetObject(
    id = id.toTransportTicket()
)

fun CCTicket.toTransportUpdateTicket() = TicketUpdateObject(
    id = id.toTransportTicket(),
    title = title,
    description = description,
    status = status.toTransportStatus(),
//    clientId = clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
//    operatorId = operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString(),
    priority = priority.toTransportPriority()
)

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Unknown request class: $clazz")
class UnknownCCCommand(command: CCCommand) : RuntimeException("Unknown command: $command")