package org.kotlined.cc.test.e2e.be.scenarios.v1

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.kotlined.cc.api.v1.models.TicketDebug
import org.kotlined.cc.test.e2e.be.base.client.Client

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosV1(
    private val client: Client,
    private val debug: TicketDebug? = null
) {
    @Nested
    internal inner class CreateV1: ScenarioCreateV1(client, debug)
    @Nested
    internal inner class UpdateV1: ScenarioUpdateV1(client, debug)
    @Nested
    internal inner class GetV1: ScenarioGetV1(client, debug)
}