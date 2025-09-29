package org.kotlined.cc.biz.stubs

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCState
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == CCState.RUNNING }
    handle {
        fail(
            CCError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}