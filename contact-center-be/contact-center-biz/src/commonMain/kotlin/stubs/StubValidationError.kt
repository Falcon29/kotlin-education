package org.kotlined.cc.biz.stubs

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCStubs
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.stubValidationBadID(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора тикета
    """.trimIndent()
    on { stubCase == CCStubs.BAD_ID && state == CCState.RUNNING }
    handle {
        fail(
            CCError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}

fun ICorChainDsl<CCContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для заголовка
    """.trimIndent()

    on { stubCase == CCStubs.BAD_TITLE && state == CCState.RUNNING }
    handle {
        fail(
            CCError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}

fun ICorChainDsl<CCContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания обращения
    """.trimIndent()
    on { stubCase == CCStubs.BAD_DESCRIPTION && state == CCState.RUNNING }
    handle {
        fail(
            CCError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}