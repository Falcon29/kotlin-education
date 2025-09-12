package org.kotlined.common.ws

interface ICCWsSession {
    suspend fun <T> send(obj: T)

    companion object {
        val NONE = object : ICCWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}