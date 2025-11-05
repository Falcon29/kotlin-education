package org.kotlined.cc.app.ktor

import io.ktor.server.application.*
import org.kotlined.TicketRepoStub
import org.kotlined.cc.app.ktor.base.KtorWsSessionRepo
import org.kotlined.cc.biz.CCProcessor
import org.kotlined.common.CCCorSettings

fun Application.initAppSettings(): CCAppSettings {
    val corSettings = CCCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSession = KtorWsSessionRepo(),
        repoTest = getDatabaseConf(TicketDbType.TEST),
        repoProd = getDatabaseConf(TicketDbType.PROD),
        repoStub = TicketRepoStub(),
    )
    return CCAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = CCProcessor(corSettings),
    )
}