package org.kotlined.cc.test.e2e.be.scenarios.v2

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.kotlined.cc.api.v2.models.TicketDebug
import org.kotlined.cc.test.e2e.be.base.client.Client

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosV2(
    private val client: Client,
    private val debug: TicketDebug? = org.kotlined.cc.test.e2e.be.scenarios.v2.base.debug
) {
    @Nested
    internal inner class CreateV2: ScenarioCreateV2(client, debug)
    @Nested
    internal inner class UpdateV2: ScenarioUpdateV2(client, debug)
    @Nested
    internal inner class GetV2: ScenarioGetV2(client, debug)
}