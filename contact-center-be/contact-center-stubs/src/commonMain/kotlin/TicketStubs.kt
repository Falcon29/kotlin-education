package org.kotlined.cc.stubs

import org.kotlined.cc.stubs.TicketStub.TICKET_LOGIN_ISSUE
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus

object TicketStubs {
    fun get(): CCTicket = TICKET_LOGIN_ISSUE.copy()

    fun prepareResult(block: CCTicket.() -> Unit): CCTicket = get().apply(block)

    fun prepareList() = listOf(
        ticket(
            "t-001",
            "Login problem",
            "Cannot login to system",
            CCTicketStatus.NEW,
            CCTicketPriority.HIGH
        ),
        ticket(
            "t-002",
            "Feature request",
            "Add dark mode",
            CCTicketStatus.NEW,
            CCTicketPriority.LOW
        ),
        ticket(
            "t-003",
            "Bug report",
            "App crashes on startup",
            CCTicketStatus.IN_PROGRESS,
            CCTicketPriority.HIGH
        )
    )

    fun prepareAssignedList(operatorId: CCTicketOperatorId) = listOf(
        assignedTicket(
            "t-007",
            "Slow performance",
            "App running very slow",
            CCTicketStatus.IN_PROGRESS,
            CCTicketPriority.MEDIUM,
            operatorId
        ),
        assignedTicket(
            "t-008",
            "UI glitch",
            "Buttons not responding",
            CCTicketStatus.IN_PROGRESS,
            CCTicketPriority.LOW,
            operatorId
        ),
    )

    private fun ticket(
        id: String,
        title: String,
        description: String,
        status: CCTicketStatus,
        priority: CCTicketPriority,
    ): CCTicket = TICKET_LOGIN_ISSUE.copy(
        id = CCTicketId(id),
        title = title,
        description = description,
        status = status,
        priority = priority,
        clientId = CCTicketClientId("client-$id"),
        operatorId = CCTicketOperatorId.NONE
    )


    private fun assignedTicket(
        id: String,
        title: String,
        description: String,
        status: CCTicketStatus,
        priority: CCTicketPriority,
        operatorId: CCTicketOperatorId,
    ) = TICKET_LOGIN_ISSUE.copy(
        id = CCTicketId(id),
        title = title,
        description = description,
        status = status,
        priority = priority,
        clientId = CCTicketClientId("client-$id"),
        operatorId = operatorId
    )
}