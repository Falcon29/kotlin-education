package org.kotlined.cc.app.ktor.v1

import io.ktor.server.application.ApplicationCall
import org.kotlined.cc.api.v1.models.*
import org.kotlined.cc.app.ktor.CCAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTicket::class
suspend fun ApplicationCall.createTicket(appSettings: CCAppSettings) =
    processV1<TicketCreateRequest, TicketCreateResponse>(appSettings, clCreate, "create")

val clGet: KClass<*> = ApplicationCall::getTicket::class
suspend fun ApplicationCall.getTicket(appSettings: CCAppSettings) =
    processV1<TicketGetRequest, TicketGetResponse>(appSettings, clGet, "get")

val clUpdate: KClass<*> = ApplicationCall::updateTicket::class
suspend fun ApplicationCall.updateTicket(appSettings: CCAppSettings) =
    processV1<TicketUpdateRequest, TicketUpdateResponse>(appSettings, clUpdate, "update")

val clAssign: KClass<*> = ApplicationCall::assignTicket::class
suspend fun ApplicationCall.assignTicket(appSettings: CCAppSettings) =
    processV1<TicketAssignRequest, TicketAssignResponse>(appSettings, clAssign, "assign")

val clList: KClass<*> = ApplicationCall::listTickets::class
suspend fun ApplicationCall.listTickets(appSettings: CCAppSettings) =
    processV1<TicketListRequest, TicketListResponse>(appSettings, clList, "list")
