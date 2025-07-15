import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class FlowTest {

    @Test
    fun `filter should return correct list`() = runBlocking {
        val flt = SearchFilter(
            title = "шнурки",
            type = AdType.DEMAND,
            visibilitiesOr = setOf(AdVisibility.OWNER, AdVisibility.GROUP),
            priceMin = BigDecimal("10.00"),
        )
        /*
        val res = LIST
            .asFlow()
            .run { flt.title?.let { title -> filter { it.title == title } } ?: this } // idea thinks "this" is never used here
            .run { flt.type?.let { type -> filter { it.type == type } } ?: this }
            .run { flt.visibilitiesOr?.let { vis -> filter { it.visibility in vis } } ?: this }
            .run { flt.priceMin?.let { min -> filter { it.price >= min } } ?: this }
            .run { flt.priceMax?.let { max -> filter { it.price <= max } } ?: this }
            .toList()

         */
        val res = LIST.asFlow()
            .filter { ad -> flt.title?.let { ad.title == it } ?: true }
            .filter { ad -> flt.type?.let { ad.type == it } ?: true }
            .filter { ad -> flt.visibilitiesOr?.let { ad.visibility in it } ?: true }
            .filter { ad -> flt.priceMin?.let { ad.price >= it } ?: true }
            .toList()
        assertEquals(1, res.size)
        assertEquals("5", res.first().id)
    }

    companion object {
        data class SearchFilter(
            val title: String? = null,
            val visibilitiesOr: Set<AdVisibility>? = null,
            val priceMin: BigDecimal? = null,
            val priceMax: BigDecimal? = null,
            val type: AdType? = null,
        )

        data class Ad(
            val id: String,
            val title: String,
            val visibility: AdVisibility,
            val price: BigDecimal,
            val type: AdType,
        )

        enum class AdVisibility { PUBLIC, GROUP, OWNER }
        enum class AdType { DEMAND, SUPPLY }

        val LIST = listOf(
            Ad("1", "носок", AdVisibility.PUBLIC, BigDecimal("22.13"), AdType.SUPPLY),
            Ad("2", "носок", AdVisibility.PUBLIC, BigDecimal("22.13"), AdType.DEMAND),
            Ad("3", "носок", AdVisibility.PUBLIC, BigDecimal("40.13"), AdType.DEMAND),
            Ad("4", "носок", AdVisibility.OWNER, BigDecimal("40.13"), AdType.DEMAND),
            Ad("5", "шнурки", AdVisibility.OWNER, BigDecimal("40.13"), AdType.DEMAND),
            Ad("6", "шнурки", AdVisibility.OWNER, BigDecimal("40.13"), AdType.SUPPLY),
            Ad("7", "шнурки", AdVisibility.GROUP, BigDecimal("9.99"), AdType.DEMAND),
        )
    }
}