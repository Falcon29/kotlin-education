package org.kotlined.cc.app.ktor.v2

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import org.kotlined.cc.api.v2.apiV2Mapper
import org.kotlined.cc.api.v2.mappers.fromTransport
import org.kotlined.cc.api.v2.mappers.toTransportTicket
import org.kotlined.cc.api.v2.models.IRequest
import org.kotlined.cc.api.v2.models.IResponse
import org.kotlined.cc.app.common.controllerHelper
import org.kotlined.cc.app.ktor.CCAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV2(
    appSettings: CCAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(this@processV2.receive<Q>())
    },
    {
        this@processV2.respond(toTransportTicket())
    },
    clazz,
    logId,
)