package org.kotlined.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CCTicketRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CCTicketRequestId("")
    }
}