package org.kotlined.cc.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kotlined.cc.repo.common.ITicketRepoInit
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.repository.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TicketRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : TicketRepoBase(), IRepoTicket, ITicketRepoInit {
    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, TicketEntity>()
        .expireAfterWrite(ttl)
        .build()

    override suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val key = randomUuid()
        val ticket = rq.ticket.copy(id = CCTicketId(key), lock = CCTicketLock(randomUuid()))
        val entity = TicketEntity(ticket)
        mutex.withLock {
            cache.put(key, entity)
        }
        DBTicketResponseOk(ticket)
    }

    override suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse = tryTicketMethod {
        val key = rq.id.takeIf { it != CCTicketId.NONE }?.asString() ?: return@tryTicketMethod errorEmptyId
        mutex.withLock {
            cache.get(key)?.let { DBTicketResponseOk(it.toInternal()) } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val rqTicket = rq.ticket
        val id = rqTicket.id.takeIf { it != CCTicketId.NONE } ?: return@tryTicketMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqTicket.lock.takeIf { it != CCTicketLock.NONE } ?: return@tryTicketMethod errorEmptyLock(id)

        mutex.withLock {
            val oldTicket = cache.get(key)?.toInternal()
            when {
                oldTicket == null -> errorNotFound(id)
                oldTicket.lock == CCTicketLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldTicket.lock != oldLock -> errorRepoConcurrency(oldTicket, oldLock)
                else -> {
                    val newTicket = rqTicket.copy(lock = CCTicketLock(randomUuid()))
                    val entity = TicketEntity(newTicket)
                    cache.put(key, entity)
                    DBTicketResponseOk(newTicket)
                }
            }
        }
    }

    override suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val rqTicket = rq.ticket
        val id = rqTicket.id.takeIf { it != CCTicketId.NONE } ?: return@tryTicketMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqTicket.lock.takeIf { it != CCTicketLock.NONE } ?: return@tryTicketMethod errorEmptyLock(id)

        mutex.withLock {
            val oldTicket = cache.get(key)?.toInternal()
            when {
                oldTicket == null -> errorNotFound(id)
                oldTicket.lock == CCTicketLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldTicket.lock != oldLock -> errorRepoConcurrency(oldTicket, oldLock)
                else -> {
                    val newTicket = rqTicket.copy(
                        operatorId = rqTicket.operatorId,
                        priority = rqTicket.priority,
                        lock = CCTicketLock(randomUuid())
                    )
                    val entity = TicketEntity(newTicket)
                    cache.put(key, entity)
                    DBTicketResponseOk(newTicket)
                }
            }
        }
    }

    override fun save(tickets: Collection<CCTicket>) = tickets.map { ticket ->
        val entity = TicketEntity(ticket)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ticket
    }
}