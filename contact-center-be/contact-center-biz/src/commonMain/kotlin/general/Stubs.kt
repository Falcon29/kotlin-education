package org.kotlined.cc.biz.general

import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCWorkMode
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.chain

fun ICorChainDsl<CCContext>.stubs(title: String, block: ICorChainDsl<CCContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CCWorkMode.STUB && state == CCState.RUNNING }
}