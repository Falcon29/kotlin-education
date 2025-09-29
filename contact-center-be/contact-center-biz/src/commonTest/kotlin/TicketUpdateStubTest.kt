package org.kotlined.cc.biz

import kotlinx.coroutines.test.runTest
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCStubs
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.common.models.CCWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketUpdateStubTest {
    private val processor = CCProcessor()
    val id = CCTicketId("ticketId0")
    val title = "Test Title"
    val description = "Ticket test desc"
    val priority = CCTicketPriority.MEDIUM

    @Test
    fun updateTicketTest() = runTest {
        val ctx = CCContext(
            command = CCCommand.UPDATE,
            state = CCState.NONE,
            workMode = CCWorkMode.STUB,
            stubCase = CCStubs.SUCCESS,
            ticketRequest = CCTicket(
                title = title,
                description = description,
                priority = priority,
            )
        )
        processor.exec(ctx)
        assertEquals(title, ctx.ticketResponse.title)
        assertEquals(description, ctx.ticketResponse.description)
        assertEquals(priority, ctx.ticketResponse.priority)
    }

    @Test
    fun updateWithBadTitleTest() = runTest {
        val ctx = CCContext(
            command = CCCommand.UPDATE,
            state = CCState.RUNNING,
            workMode = CCWorkMode.STUB,
            stubCase = CCStubs.BAD_TITLE,
            ticketRequest = CCTicket(
                id = id,
                title = "",
                description = description,
                priority = priority
            )
        )
        processor.exec(ctx)
        assertEquals(CCTicket(), ctx.ticketResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun updateWithBadDescriptionTest() = runTest {
        val ctx = CCContext(
            command = CCCommand.UPDATE,
            state = CCState.RUNNING,
            workMode = CCWorkMode.STUB,
            stubCase = CCStubs.BAD_DESCRIPTION,
            ticketRequest = CCTicket(
                id = id,
                title = title,
                description = "",
                priority = priority
            )
        )
        processor.exec(ctx)
        assertEquals(CCTicket(), ctx.ticketResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}