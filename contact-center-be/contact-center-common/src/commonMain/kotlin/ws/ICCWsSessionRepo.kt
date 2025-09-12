package org.kotlined.common.ws

interface ICCWsSessionRepo {
    fun add(session: ICCWsSession)
    fun clearAll()
    fun remove(session: ICCWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : ICCWsSessionRepo {
            override fun add(session: ICCWsSession) {}
            override fun clearAll() {}
            override fun remove(session: ICCWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}