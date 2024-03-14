package grocerystore

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class CalculateROSGrandTotalIncomeTest {

    @Test
    fun `read a simple ROS file with just one entry`() {
        val rosFile = File.createTempFile("ros", ".txt").apply {
            writeText("""
                bread, 1, 2
                """.trimIndent())
        }

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe 1 * 2
    }
}

