package org.kotlined.cc.repo.tests

import org.kotlined.common.models.CCError
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.repository.DBTicketIdRequest
import org.kotlined.common.repository.DBTicketResponseError
import org.kotlined.common.repository.DBTicketResponseOk
import org.kotlined.common.repository.IRepoTicket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoTicketGetTest {
    abstract val repo: IRepoTicket
    protected open val getSucc = initObjects[0]

    @Test
    fun getSuccess() = runRepoTest {
        val result = repo.getTicket(DBTicketIdRequest(getSucc.id))
        assertIs<DBTicketResponseOk>(result)
        assertEquals(getSucc, result.data)
    }

    @Test
    fun getNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.getTicket(DBTicketIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DBTicketResponseError>(result)
        println("ERRORS: ${result.errors}")
        val error: CCError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTicket("get") {
        override val initObjects: List<CCTicket> = listOf(
            createInitTestModel("get")
        )

        val notFoundId = CCTicketId("ticket-repo-get-notFound")

    }
}
