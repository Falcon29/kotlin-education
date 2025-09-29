package org.kotlined.cc.biz.validation

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.errorValidation
import org.kotlined.common.helpers.fail
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.validateTitleIsNotEmpty(title: String) = worker {
    this.title = title
    on { ticketValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<CCContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { ticketValidating.title.isNotEmpty() && ! ticketValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}