package org.kotlined.cc.test.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.Test
import org.kotlined.cc.api.v2.models.*
import org.kotlined.cc.test.e2e.be.base.client.Client
import org.kotlined.cc.test.e2e.be.scenarios.v2.base.createTicket
import org.kotlined.cc.test.e2e.be.scenarios.v2.base.sendAndReceive
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateV2(
    private val client: Client,
    private val debug: TicketDebug? = org.kotlined.cc.test.e2e.be.scenarios.v2.base.debug
) {

    @Test
    fun create() = runBlocking {
        val obj = createTicket
        val restCreate = client.sendAndReceive(
            "ticket/create", TicketCreateRequest(
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