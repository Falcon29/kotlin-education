package org.kotlined.v1

import io.ktor.server.application.ApplicationCall
import org.kotlined.cc.api.v1.models.*
import org.kotlined.cc.app.ktor.CCAppSettings

//val clCreate: KClass<*> = ApplicationCall::createTicket::class
suspend fun ApplicationCall.createTicket(appSettings: CCAppSettings) =
    processV1<TicketCreateRequest, TicketCreateResponse>(appSettings)

//val clGet: KClass<*> = ApplicationCall::getTicket::class
suspend fun ApplicationCall.getTicket(appSettings: CCAppSettings) =
    processV1<TicketGetRequest, TicketGetResponse>(appSettings)

//val clUpdate: KClass<*> = ApplicationCall::updateTicket::class
suspend fun ApplicationCall.updateTicket(appSettings: CCAppSettings) =
    processV1<TicketUpdateRequest, TicketUpdateResponse>(appSettings)

//val clAssign: KClass<*> = ApplicationCall::assignTicket::class
suspend fun ApplicationCall.assignTicket(appSettings: CCAppSettings) =
    processV1<TicketAssignRequest, TicketAssignResponse>(appSettings)

//val clList: KClass<*> = ApplicationCall::listTickets::class
suspend fun ApplicationCall.listTickets(appSettings: CCAppSettings) =
    processV1<TicketListRequest, TicketListResponse>(appSettings)
