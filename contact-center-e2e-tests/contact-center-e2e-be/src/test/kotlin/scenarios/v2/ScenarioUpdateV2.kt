package org.kotlined.cc.test.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.Test
import org.kotlined.cc.api.v2.models.*
import org.kotlined.cc.test.e2e.be.base.client.Client
import org.kotlined.cc.test.e2e.be.scenarios.v2.base.createTicket
import org.kotlined.cc.test.e2e.be.scenarios.v2.base.sendAndReceive
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV2(
    private val client: Client,
    private val debug: TicketDebug? = org.kotlined.cc.test.e2e.be.scenarios.v2.base.debug,
) {

    @Test
    fun update() = runBlocking {
        val obj = createTicket
        val restCreate = client.sendAndReceive(
            path = "ticket/create",
            TicketCreateRequest(
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