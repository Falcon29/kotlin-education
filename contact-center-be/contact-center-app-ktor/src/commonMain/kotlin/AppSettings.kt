package org.kotlined.cc.app.ktor

import io.ktor.server.application.Application
import org.kotlined.cc.app.ktor.base.KtorWsSessionRepo
import org.kotlined.cc.biz.CCProcessor
import org.kotlined.cc.logging.common.CCLoggerProvider
import org.kotlined.common.CCCorSettings

fun Application.initAppSettings(): CCAppSettings {
    val corSettings = CCCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSession = KtorWsSessionRepo(),
    )
    return CCAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = CCProcessor(corSettings),
    )
}