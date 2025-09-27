package org.kotlined.cc.app.ktor.v2

import io.ktor.server.application.ApplicationCall
import org.kotlined.cc.api.v2.models.*
import org.kotlined.cc.app.ktor.CCAppSettings

//val clCreate: KClass<*> = ApplicationCall::createTicket::class
suspend fun ApplicationCall.createTicket(appSettings: CCAppSettings) =
    processV2<TicketCreateRequest, TicketCreateResponse>(appSettings)

//val clGet: KClass<*> = ApplicationCall::getTicket::class
suspend fun ApplicationCall.getTicket(appSettings: CCAppSettings) =
    processV2<TicketGetRequest, TicketGetResponse>(appSettings)

//val clUpdate: KClass<*> = ApplicationCall::updateTicket::class
suspend fun ApplicationCall.updateTicket(appSettings: CCAppSettings) =
    processV2<TicketUpdateRequest, TicketUpdateResponse>(appSettings)

//val clAssign: KClass<*> = ApplicationCall::assignTicket::class
suspend fun ApplicationCall.assignTicket(appSettings: CCAppSettings) =
    processV2<TicketAssignRequest, TicketAssignResponse>(appSettings)

//val clList: KClass<*> = ApplicationCall::listTickets::class
suspend fun ApplicationCall.listTickets(appSettings: CCAppSettings) =
    processV2<TicketListRequest, TicketListResponse>(appSettings)
