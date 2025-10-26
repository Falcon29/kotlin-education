package org.kotlined.cc.inmemory

import org.kotlined.cc.repo.common.TicketRepoInit
import org.kotlined.cc.repo.tests.RepoTicketCreateTest
import org.kotlined.cc.repo.tests.RepoTicketGetTest
import org.kotlined.cc.repo.tests.RepoTicketUpdateTest

class TicketRepoInMemoryCreateTest : RepoTicketCreateTest() {
    override val repo = TicketRepoInit(
        TicketRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class TicketRepoInMemoryGetTicketTest : RepoTicketGetTest() {
    override val repo = TicketRepoInit(
        TicketRepoInMemory(),
        initObjects = initObjects,
    )
}

class TicketRepoInMemoryUpdateTest : RepoTicketUpdateTest() {
    override val repo = TicketRepoInit(
        TicketRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}