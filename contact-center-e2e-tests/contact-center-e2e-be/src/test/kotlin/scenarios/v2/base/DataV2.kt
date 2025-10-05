package org.kotlined.cc.test.e2e.be.scenarios.v2.base

import org.kotlined.cc.api.v2.models.*

val debug = TicketDebug(mode = TicketRequestDebugMode.STUB, stub = TicketRequestDebugStubs.SUCCESS)

val createTicket = TicketCreateObject(
    title = "Ticket for wiremock",
    description = "Ticket for wiremock description",
    priority = TicketPriority.HIGH
)