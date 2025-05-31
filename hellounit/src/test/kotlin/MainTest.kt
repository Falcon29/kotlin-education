import org.edu.sayHello
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MainTest {

    @Test
    fun `sayHello returns correct output`() {
        assertEquals("Hello, Kotlin!", sayHello("Kotlin"))
    }

}