package org.kotlined.common.repository

import org.kotlined.common.helpers.errorSystem

abstract class TicketRepoBase: IRepoTicket {

    protected suspend fun tryTicketMethod(block: suspend () -> IDBTicketResponse) = try {
        block()
    } catch (e: Throwable) {
        DBTicketResponseError(errorSystem("methodException", e = e))
    }
}