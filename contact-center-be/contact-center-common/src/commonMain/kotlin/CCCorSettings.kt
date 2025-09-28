package org.kotlined.common

import org.kotlined.cc.logging.common.CCLoggerProvider
import org.kotlined.common.ws.ICCWsSessionRepo

data class CCCorSettings(
    val loggerProvider: CCLoggerProvider = CCLoggerProvider(),
    val wsSession: ICCWsSessionRepo = ICCWsSessionRepo.NONE
) {
    companion object {
        val NONE = CCCorSettings()
    }
}
