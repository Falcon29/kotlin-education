import org.junit.jupiter.api.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

@Ignore
class MapToNameListTest {

    /*
    * Реализовать функцию, которая преобразует список словарей строк в ФИО
    * Функцию сделать с использованием разных функций для разного числа составляющих имени
    * Итого, должно получиться 4 функции
    * Для успешного решения задания, требуется раскомментировать тест, тест должен выполняться успешно
    */
    @Test
    fun `list should contain full names from map`() {
        val input = listOf(
            mapOf(
                "first" to "Иван",
                "middle" to "Васильевич",
                "last" to "Рюрикович",
            ),
            mapOf(
                "first" to "Петька",
            ),
            mapOf(
                "first" to "Сергей",
                "last" to "Королев",
            ),
        )
        val expected = listOf(
            "Рюрикович Иван Васильевич",
            "Петька",
            "Королев Сергей",
        )
        val res = mapToNameList(input)
        assertEquals(expected, res)
    }
}
