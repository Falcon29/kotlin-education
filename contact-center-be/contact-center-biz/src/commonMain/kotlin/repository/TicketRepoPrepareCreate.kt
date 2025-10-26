package org.kotlined.cc.biz.repository

import org.kotlined.cc.stubs.TicketStubs
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка сохранения тикета в БД"
    on { state == CCState.RUNNING }
    handle {
        ticketRepoPrepare = ticketValidated.copy()
        // TODO user management
        ticketRepoPrepare.clientId = TicketStubs.get().clientId
    }
}