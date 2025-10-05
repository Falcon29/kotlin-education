package org.kotlined.cc.stubs

import org.kotlined.common.models.*

object TicketStub {

    val TICKET_LOGIN_ISSUE: CCTicket
        get() = CCTicket(
            id = CCTicketId("ticket-123"),
            title = "Cannot login to account",
            description = "I'm unable to login to my account since this morning. Getting 'invalid credentials' error.",
            status = CCTicketStatus.NEW,
            priority = CCTicketPriority.HIGH,
            clientId = CCTicketClientId("client-001"),
            operatorId = CCTicketOperatorId.NONE,
//            createdAt = "2025-09-28T18:56:00.477171100Z",
//            updatedAt = "2025-09-28T18:56:00.477171100Z",
            permissions = mutableListOf(
                CCTicketPermission.READ,
                CCTicketPermission.UPDATE,
                CCTicketPermission.CLOSE
            )
        )
}