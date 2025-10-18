package org.kotlined.cc.repo.common

import org.kotlined.common.models.CCTicket
import org.kotlined.common.repository.IRepoTicket

interface ITicketRepoInit: IRepoTicket {
    fun save(tickets: Collection<CCTicket>): Collection<CCTicket>
}