package org.kotlined.cc.app.ktor.base

import org.kotlined.common.ws.ICCWsSession
import org.kotlined.common.ws.ICCWsSessionRepo

class KtorWsSessionRepo: ICCWsSessionRepo {
    private val sessions: MutableSet<ICCWsSession> = mutableSetOf()
    override fun add(session: ICCWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: ICCWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}