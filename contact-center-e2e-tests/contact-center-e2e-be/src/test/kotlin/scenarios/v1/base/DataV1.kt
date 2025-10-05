package org.kotlined.cc.test.e2e.be.scenarios.v1.base

import org.kotlined.cc.api.v1.models.*

val createTicket = TicketCreateObject(
    title = "Ticket for wiremock",
    description = "Ticket for wiremock description",
    priority = TicketPriority.HIGH
)