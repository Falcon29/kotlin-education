package org.kotlined.cc.app.common

import kotlinx.coroutines.test.runTest
import org.kotlined.cc.api.v2.mappers.fromTransport
import org.kotlined.cc.api.v2.mappers.toTransportTicket
import org.kotlined.cc.api.v2.models.IRequest
import org.kotlined.cc.api.v2.models.IResponse
import org.kotlined.cc.api.v2.models.ResponseResult
import org.kotlined.cc.api.v2.models.TicketCreateObject
import org.kotlined.cc.api.v2.models.TicketCreateRequest
import org.kotlined.cc.api.v2.models.TicketCreateResponse
import org.kotlined.cc.api.v2.models.TicketPriority
import org.kotlined.cc.biz.CCProcessor
import org.kotlined.common.CCCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {

    private val request = TicketCreateRequest(
        ticket = TicketCreateObject(
            title = "ticket title",
            description = "description",
            priority = TicketPriority.LOW
        )
    )

    private val appSettings: ICCAppSettings = object : ICCAppSettings {
        override val corSettings: CCCorSettings = CCCorSettings()
        override val processor: CCProcessor = CCProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createTicket(appSettings: ICCAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TicketCreateRequest>()) },
            { toTransportTicket() }
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createTicket(appSettings) }
        val res = testApp.res as TicketCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}