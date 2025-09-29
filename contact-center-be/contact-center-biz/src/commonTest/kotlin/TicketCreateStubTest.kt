package org.kotlined.cc.biz

import kotlinx.coroutines.test.runTest
import org.kotlined.cc.stubs.TicketStub
import org.kotlined.cc.stubs.TicketStubs
import org.kotlined.common.CCContext
import org.kotlined.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketCreateStubTest {
    private val processor = CCProcessor()
    val id = CCTicketId("ticketId0")
    val title = "Test Title"
    val description = "Ticket test desc"
    val priority = CCTicketPriority.MEDIUM
//    val permissions = mutableListOf(CCTicketPermission.CHANGE_PRIORITY)

    @Test
    fun createTicketTest() = runTest {
        val ctx = CCContext(
            command = CCCommand.CREATE,
            state = CCState.NONE,
            workMode = CCWorkMode.STUB,
            ticketRequest = CCTicket(
                id = id,
                title = title,
                description = description,
                priority = priority,
//                permissions = permissions
            )
        )
        processor.exec(ctx)
        assertEquals(TicketStubs.get().id, ctx.ticketResponse.id)
        assertEquals(title, ctx.ticketResponse.title)
        assertEquals(description, ctx.ticketResponse.description)
        assertEquals(priority, ctx.ticketResponse.priority)
//        assertEquals(permissions, ctx.ticketResponse.permissions)
    }

    @Test
    fun createWithBadTitleTest() = runTest {
        val ctx = CCContext(
            command = CCCommand.CREATE,
            state = CCState.RUNNING,
            workMode = CCWorkMode.STUB,
            stubCase = CCStubs.BAD_TITLE,
            ticketRequest = CCTicket(
                id = id,
                title = "",
                description = description,
                priority = priority,
//                permissions = permissions
            )
        )
        processor.exec(ctx)
        assertEquals(CCTicket(), ctx.ticketResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}