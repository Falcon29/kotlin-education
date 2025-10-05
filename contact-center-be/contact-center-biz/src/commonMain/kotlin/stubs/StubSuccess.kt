package org.kotlined.cc.biz.stubs

import org.kotlined.cc.logging.common.LogLevel
import org.kotlined.cc.stubs.TicketStubs
import org.kotlined.common.CCContext
import org.kotlined.common.CCCorSettings
import org.kotlined.common.models.CCState
import org.kotlined.common.models.CCStubs
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketOperatorId
import org.kotlined.common.models.CCTicketPriority
import org.kotlined.cor.ICorChainDsl
import org.kotlined.cor.worker

fun ICorChainDsl<CCContext>.stubCreateSuccess(title: String, corSettings: CCCorSettings) = worker {
    this.title = title
    this.description = """
        Успешное создание тикета
    """.trimIndent()
    on { stubCase == CCStubs.SUCCESS && state == CCState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CCState.FINISHING
            val stub = TicketStubs.prepareResult {
                ticketRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                ticketRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                ticketRequest.priority.takeIf { it != CCTicketPriority.NONE }?.also { this.priority = it }
            }
            ticketResponse = stub
        }
    }
}

fun ICorChainDsl<CCContext>.stubUpdateSuccess(title: String, corSettings: CCCorSettings) = worker {
    this.title = title
    this.description = """
        Успешное обновление тикета
    """.trimIndent()
    on { stubCase == CCStubs.SUCCESS && state == CCState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CCState.FINISHING
            val stub = TicketStubs.prepareResult {
                ticketRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                ticketRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                ticketRequest.priority.takeIf { it != CCTicketPriority.NONE }?.also { this.priority = it }
            }
            ticketResponse = stub
        }
    }
}

fun ICorChainDsl<CCContext>.stubAssignSuccess(title: String, corSettings: CCCorSettings) = worker {
    this.title = title
    this.description = """
        Успешное назначение тикета на оператора
    """.trimIndent()
    on { stubCase == CCStubs.SUCCESS && state == CCState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubAssignSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CCState.FINISHING
            val stub = TicketStubs.prepareResult {
//                this.operatorId = ticketRequest.operatorId
                ticketRequest.operatorId.takeIf { it != CCTicketOperatorId.NONE }?.also { this.operatorId = it }
            }
            ticketResponse = stub
        }
    }
}

fun ICorChainDsl<CCContext>.stubGetSuccess(title: String, corSettings: CCCorSettings) = worker {
    this.title = title
    this.description = """
        Успешное получение/чтение тикета
    """.trimIndent()
    on { stubCase == CCStubs.SUCCESS && state == CCState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubGetSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CCState.FINISHING
            val stub = TicketStubs.prepareResult {
                ticketRequest.id.takeIf { it != this.id || it == CCTicketId.NONE }?.also { this.id = it }
//                ticketRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            ticketResponse = stub
        }
    }
}