package org.kotlined.cc.biz.validation

import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.chain
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.validation(block: ICorChainDsl<CCContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == CCState.RUNNING }
}

fun ICorChainDsl<CCContext>.finishValidation(title: String) = worker {
    this.title = title
    on { state == CCState.RUNNING }
    handle {
        ticketValidated = ticketValidating
    }
}
