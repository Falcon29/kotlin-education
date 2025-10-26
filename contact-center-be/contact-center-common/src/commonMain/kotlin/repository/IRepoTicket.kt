package org.kotlined.common.repository

interface IRepoTicket {
    suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse
    suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse
    suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse
    suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse
//    suspend fun listTickets(rq: DBTicketRequest): IDBTicketResponse

    companion object {
        val NONE = object : IRepoTicket {
            override suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse {
                TODO("Not yet implemented")
            }

            override suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse {
                TODO("Not yet implemented")
            }

        }
    }
}