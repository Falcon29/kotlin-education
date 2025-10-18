package org.kotlined.common

import org.kotlined.cc.logging.common.CCLoggerProvider
import org.kotlined.common.repository.IRepoTicket
import org.kotlined.common.ws.ICCWsSessionRepo

data class CCCorSettings(
    val loggerProvider: CCLoggerProvider = CCLoggerProvider(),
    val wsSession: ICCWsSessionRepo = ICCWsSessionRepo.NONE,
    val repoStub: IRepoTicket = IRepoTicket.NONE,
    val repoTest: IRepoTicket = IRepoTicket.NONE,
    val repoProd: IRepoTicket = IRepoTicket.NONE,
) {
    companion object {
        val NONE = CCCorSettings()
    }
}
