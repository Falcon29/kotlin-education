package org.kotlined.cc.biz

import kotlinx.coroutines.test.runTest
import org.kotlined.cc.biz.validation.validateTitleHasContent
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCTicket
import org.kotlined.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketValidationTest {

    @Test
    fun emptyString() = runTest {
        val ctx = CCContext(state = CCState.RUNNING, ticketValidating = CCTicket(title = ""))
        chain.exec(ctx)
        assertEquals(CCState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = CCContext(state = CCState.RUNNING, ticketValidating = CCTicket(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(CCState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = CCContext(state = CCState.RUNNING, ticketValidating = CCTicket(title = "title"))
        chain.exec(ctx)
        assertEquals(CCState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}