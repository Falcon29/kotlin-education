package org.kotlined

import org.kotlined.cc.stubs.TicketStubs
import org.kotlined.common.repository.DBTicketIdRequest
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.common.repository.IDBTicketResponse
import org.kotlined.common.repository.IRepoTicket

class TicketRepoStub : IRepoTicket {

    override suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse {
        return DBTicketResponseOk(
            data = TicketStubs.get(),
        )
    }

    override suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse {
        return DBTicketResponseOk(
            data = TicketStubs.get(),
        )
    }

    override suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse {
        return DBTicketResponseOk(
            data = TicketStubs.get(),
        )
    }

    override suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse {
        return DBTicketResponseOk(
            data = TicketStubs.get(),
        )
    }
}