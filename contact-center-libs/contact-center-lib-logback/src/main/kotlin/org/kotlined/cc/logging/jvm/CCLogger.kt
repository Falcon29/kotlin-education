package org.kotlined.cc.logging.jvm

import ch.qos.logback.classic.Logger
import org.kotlined.cc.logging.common.ICCLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun ccLoggerLogback(logger: Logger): ICCLogWrapper = CCLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun ccLoggerLogback(clazz: KClass<*>): ICCLogWrapper =
    ccLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun ccLoggerLogback(loggerId: String): ICCLogWrapper =
    ccLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
