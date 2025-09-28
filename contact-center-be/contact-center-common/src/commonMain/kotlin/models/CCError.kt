package org.kotlined.common.models

import org.kotlined.cc.logging.common.LogLevel

data class CCError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: LogLevel = LogLevel.ERROR
)
