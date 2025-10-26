package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCStubs
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseErrWithData
import org.kotlined.common.repository.DBTicketResponseError
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.repoAssign(title: String) = worker {
    this.title = title
    this.description = "Назначение на оператора"
    on { stubCase == CCStubs.SUCCESS && state == CCState.RUNNING }
    handle {
        val request = DBTicketRequest(ticketRepoPrepare)
        when (val result = ticketRepo.assignTicket(request)) {
            is DBTicketResponseOk -> ticketRepoDone = result.data
            is DBTicketResponseError -> fail(result.errors)
            is DBTicketResponseErrWithData -> {
                fail(result.errors)
                ticketRepoDone = result.data
            }
        }
    }
}