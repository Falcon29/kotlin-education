package org.kotlined.cc.app.ktor.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import org.kotlined.cc.api.v2.apiV2RequestDeserialize
import org.kotlined.cc.api.v2.apiV2ResponseSerialize
import org.kotlined.cc.api.v2.mappers.fromTransport
import org.kotlined.cc.api.v2.mappers.toTransportGet
import org.kotlined.cc.api.v2.mappers.toTransportTicket
import org.kotlined.cc.api.v2.models.IRequest
import org.kotlined.cc.app.common.controllerHelper
import org.kotlined.cc.app.ktor.CCAppSettings
import org.kotlined.cc.app.ktor.base.KtorWsSessionV2
import org.kotlined.common.models.CCCommand
import kotlin.reflect.KClass

private val clWsV2: KClass<*> = WebSocketSession::wsHandlerV2::class
suspend fun WebSocketSession.wsHandlerV2(appSettings: CCAppSettings) = with(KtorWsSessionV2(this)) {
    // Обновление реестра сессий
    val sessions = appSettings.corSettings.wsSession
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = CCCommand.GET
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV2ResponseSerialize(toTransportGet()))) },
        clWsV2,
        "wsV2-init"
    )

    // Handle flow
    incoming.receiveAsFlow()
        .mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull
            // Handle without flow destruction
            try {
                appSettings.controllerHelper(
                    {
                        fromTransport(apiV2RequestDeserialize<IRequest>(frame.readText()))
                        wsSession = this@with
                    },
                    {
                        val result = apiV2ResponseSerialize(toTransportTicket())
                        // If change request, response is sent to everyone
                        outgoing.send(Frame.Text(result))
                    },
                    clWsV2,
                    "wsV2-handle"
                )

            } catch (_: ClosedReceiveChannelException) {
                sessions.remove(this@with)
            } catch (e: Throwable) {
                println("FFF")
            }
        }
        .onCompletion {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = CCCommand.NONE
                    wsSession = this@with
                },
                { },
                clWsV2,
                "wsV2-finish"
            )
            sessions.remove(this@with)
        }
        .collect()
}