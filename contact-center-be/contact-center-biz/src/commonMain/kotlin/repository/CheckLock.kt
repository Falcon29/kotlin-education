package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCState
import org.kotlined.common.repository.errorRepoConcurrency
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == CCState.RUNNING && ticketValidated.lock != ticketRepoGet.lock }
    handle {
        fail(errorRepoConcurrency(ticketRepoGet, ticketValidated.lock).errors)
    }
}
