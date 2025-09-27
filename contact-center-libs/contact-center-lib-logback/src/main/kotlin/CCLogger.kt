package org.kotlined

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun mpLoggerLogback(logger: Logger): ICCLogWrapper = CCLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mpLoggerLogback(clazz: KClass<*>): ICCLogWrapper =
    mpLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun mpLoggerLogback(loggerId: String): ICCLogWrapper =
    mpLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
