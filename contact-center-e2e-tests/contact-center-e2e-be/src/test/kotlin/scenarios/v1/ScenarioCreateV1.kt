package org.kotlined.cc.test.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.Test
import org.kotlined.cc.api.v1.models.ResponseResult
import org.kotlined.cc.api.v1.models.TicketCreateRequest
import org.kotlined.cc.api.v1.models.TicketCreateResponse
import org.kotlined.cc.api.v1.models.TicketDebug
import org.kotlined.cc.api.v1.models.TicketResponseObject
import org.kotlined.cc.test.e2e.be.base.client.Client
import org.kotlined.cc.test.e2e.be.scenarios.v1.base.createTicket
import org.kotlined.cc.test.e2e.be.scenarios.v1.base.sendAndReceive
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateV1(
    private val client: Client,
    private val debug: TicketDebug? = null
) {

    @Test
    fun create() = runBlocking {
        val obj = createTicket
        val restCreate = client.sendAndReceive(
            "ticket/create", TicketCreateRequest(
                requestType = "create",
                debug = debug,
                ticket = obj,
            )
        ) as TicketCreateResponse

        assertEquals(ResponseResult.SUCCESS, restCreate.result)

        val cObj: TicketResponseObject = restCreate.ticket ?: fail("No ad in Create response")
        assertEquals(obj.title, cObj.title)
        assertEquals(obj.description, cObj.description)
        assertEquals(obj.priority, cObj.priority)
    }
}