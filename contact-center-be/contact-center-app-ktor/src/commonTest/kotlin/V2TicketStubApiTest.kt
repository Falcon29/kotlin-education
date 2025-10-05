package org.kotlined.cc.app.ktor

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import org.kotlined.cc.api.v2.apiV2Mapper
import org.kotlined.cc.api.v2.models.*
import org.kotlined.common.CCCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V2TicketStubApiTest {

    @Test
    fun create() = v2TestApplication<IRequest>(
        func = "create",
        request = TicketCreateRequest(
            ticket = TicketCreateObject(
                title = "Ticket",
                description = "with desc",
                priority = TicketPriority.MEDIUM,
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )
    ) { response ->
        val responseObj = response.body<TicketCreateResponse>()
        println(responseObj.toString())
        assertEquals(200, response.status.value)
        assertEquals("Ticket", responseObj.ticket?.title)
        assertEquals("with desc", responseObj.ticket?.description)
        assertEquals(TicketPriority.MEDIUM, responseObj.ticket?.priority)
    }

    @Test
    fun get() = v2TestApplication<IRequest>(
        func = "get",
        request = TicketGetRequest(
            ticket = TicketGetObject(id = "12345"),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketGetResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.ticket?.id)
    }

    @Test
    fun update() = v2TestApplication<IRequest>(
        func = "update",
        request = TicketUpdateRequest(
            ticket = TicketUpdateObject(
                title = "Ticketus",
                description = "with descus",
                priority = TicketPriority.HIGH
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(200, response.status.value)
        assertEquals("Ticketus", responseObj.ticket?.title)
        assertEquals("with descus", responseObj.ticket?.description)
        assertEquals(TicketPriority.HIGH, responseObj.ticket?.priority)
    }

    @Test
    fun assign() = v2TestApplication<IRequest>(
        func = "assign",
        request = TicketAssignRequest(
            assignment = TicketAssignObject(ticketId = "12345", operatorId = "1002"),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketAssignResponse>()
        assertEquals(200, response.status.value)
        assertEquals("1002", responseObj.ticket?.operatorId)
    }

//    @Test
//    fun list() = v2TestApplication(
//        func = "list",
//        request = TicketListRequest(
//            ...
//        ),
//    ) { response ->
//        val responseObj = response.body<TicketListResponse>()
//        assertEquals(200, response.status.value)
//        assertEquals(..., responseObj.ticketList ...)
//    }

    private inline fun <reified T : IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(CCAppSettings(corSettings = CCCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/ticket/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
