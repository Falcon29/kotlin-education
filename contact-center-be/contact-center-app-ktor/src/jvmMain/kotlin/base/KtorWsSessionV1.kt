package org.kotlined.base

import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import org.kotlined.cc.api.v1.apiV1ResponseSerialize
import org.kotlined.cc.api.v1.models.IResponse
import org.kotlined.common.ws.ICCWsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : ICCWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
