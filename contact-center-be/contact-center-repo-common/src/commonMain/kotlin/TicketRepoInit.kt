package org.kotlined.cc.repo.common

import org.kotlined.common.models.CCTicket

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class TicketRepoInit(
    val repo: ITicketRepoInit,
    initObjects: Collection<CCTicket> = emptyList(),
) : ITicketRepoInit by repo {
    @Suppress("unused")
    val initializedObjects: List<CCTicket> = save(initObjects).toList()
}