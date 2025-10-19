package org.kotlined.cc.repo.tests

import org.kotlined.common.models.*

abstract class BaseInitTicket(private val op: String) : IInitObjects<CCTicket> {
    open val lockOld: CCTicketLock = CCTicketLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: CCTicketLock = CCTicketLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        clientId: CCTicketClientId = CCTicketClientId("owner-123"),
        priority: CCTicketPriority = CCTicketPriority.MEDIUM,
        lock: CCTicketLock = lockOld,
    ) = CCTicket(
        id = CCTicketId("ticket-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        priority = priority,
        status = CCTicketStatus.NEW,
        clientId = clientId,
        lock = lock,
    )
}

internal interface IInitObjects<T> {
    val initObjects: List<T>
}