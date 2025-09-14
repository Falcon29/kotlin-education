package org.kotlined.common.models

data class CCTicketList(
    var status: CCTicketStatus = CCTicketStatus.NONE,
    var priority: CCTicketPriority = CCTicketPriority.NONE,
    var clientId: CCTicketClientId = CCTicketClientId.NONE,
    var operatorId: CCTicketOperatorId = CCTicketOperatorId.NONE
)
