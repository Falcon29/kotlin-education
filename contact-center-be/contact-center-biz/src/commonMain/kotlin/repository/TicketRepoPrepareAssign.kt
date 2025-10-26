package org.kotlined.cc.biz.repository

import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.repoPrepareAssign(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, и данные, полученные от пользователя"
    on { state == CCState.RUNNING }
    handle {
        ticketRepoPrepare = ticketRepoGet.copy().apply {
            operatorId = ticketValidated.operatorId
        }
    }
}
