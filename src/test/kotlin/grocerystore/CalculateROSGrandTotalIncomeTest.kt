package grocerystore

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class CalculateROSGrandTotalIncomeTest {
    @Test
    fun `gti is zero if the ros file is empty`() {
        val rosFile = File.createTempFile("ros", ".txt").apply {
            writeText("".trimIndent())
        }.also { it.deleteOnExit() }

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe 0
    }

    @Test
    fun `read a simple ROS file with multiple entries`() {
        val rosFile = File.createTempFile("ros", ".txt").apply {
            writeText("""
                bread, 1, 2
                12-pack of eggs, 1, 2
                milk (1L), 4, 8
                coca cola (33cl), 10, 10
                """.trimIndent())
        }.also { it.deleteOnExit() }

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe (1 * 2) + (1 * 2) + (4 * 8) + (10 * 10)
    }
}

