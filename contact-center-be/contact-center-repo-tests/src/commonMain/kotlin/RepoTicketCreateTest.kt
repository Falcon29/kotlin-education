package org.kotlined.cc.repo.tests

import org.kotlined.cc.repo.common.ITicketRepoInit
import org.kotlined.common.models.*
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoTicketCreateTest {
    abstract val repo: ITicketRepoInit
    protected open val uuidNew = CCTicketId("10000000-0000-0000-0000-000000000001")

    private val createObj = CCTicket(
        title = "create object",
        description = "create object description",
        priority = CCTicketPriority.MEDIUM,
        status = CCTicketStatus.NEW,
        clientId = CCTicketClientId("clientidtest"),
        operatorId = CCTicketOperatorId.NONE
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTicket(DBTicketRequest(createObj))
        val expected = createObj
        assertIs<DBTicketResponseOk>(result)
        assertNotEquals(CCTicketId.NONE, result.data.id)
        assertEquals(uuidNew.asString(), result.data.lock.asString())
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.priority, result.data.priority)
        assertEquals(expected.status, result.data.status)
        assertEquals(expected.operatorId, result.data.operatorId)
        assertEquals(expected.clientId, result.data.clientId)
    }

    companion object : BaseInitTicket("create") {
        override val initObjects: List<CCTicket> = emptyList()
    }
}
