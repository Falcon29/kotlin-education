package org.kotlined.cc.test.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.Test
import org.kotlined.cc.api.v1.models.ResponseResult
import org.kotlined.cc.api.v1.models.TicketCreateRequest
import org.kotlined.cc.api.v1.models.TicketCreateResponse
import org.kotlined.cc.api.v1.models.TicketDebug
import org.kotlined.cc.api.v1.models.TicketGetObject
import org.kotlined.cc.api.v1.models.TicketGetRequest
import org.kotlined.cc.api.v1.models.TicketGetResponse
import org.kotlined.cc.api.v1.models.TicketPriority
import org.kotlined.cc.api.v1.models.TicketResponseObject
import org.kotlined.cc.api.v1.models.TicketUpdateObject
import org.kotlined.cc.api.v1.models.TicketUpdateRequest
import org.kotlined.cc.api.v1.models.TicketUpdateResponse
import org.kotlined.cc.test.e2e.be.base.client.Client
import org.kotlined.cc.test.e2e.be.scenarios.v1.base.createTicket
import org.kotlined.cc.test.e2e.be.scenarios.v1.base.sendAndReceive
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: TicketDebug? = null,
) {

    @Test
    fun update() = runBlocking {
        val obj = createTicket
        val restCreate = client.sendAndReceive(
            path = "ticket/create",
            TicketCreateRequest(
                requestType = "create",
                debug = debug,
                ticket = obj,
            )
        ) as TicketCreateResponse

        assertEquals(ResponseResult.SUCCESS, restCreate.result)

        val cObj: TicketResponseObject = restCreate.ticket ?: fail("No ticket in Create response")
        val uObj = TicketUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            title = "Ticket update",
            description = "Ticket update desc",
            priority = TicketPriority.MEDIUM
        )

        val restUpdate = client.sendAndReceive(
            path = "ticket/update",
            TicketUpdateRequest(
                requestType = "update",
                debug = debug,
                ticket = uObj
            )
        ) as TicketUpdateResponse

        assertEquals(ResponseResult.SUCCESS, restUpdate.result)

        val usObj: TicketResponseObject = restUpdate.ticket ?: fail("No ticket in upd response")
        assertEquals(uObj.title, usObj.title)
        assertEquals(uObj.description, usObj.description)
        assertEquals(uObj.priority, usObj.priority)

        val restGet = client.sendAndReceive(
            path = "ticket/get",
            TicketGetRequest(
                requestType = "get",
                debug = debug,
                ticket = TicketGetObject(id = cObj.id)
            )
        ) as TicketGetResponse

        assertEquals(ResponseResult.SUCCESS, restGet.result)

        val rObj: TicketResponseObject = restGet.ticket ?: fail("Ticked wasn't found")
        assertEquals(uObj.title, rObj.title)
        assertEquals(uObj.description, rObj.description)
        assertEquals(uObj.priority, rObj.priority)
    }
}