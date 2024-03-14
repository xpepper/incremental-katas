package grocerystore

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class CalculateROSGrandTotalIncomeTest {

    @Test
    fun `gti is zero if the ros file is empty`() {
        val emptyRosFile = writeRosFileWith("")

        val gti = RecordOfSales(emptyRosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe 0
    }


    @Test
    fun `compute gti for a single ROS file with multiple entries`() {
        val rosFile = writeRosFileWith(
            """
            bread, 1, 2
            12-pack of eggs, 1, 2
            milk (1L), 4, 8
            coca cola (33cl), 10, 10
            """
        )

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe (1 * 2) + (1 * 2) + (4 * 8) + (10 * 10)
    }

    @Test
    fun `parse ROS file with commas in the product name`() {
        val rosFile = writeRosFileWith(
            """
            tomatoes, 12, 3
            bananas, 3, 1 
            """
        )

        RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()
    }

    private fun writeRosFileWith(fileContent: String) = File.createTempFile("ros", ".txt").apply {
        writeText(fileContent.trimIndent())
    }.also { it.deleteOnExit() }
}

