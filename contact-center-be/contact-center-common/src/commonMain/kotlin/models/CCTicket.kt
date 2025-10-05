package org.kotlined.common.models

import kotlinx.datetime.Clock

data class CCTicket(
    var id: CCTicketId = CCTicketId.NONE,
    var title: String = "",
    var description: String = "",
    var status: CCTicketStatus = CCTicketStatus.NONE,
    var clientId: CCTicketClientId = CCTicketClientId.NONE,
    var operatorId: CCTicketOperatorId = CCTicketOperatorId.NONE,
    var priority: CCTicketPriority = CCTicketPriority.NONE,
//    var createdAt: String = Clock.System.now().toString(),
//    var updatedAt: String = Clock.System.now().toString(),
    var lock: CCTicketLock = CCTicketLock.NONE,
    var permissions: MutableList<CCTicketPermission> = mutableListOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = CCTicket()
    }
}