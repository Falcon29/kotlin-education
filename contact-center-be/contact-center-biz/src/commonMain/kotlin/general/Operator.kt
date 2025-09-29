package org.kotlined.cc.biz.general

import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.chain

fun ICorChainDsl<CCContext>.operation(
    title: String,
    command: CCCommand,
    block: ICorChainDsl<CCContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == CCState.RUNNING }
}