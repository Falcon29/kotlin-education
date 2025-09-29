package org.kotlined.cc.biz.validation

import org.kotlined.common.CCContext
import org.kotlined.common.helpers.errorValidation
import org.kotlined.common.helpers.fail
import org.kotlined.common.models.CCTicketId
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.validateId(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on {
        ticketValidating.id != CCTicketId.NONE && !ticketValidating.id.asString().matches(regExp)
                || ticketValidating.id.asString().isEmpty()
    }
    handle {
        val encodedId = ticketValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}