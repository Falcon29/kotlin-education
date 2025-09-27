package org.kotlined.common

import org.kotlined.common.ws.ICCWsSessionRepo

data class CCCorSettings(
    val wsSession: ICCWsSessionRepo = ICCWsSessionRepo.NONE
) {
    companion object {
        val NONE = CCCorSettings()
    }
}
