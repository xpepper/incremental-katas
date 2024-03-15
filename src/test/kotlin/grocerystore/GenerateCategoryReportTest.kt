package grocerystore

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GenerateCategoryReportTest {

    @Test
    fun `generate the category report even when some categories are missing`() {
        val rosFile = writeRosFileWith(
            """
            bread, 1, 2
            12-pack of eggs, 1, 2
            milk (1L), 4, 8
            coca cola (33cl), 10, 10
            orange juice (1L), 2, 3
            """
        )

        RecordOfSales(rosFile.absolutePath).withCategories(
            """
            bread, wheat and pasta
            juice, drinks
            """
        ).generateReport() shouldBe """
            wheat and pasta: 2
            drinks: 6
            total: 142
        """.trimIndent()
    }

    @Test
    fun `generate the category report grouping different product families belonging to the same category`() {
        val rosFile = writeRosFileWith(
            """
            milk (1L), 4, 8
            butter (500 g), 3, 6
            cheese (1Kg), 1, 7
            cheese (gouda, 1Kg), 1, 5
            """
        )

        RecordOfSales(rosFile.absolutePath).withCategories(
            """
            milk, dairy
            butter, dairy
            cheese, dairy
            """
        ).generateReport() shouldBe """
            dairy: 62
            total: 62
        """.trimIndent()
    }

    @Test
    fun `fails when cannot parse a ROS entry`() {
        val invalidRosFile = writeRosFileWith(
            """
            invalid entry
            """
        )
        shouldThrow<InvalidContentException> {
            RecordOfSales(invalidRosFile.absolutePath)
        }.message shouldBe "Invalid entry 'invalid entry' found in the ROS file."
    }
}
