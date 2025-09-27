package org.kotlined.cc.mappers.v1

import org.junit.Test
import org.kotlined.cc.api.v1.models.*
import org.kotlined.common.CCContext
import org.kotlined.common.models.*
import kotlin.test.assertEquals

class MapperFromTransportTest {
    @Test
    fun fromTransportCreateRequest() {
        val req = TicketCreateRequest(
            ticket = CCTicketStub.get().toTransportCreateTicket()
        )

        val expected = CCTicketStub.prepareResult {
            id = CCTicketId.NONE
            clientId = CCTicketClientId.NONE
            operatorId = CCTicketOperatorId.NONE
            permissions.clear()
        }

        val context = CCContext()
        context.fromTransport(req)

        assertEquals(CCCommand.CREATE, context.command)
//        assertEquals(CCWorkMode.STUB, context.workMode)
        assertEquals(expected, context.ticketRequest)
    }

    @Test
    fun fromTransportGetRequest() {
        val req = TicketGetRequest(
            ticket = TicketGetObject(id = "123")
        )

        val context = CCContext()
        context.fromTransport(req)

        assertEquals(CCCommand.GET, context.command)
//        assertEquals(CCWorkMode.PROD, context.workMode)
        assertEquals("123", context.ticketRequest.id.asString())
    }

    @Test
    fun fromTransportUpdateRequest() {
        val req = TicketUpdateRequest(
            ticket = CCTicketStub.get().toTransportUpdateTicket()
        )

        val context = CCContext()
        context.fromTransport(req)

        assertEquals(CCCommand.UPDATE, context.command)
//        assertEquals(CCWorkMode.TEST, context.workMode)
        assertEquals(CCTicketStub.ID, context.ticketRequest.id.asString())
    }

}

class MapperToTransportTest {
    @Test
    fun toTransportCreateResponse() {
        val context = CCContext(
            requestId = CCRequestId("1234"),
            command = CCCommand.CREATE,
            ticketResponse = CCTicketStub.get(),
            errors = mutableListOf(
                CCError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = CCTicketStatus.NEW,
        )

        val res = context.toTransportTicket() as TicketCreateResponse

        assertEquals(CCTicketStub.get().toTransportTicket(), res.ticket)
        assertEquals(1, res.errors?.size)
        assertEquals("err", res.errors?.firstOrNull()?.code)
        assertEquals("request", res.errors?.firstOrNull()?.group)
        assertEquals("title", res.errors?.firstOrNull()?.field)
        assertEquals("wrong title", res.errors?.firstOrNull()?.message)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun toTransportGetResponse() {
        val context = CCContext(
            command = CCCommand.GET,
            ticketResponse = CCTicketStub.get()
        )

        val res = context.toTransportTicket() as TicketGetResponse

        assertEquals(CCTicketStub.get().toTransportTicket(), res.ticket)
    }

    @Test
    fun toTransportUpdateResponse() {
        val context = CCContext(
            command = CCCommand.UPDATE,
            ticketResponse = CCTicketStub.get()
        )

        val res = context.toTransportTicket() as TicketUpdateResponse

        assertEquals(CCTicketStub.get().toTransportTicket(), res.ticket)
    }

    @Test
    fun toTransportSearchResponse() {
        val context = CCContext(
            command = CCCommand.LIST,
            ticketListResponse = mutableListOf(CCTicketStub.get(), CCTicketStub.get())
        )

        val res = context.toTransportTicket() as TicketListResponse

        assertEquals(2, res.tickets?.size)
        assertEquals(CCTicketStub.get().toTransportTicket(), res.tickets?.firstOrNull())
    }
}

object CCTicketStub {
    const val ID = "123"
    const val TITLE = "Test Ticket"
    const val DESCRIPTION = "Test Description"

    fun get() = CCTicket(
        id = CCTicketId(ID),
        title = TITLE,
        description = DESCRIPTION,
        status = CCTicketStatus.NEW,
        clientId = CCTicketClientId("client-123"),
        operatorId = CCTicketOperatorId("operator-456"),
        priority = CCTicketPriority.HIGH,
        permissions = mutableListOf(
            CCTicketPermission.READ,
            CCTicketPermission.UPDATE
        )
    )

    inline fun prepareResult(block: CCTicket.() -> Unit): CCTicket {
        return get().apply(block)
    }
}