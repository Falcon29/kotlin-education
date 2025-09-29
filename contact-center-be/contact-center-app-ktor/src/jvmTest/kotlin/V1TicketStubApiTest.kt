package org.kotlined

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.kotlined.cc.api.v1.models.*
import org.kotlined.cc.app.ktor.CCAppSettings
import org.kotlined.cc.app.ktor.moduleJvm
import org.kotlined.common.CCCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V1TicketStubApiTest {

    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = TicketCreateRequest(
            ticket = TicketCreateObject(
                title = "Ticket",
                description = "with desc",
                priority = TicketPriority.MEDIUM
            )
        ),
    ) { response ->
        val responseObj = response.body<TicketCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Ticket", responseObj.ticket?.title)
        assertEquals("with desc", responseObj.ticket?.description)
        assertEquals(TicketPriority.MEDIUM, responseObj.ticket?.priority)
    }

    @Test
    fun get() = v1TestApplication(
        func = "get",
        request = TicketGetRequest(
            ticket = TicketGetObject(id = "12345")
        ),
    ) { response ->
        val responseObj = response.body<TicketGetResponse>()
        assertEquals(200, response.status.value)
        assertEquals("12345", responseObj.ticket?.id)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = TicketUpdateRequest(
            ticket = TicketUpdateObject(
                title = "Ticketus",
                description = "with descus",
                priority = TicketPriority.HIGH
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
    fun assign() = v1TestApplication(
        func = "assign",
        request = TicketAssignRequest(
            assignment = TicketAssignObject(ticketId = "12345", operatorId = "1002")
        ),
    ) { response ->
        val responseObj = response.body<TicketAssignResponse>()
        assertEquals(200, response.status.value)
        assertEquals("1002", responseObj.ticket?.operatorId)
    }

//    @Test
//    fun list() = v1TestApplication(
//        func = "list",
//        request = TicketListRequest(
//            ...
//        ),
//    ) { response ->
//        val responseObj = response.body<TicketListResponse>()
//        assertEquals(200, response.status.value)
//        assertEquals(..., responseObj.ticketList ...)
//    }

    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(CCAppSettings(corSettings = CCCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/ticket/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
