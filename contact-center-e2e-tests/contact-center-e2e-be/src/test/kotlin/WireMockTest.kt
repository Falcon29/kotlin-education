package org.kotlined.cc.test.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.kotlined.cc.api.v1.models.TicketDebug as TicketDebugV1
import org.kotlined.cc.api.v2.models.TicketDebug as TicketDebugV2
import org.kotlined.cc.api.v1.models.TicketRequestDebugMode as TicketDebugRequestV1
import org.kotlined.cc.api.v2.models.TicketRequestDebugMode as TicketDebugRequestV2
import org.kotlined.cc.test.e2e.be.base.BaseContainerTest
import org.kotlined.cc.test.e2e.be.base.client.Client
import org.kotlined.cc.test.e2e.be.base.client.RestClient
import org.kotlined.cc.test.e2e.be.docker.WiremockDockerCompose
import org.kotlined.cc.test.e2e.be.scenarios.v1.ScenariosV1
import org.kotlined.cc.test.e2e.be.scenarios.v2.ScenariosV2

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WireMockTest : BaseContainerTest(WiremockDockerCompose) {
    private val client: Client = RestClient(compose)

    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1 : ScenariosV1(client, TicketDebugV1(mode = TicketDebugRequestV1.PROD))

    @Nested
    internal inner class V2 : ScenariosV2(client, TicketDebugV2(mode = TicketDebugRequestV2.PROD))

}
