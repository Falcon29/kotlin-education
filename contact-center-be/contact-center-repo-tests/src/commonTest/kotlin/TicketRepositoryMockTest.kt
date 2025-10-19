package org.kotlined.cc.repo.tests

import kotlinx.coroutines.test.runTest
import org.kotlined.cc.stubs.TicketStubs
import org.kotlined.common.models.CCTicket
import org.kotlined.common.repository.DBTicketIdRequest
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TicketRepositoryMockTest {
    private val repo = TicketRepositoryMock(
        invokeCreateTicket = { DBTicketResponseOk(TicketStubs.prepareResult { title = "create" }) },
        invokeGetTicket = { DBTicketResponseOk(TicketStubs.prepareResult { title = "get" }) },
        invokeUpdateTicket = { DBTicketResponseOk(TicketStubs.prepareResult { title = "update" }) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createTicket(DBTicketRequest(CCTicket()))
        assertIs<DBTicketResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockGet() = runTest {
        val result = repo.getTicket(DBTicketIdRequest(CCTicket()))
        assertIs<DBTicketResponseOk>(result)
        assertEquals("get", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateTicket(DBTicketRequest(CCTicket()))
        assertIs<DBTicketResponseOk>(result)
        assertEquals("update", result.data.title)
    }

}