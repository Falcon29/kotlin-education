package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.errorSystem
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCWorkMode
import org.kotlined.common.repository.IRepoTicket
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        ticketRepo = when {
            workMode == CCWorkMode.TEST -> corSettings.repoTest
            workMode == CCWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != CCWorkMode.STUB && ticketRepo == IRepoTicket.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DBNotConfiguredException(workMode)
            )
        )
    }
}

class DBNotConfiguredException(workMode: CCWorkMode) : Exception(
    "Database is not configured properly for work mode $workMode"
)