package org.kotlined.base

import io.ktor.http.cio.Response
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import org.kotlined.common.ws.ICCWsSession

data class KtorWsSession(private val session: WebSocketSession) : ICCWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is Response)
        session.send(Frame.Text(apiV1ResponseSerialize())
    }

}
