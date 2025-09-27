package org.kotlined.cc.app.common

import kotlinx.datetime.Clock
import org.kotlined.common.CCContext
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCTicketStatus

suspend inline fun <T> ICCAppSettings.controllerHelper(
    crossinline getRequest: suspend CCContext.() -> Unit,
    crossinline toResponse: suspend CCContext.() -> T,
): T {
    val ctx = CCContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = CCTicketStatus.CLOSED
        processor.exec(ctx)
        if (ctx.command == CCCommand.NONE) {
            ctx.command = CCCommand.GET
        }
        ctx.toResponse()
    }
}