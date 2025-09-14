package org.kotlined.cc.app.ktor.v2

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.kotlined.cc.api.v2.mappers.fromTransport
import org.kotlined.cc.api.v2.mappers.toTransportTicket
import org.kotlined.cc.api.v2.models.IRequest
import org.kotlined.cc.api.v2.models.IResponse
import org.kotlined.cc.app.common.controllerHelper
import org.kotlined.cc.app.ktor.CCAppSettings

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: CCAppSettings
) = appSettings.controllerHelper(
    {
        fromTransport(this@processV2.receive<Q>())
    },
    { this@processV2.respond(toTransportTicket() as R) }
)