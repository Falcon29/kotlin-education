package org.kotlined.cc.app.ktor.v1

import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kotlined.cc.app.ktor.CCAppSettings

fun Route.v1Ticket(appSettings: CCAppSettings) {
    route("ad") {
        post("create") {
            call.createTicket(appSettings)
        }
        post("get") {
            call.getTicket(appSettings)
        }
        post("update") {
            call.updateTicket(appSettings)
        }
        post("assign") {
            call.assignTicket(appSettings)
        }
        post("list") {
            call.listTickets(appSettings)
        }
    }
}