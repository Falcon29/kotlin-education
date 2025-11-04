package org.kotlined.cc.app.common

import kotlinx.datetime.Clock
import org.kotlined.cc.api.log.mapper.toLog
import org.kotlined.common.CCContext
import org.kotlined.common.helpers.asCCError
import org.kotlined.common.models.CCCommand
import org.kotlined.common.models.CCState
import kotlin.reflect.KClass

suspend inline fun <T> ICCAppSettings.controllerHelper(
    crossinline getRequest: suspend CCContext.() -> Unit,
    crossinline toResponse: suspend CCContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = CCContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}:: ${ctx.toLog(logId)}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = CCState.FAILING
        ctx.errors.add(e.asCCError())
        processor.exec(ctx)
        if (ctx.command == CCCommand.NONE) {
            ctx.command = CCCommand.GET
        }
        ctx.toResponse()
    }
}