package org.kotlined.cc.app.ktor.base

import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import org.kotlined.cc.api.v2.apiV2ResponseSerialize
import org.kotlined.cc.api.v2.models.IResponse
import org.kotlined.common.ws.ICCWsSession

data class KtorWsSessionV2(
    private val session: WebSocketSession
) : ICCWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV2ResponseSerialize(obj)))
    }
}