package grocerystore

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

class CalculateROSGrandTotalIncomeTest {

    @Test
    fun `read a simple ROS file with just one entry`() {
        val rosFile = File.createTempFile("ros", ".txt").apply {
            writeText("bread, 1, 2")
        }

        val gti = RecordOfSales(rosFile.absolutePath).computeGrandTotalIncome()

        gti shouldBe 1 * 2
    }
}

class RecordOfSales(private val rosFilePath: String) {
    fun computeGrandTotalIncome(): Long {
        var gti: Long = 0
        File(rosFilePath).forEachLine {
            val (product, quantity, price) = it.split(", ")
            gti += quantity.toLong() * price.toLong()
        }
        return gti
    }

}
