package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCState
import org.kotlined.common.repository.DBTicketIdRequest
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseErrWithData
import org.kotlined.common.repository.DBTicketResponseError
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.repoGet(title: String) = worker {
    this.title = title
    description = "Чтение тикета в БД"
    on { state == CCState.RUNNING }
    handle {
        val request = DBTicketIdRequest(ticketValidated)
        when (val result = ticketRepo.getTicket(request)) {
            is DBTicketResponseOk -> ticketRepoGet = result.data
            is DBTicketResponseError -> fail(result.errors)
            is DBTicketResponseErrWithData -> {
                fail(result.errors)
                ticketRepoGet = result.data
            }
        }
    }
}