import kotlinx.coroutines.delay

suspend fun findNumberInList(toFind: Int, numbers: List<Int>): Int {
    delay(2000)
    return numbers.firstOrNull { it == toFind } ?: -1
}