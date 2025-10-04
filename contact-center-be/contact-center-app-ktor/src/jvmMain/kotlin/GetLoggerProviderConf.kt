package org.kotlined.cc.app.ktor

import io.ktor.server.application.Application
import org.kotlined.cc.logging.common.CCLoggerProvider
import org.kotlined.cc.logging.jvm.ccLoggerLogback

actual fun Application.getLoggerProviderConf(): CCLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> CCLoggerProvider { ccLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
    }