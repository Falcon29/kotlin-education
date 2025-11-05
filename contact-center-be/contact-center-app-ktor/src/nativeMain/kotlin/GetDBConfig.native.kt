package org.kotlined.cc.app.ktor

import io.ktor.server.application.Application
import org.kotlined.common.repository.IRepoTicket

actual fun Application.getDatabaseConf(type: TicketDbType): IRepoTicket {
    TODO("Not yet implemented")
}