package org.kotlined.cc.inmemory

import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus

data class TicketEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val priority: String? = null,
    val status: String? = null,
    val operatorId: String? = null,
    val clientId: String? = null,
//    val permissions: List<String?>? = mutableListOf(),
    val lock: String? = null,
) {
    constructor(model: CCTicket) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        priority = model.priority.takeIf { it != CCTicketPriority.NONE }?.name,
        status = model.status.takeIf { it != CCTicketStatus.NONE }?.name,
        operatorId = model.id.asString().takeIf { it.isNotBlank() },
        clientId = model.id.asString().takeIf { it.isNotBlank() },
        lock = model.id.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = CCTicket(
        id = id?.let { CCTicketId(it) } ?: CCTicketId.NONE,
        title = title ?: "",
        description = description ?: "",
        status = status?.let { CCTicketStatus.valueOf(it) } ?: CCTicketStatus.NONE,
        clientId = clientId?.let { CCTicketClientId(it) } ?: CCTicketClientId.NONE,
        operatorId = operatorId?.let { CCTicketOperatorId(it) } ?: CCTicketOperatorId.NONE,
        priority = priority?.let { CCTicketPriority.valueOf(it) } ?: CCTicketPriority.NONE,
        lock = lock?.let { CCTicketLock(it) } ?: CCTicketLock.NONE,
    )
}