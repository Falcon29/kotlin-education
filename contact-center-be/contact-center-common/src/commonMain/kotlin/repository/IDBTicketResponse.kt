package org.kotlined.common.repository

import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCTicket

sealed interface IDBTicketResponse : IDBResponse<CCTicket>

data class DBTicketResponseOk(
    val data: CCTicket,
) : IDBTicketResponse

data class DBTicketResponseError(
    val errors: List<CCError> = emptyList(),
) : IDBTicketResponse {
    constructor(err: CCError) : this(listOf(err))
}

data class DBTicketResponseErrWithData(
    val data: CCTicket,
    val errors: List<CCError> = emptyList(),
) : IDBTicketResponse {
    constructor(ticket: CCTicket, err: CCError) : this(ticket, listOf(err))
}