import org.junit.jupiter.api.Test
import org.kotlined.sayHello
import kotlin.test.Ignore
import kotlin.test.assertEquals

@Ignore
class MainTest {

    @Test
    fun `sayHello returns correct output`() {
        assertEquals("Hello, Kotlin!", sayHello("Kotlin"))
    }

}