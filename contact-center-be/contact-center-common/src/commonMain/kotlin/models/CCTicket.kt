package org.kotlined.common.models

data class CCTicket(
    var  id: CCTicketId = CCTicketId.NONE,
    var  title: String = "",
    var  description: String = "",
    var  status: CCTicketStatus = CCTicketStatus.NONE,
    var  clientId: CCTicketClientId = CCTicketClientId.NONE,
    var  operatorId: CCTicketOperatorId = CCTicketOperatorId.NONE,
    var  priority: CCTicketPriority = CCTicketPriority.NONE,
    var  createdAt: String = "",
    var  updatedAt: String = "",
    var  permissions: MutableSet<CCTicketPermission> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = CCTicket()
    }
}