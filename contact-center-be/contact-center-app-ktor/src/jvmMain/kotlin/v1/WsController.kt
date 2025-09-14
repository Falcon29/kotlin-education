package org.kotlined.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import org.kotlined.base.KtorWsSessionV1
import org.kotlined.cc.api.v1.apiV1Mapper
import org.kotlined.cc.api.v1.models.IRequest
import org.kotlined.cc.app.common.controllerHelper
import org.kotlined.cc.app.ktor.CCAppSettings
import org.kotlined.cc.mappers.v1.fromTransport
import org.kotlined.cc.mappers.v1.toTransportGet
import org.kotlined.cc.mappers.v1.toTransportTicket
import org.kotlined.common.models.CCCommand

//private val clWsV1: KClass<*> = WebSocketSession::wsHandlerV1::class
suspend fun WebSocketSession.wsHandlerV1(appSettings: CCAppSettings) = with(KtorWsSessionV1(this)) {
    val sessions = appSettings.corSettings.wsSession
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = CCCommand.GET
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportGet()))) }
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                {
                    fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText()))
                    wsSession = this@with
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportTicket())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                }
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this@with)
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = CCCommand.NONE
                    wsSession = this@with
                },
                { },
            )
            sessions.remove(this@with)
        }
    }.collect()
}
