package org.kotlined.v1

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.kotlined.cc.api.v1.models.IRequest
import org.kotlined.cc.api.v1.models.IResponse
import org.kotlined.cc.app.common.controllerHelper
import org.kotlined.cc.app.ktor.CCAppSettings
import org.kotlined.cc.mappers.v1.fromTransport
import org.kotlined.cc.mappers.v1.toTransportTicket

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: CCAppSettings,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransportTicket()) }
)