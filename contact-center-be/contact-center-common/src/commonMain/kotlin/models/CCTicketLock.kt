package org.kotlined.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CCTicketLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CCTicketLock("")
    }
}