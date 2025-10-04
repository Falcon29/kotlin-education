package org.kotlined.cc.biz.validation

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.errorValidation
import org.kotlined.common.helpers.fail
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.validateDescription(title: String) = worker {
    this.title = title
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