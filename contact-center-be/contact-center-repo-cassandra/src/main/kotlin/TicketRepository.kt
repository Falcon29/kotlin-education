package org.kotlined

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import org.kotlined.cc.repo.common.ITicketRepoInit
import org.kotlined.common.models.CCTicket
import org.kotlined.common.models.CCTicketId
import org.kotlined.common.models.CCTicketLock
import org.kotlined.common.repository.*
import org.kotlined.model.TicketCassandraDTO
import org.kotlined.model.TicketCassandraDTO.Companion.COLUMN_LOCK
import org.kotlined.model.TicketPriority
import org.kotlined.model.TicketStatus
import java.net.InetAddress
import java.net.InetSocketAddress

class TicketRepository(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val dc: String = "dc1",
    private val randomUuid: () -> String = { uuid4().toString() },
) : TicketRepoBase(), IRepoTicket, ITicketRepoInit {

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(TicketStatus::class.java))
            register(EnumNameCodec(TicketPriority::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter(dc)
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .withKeyspace(keyspaceName)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private val dao by lazy {
        mapper.ticketDao(keyspaceName, TicketCassandraDTO.TABLE_NAME)
    }

    fun clear() = dao.deleteAll()

    override suspend fun createTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val new = rq.ticket.copy(id = CCTicketId(randomUuid()), lock = CCTicketLock(randomUuid()))
        dao.create(TicketCassandraDTO(new)).await()
        DBTicketResponseOk(new)
    }

    override suspend fun getTicket(rq: DBTicketIdRequest): IDBTicketResponse = tryTicketMethod {
        if (rq.id == CCTicketId.NONE) return@tryTicketMethod errorEmptyId
        val res = dao.get(rq.id.asString()).await() ?: return@tryTicketMethod errorNotFound(rq.id)
        DBTicketResponseOk(res.toTicketModel())
    }

    override suspend fun updateTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val id = rq.ticket.id.asString()
        val prevLock = rq.ticket.lock.asString()
        val new = rq.ticket.copy(lock = CCTicketLock(randomUuid()))
        val dto = TicketCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(COLUMN_LOCK) }
            ?.getString(COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DBTicketResponseOk(new)
            resultField == null -> errorNotFound(rq.ticket.id)
            else -> errorRepoConcurrency(
                oldTicket = dao.get(id).await()?.toTicketModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $id and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.ticket.lock
            )
        }
    }

    override suspend fun assignTicket(rq: DBTicketRequest): IDBTicketResponse = tryTicketMethod {
        val id = rq.ticket.id.asString()
        val prevLock = rq.ticket.lock.asString()
        val new = rq.ticket.copy(lock = CCTicketLock(randomUuid()))
        val dto = TicketCassandraDTO(new)

        val res: AsyncResultSet = dao.assign(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(COLUMN_LOCK) }
            ?.getString(COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DBTicketResponseOk(new)
            resultField == null -> errorNotFound(rq.ticket.id)
            else -> errorRepoConcurrency(
                oldTicket = dao.get(id).await()?.toTicketModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $id and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.ticket.lock
            )
        }
    }

    override fun save(tickets: Collection<CCTicket>): Collection<CCTicket> = runBlocking {
        tickets.onEach {
            dao.create(
                TicketCassandraDTO(it)
            ).await()
        }
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}