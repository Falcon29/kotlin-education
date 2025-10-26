package org.kotlined.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketClientId
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPermission
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCTicketStatus

@Entity
data class TicketCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: String? = null,
    @field:CqlName(COLUMN_TITLE)
    var title: String? = null,
    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,
    @field:CqlName(COLUMN_STATUS)
    var status: TicketStatus? = null,
    @field:CqlName(COLUMN_CLIENT_ID)
    var clientId: String? = null,
    @field:CqlName(COLUMN_OPERATOR_ID)
    var operatorId: String? = null,
    @field:CqlName(COLUMN_PRIORITY)
    var priority: TicketPriority? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
    @field:CqlName(COLUMN_PERMISSIONS)
    var permissions: MutableList<CCTicketPermission> = mutableListOf(),
) {

    constructor(ticketModel: CCTicket) : this(
        id = ticketModel.id.takeIf { it != CCTicketId.NONE }?.asString(),
        title = ticketModel.title.takeIf { it.isNotBlank() },
        description = ticketModel.description.takeIf { it.isNotBlank() },
        status = ticketModel.status.toTransport(),
        clientId = ticketModel.clientId.takeIf { it != CCTicketClientId.NONE }?.asString(),
        operatorId = ticketModel.operatorId.takeIf { it != CCTicketOperatorId.NONE }?.asString(),
        priority = ticketModel.priority.toTransport(),
        lock = ticketModel.lock.takeIf { it != CCTicketLock.NONE }?.asString(),
        permissions = ticketModel.permissions.takeIf { it.isEmpty() } ?: mutableListOf()
    )

    fun toTicketModel(): CCTicket =
        CCTicket(
            id = id?.let { CCTicketId(it) } ?: CCTicketId.NONE,
            title = title ?: "",
            description = description ?: "",
            status = status.fromTransport(),
            clientId = clientId?.let { CCTicketClientId(it) } ?: CCTicketClientId.NONE,
            operatorId = operatorId?.let { CCTicketOperatorId(it) } ?: CCTicketOperatorId.NONE,
            priority = priority.fromTransport(),
            lock = lock?.let { CCTicketLock(it) } ?: CCTicketLock.NONE,
            permissions = permissions.toMutableList()
        )

    companion object {
        const val TABLE_NAME = "cc_ticket"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_STATUS = "status"
        const val COLUMN_CLIENT_ID = "client_id"
        const val COLUMN_OPERATOR_ID = "operator_id"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_LOCK = "lock"
        const val COLUMN_PERMISSIONS = "permissions"
    }
}