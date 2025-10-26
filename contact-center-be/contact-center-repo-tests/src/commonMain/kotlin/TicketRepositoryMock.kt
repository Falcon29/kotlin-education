package org.kotlined.cc.repo.tests

import org.kotlined.common.models.CCTicket
import org.kotlined.common.repository.DBTicketIdRequest
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.common.repository.IDBTicketResponse
import org.kotlined.common.repository.IRepoTicket

class TicketRepositoryMock(
    private val invokeCreateTicket: (DBTicketRequest) -> IDBTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeGetTicket: (DBTicketIdRequest) -> IDBTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateTicket: (DBTicketRequest) -> IDBTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
) : IRepoTicket {

    override suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse = invokeCreateTicket(rq)

    override suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse = invokeGetTicket(rq)

    override suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse = invokeUpdateTicket(rq)

    override suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse {
        TODO("Not yet implemented")
    }


    companion object {
        val DEFAULT_TICKET_SUCCESS_EMPTY_MOCK = DBTicketResponseOk(CCTicket())
    }
}