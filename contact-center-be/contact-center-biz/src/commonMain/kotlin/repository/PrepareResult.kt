package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCWorkMode
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != CCWorkMode.STUB }
    handle {
        ticketResponse = ticketRepoDone
//        ticketsResponse = ticketsRepoDone
        state = when (val st = state) {
            CCState.RUNNING -> CCState.FINISHING
            else -> st
        }
    }
}