package org.kotlined.cc.biz.stubs

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { state == CCState.RUNNING }
    handle {
        fail(
            CCError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}