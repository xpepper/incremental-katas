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
            twixies (1 whole box, 3 rows, 5 per row), 1, 20
            cheese (gouda, 1Kg), 1, 5
            bacon ("tasty" brand, 3 pack), 2, 7
            apples (red, 1Kg bag), 1, 2
            """
        )

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe (1 * 20) + (1 * 5) + (2 * 7) + (1 * 2)
    }

    @Test
    fun `list all the products in the ROS file`() {
        val rosFile = writeRosFileWith(
            """
            twixies (1 whole box, 3 rows, 5 per row), 1, 20
            cheese (gouda, 1Kg), 1, 5
            bacon ("tasty" brand, 3 pack), 2, 7
            apples (red, 1Kg bag), 1, 2
            """
        )

        RecordOfSales(rosFile.absolutePath).allProducts() shouldBe listOf(
            "twixies (1 whole box, 3 rows, 5 per row)",
            "cheese (gouda, 1Kg)",
            "bacon (\"tasty\" brand, 3 pack)",
            "apples (red, 1Kg bag)"
        )
    }

    private fun writeRosFileWith(fileContent: String) = File.createTempFile("ros", ".txt").apply {
        writeText(fileContent.trimIndent())
    }.also { it.deleteOnExit() }
}

