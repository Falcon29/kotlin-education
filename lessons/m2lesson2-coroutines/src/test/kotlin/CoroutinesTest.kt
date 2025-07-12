import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

@Ignore
class CoroutinesTest {

    @Test
    fun easyHw() = runBlocking {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000

        val result10 = async {findNumberInList(toFind, numbers)}
        val result1000 = async {findNumberInList(toFindOther, numbers)}

        val foundNumbers = listOf(result10.await(), result1000.await())

        foundNumbers.forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }
    /*
    @Additional explanations by DeepSeek (for myself)@
    runBlocking: Creates a coroutine scope where we can launch other coroutines
    async: Starts a coroutine that returns a Deferred (a future result)
    Both searches start immediately and run in parallel
    await(): Waits for the result without blocking the thread
    Output: Same as before, but now the searches happened concurrently
    delay and suspend: method findNumber...() now suspend and have delay(), coroutine version of Thread.sleep - it suspends instead of blocking
     */
}