package org.kotlined.cc.app.ktor

import io.ktor.server.application.Application
import org.kotlined.cc.logging.common.CCLoggerProvider

expect fun Application.getLoggerProviderConf(): CCLoggerProvider