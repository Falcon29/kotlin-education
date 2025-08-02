package org.kotlined.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CCRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CCRequestId("")
    }
}