package org.kotlined.cc.repo.tests

import org.kotlined.common.models.*
import org.kotlined.common.repository.DBTicketRequest
import org.kotlined.common.repository.DBTicketResponseErrWithData
import org.kotlined.common.repository.DBTicketResponseError
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.common.repository.IRepoTicket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoTicketUpdateTest {
    abstract val repo: IRepoTicket
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = CCTicketId("ticket-repo-update-not-found")
    protected val lockBad = CCTicketLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = CCTicketLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdSuccessObj by lazy {
        CCTicket(
            id = updateSucc.id,
            title = "upd object",
            description = "upd object description",
            priority = CCTicketPriority.LOW,
            status = CCTicketStatus.IN_PROGRESS,
            clientId = CCTicketClientId("clientidtest"),
            operatorId = CCTicketOperatorId.NONE,
            lock = initObjects.first().lock
        )
    }

    private val reqUpdConcObj by lazy {
        CCTicket(
            id = updateConc.id,
            title = "upd object",
            description = "upd object description",
            priority = CCTicketPriority.LOW,
            status = CCTicketStatus.IN_PROGRESS,
            clientId = CCTicketClientId("clientidtest"),
            operatorId = CCTicketOperatorId.NONE,
            lock = lockBad
        )
    }

    private val reqUpdNotFoundObj = CCTicket(
        id = updateIdNotFound,
        title = "upd object not found",
        description = "upd object description not found",
        priority = CCTicketPriority.LOW,
        status = CCTicketStatus.IN_PROGRESS,
        clientId = CCTicketClientId("clientidtest"),
        operatorId = CCTicketOperatorId.NONE,
        lock = initObjects.first().lock
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTicket(DBTicketRequest(reqUpdSuccessObj))
        println("ERRORS: ${(result as? DBTicketResponseError)?.errors}")
        println("ERRORSWD: ${(result as? DBTicketResponseErrWithData)?.errors}")
        assertIs<DBTicketResponseOk>(result)
        assertNotEquals(CCTicketId.NONE, result.data.id)
        assertEquals(reqUpdSuccessObj.id, result.data.id)
        assertEquals(reqUpdSuccessObj.title, result.data.title)
        assertEquals(reqUpdSuccessObj.description, result.data.description)
        assertEquals(reqUpdSuccessObj.priority, result.data.priority)
        assertEquals(reqUpdSuccessObj.status, result.data.status)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTicket(DBTicketRequest(reqUpdNotFoundObj))
        assertIs<DBTicketResponseError>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTicket(DBTicketRequest(reqUpdConcObj))
        assertIs<DBTicketResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitTicket("update") {
        override val initObjects: List<CCTicket> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
