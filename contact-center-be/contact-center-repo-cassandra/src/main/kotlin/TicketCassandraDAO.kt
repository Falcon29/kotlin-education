package org.kotlined

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.Query
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes
import com.datastax.oss.driver.api.mapper.annotations.Update
import org.kotlined.model.TicketCassandraDTO
import org.kotlined.model.TicketCassandraDTO.Companion.COLUMN_LOCK
import java.util.concurrent.CompletionStage

@Dao
interface TicketCassandraDAO {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: TicketCassandraDTO): CompletionStage<TicketCassandraDTO>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun get(id: String): CompletionStage<TicketCassandraDTO>

    @Update(customIfClause = "$COLUMN_LOCK = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: TicketCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Update(customIfClause = "$COLUMN_LOCK = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun assign(dto: TicketCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Query("TRUNCATE ${TicketCassandraDTO.TABLE_NAME}")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun deleteAll()
}

/*
class AdCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<AdCassandraDTO>
) {
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.titleFilter.isNotBlank()) {
            // Внимание! При использовании LIKE необходимо использовать SASI индексы.
            // При использовании SASI индекса типа StandardAnalyzer происходит токенизация текста по пробелам.
            // Оператор LIKE в этом случае должен быть НЕ LIKE '%<токен>%' а LIKE '<токен>%'
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_TITLE)
                .like(QueryBuilder.literal("${filter.titleFilter}%"))
        }
        if (filter.ownerId != MkplUserId.NONE) {
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.dealSide != MkplDealSide.NONE) {
            select = select
                .whereColumn(AdCassandraDTO.COLUMN_AD_TYPE)
                .isEqualTo(QueryBuilder.literal(filter.dealSide.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<AdCassandraDTO>()
        private val future = CompletableFuture<Collection<AdCassandraDTO>>()
        val stage: CompletionStage<Collection<AdCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}
 */