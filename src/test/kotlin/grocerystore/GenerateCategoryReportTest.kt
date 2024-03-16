package grocerystore

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GenerateCategoryReportTest {

    @Test
    fun `generate the category report with one entry ROS file`() {
        val rosFile = writeRosFileWith(
            """
            bread, 1, 2
            """
        )

        RecordOfSales(rosFile.absolutePath).withCategories(
            """
            bread, wheat and pasta
            """
        ).generateReport() shouldBe """
            wheat and pasta: 2
            total: 2
        """.trimIndent()
    }

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
    fun `generate a full category report`() {
        val rosFile = writeRosFileWith(
            """
            bread, 1, 2
            12-pack of eggs, 1, 2
            milk (1L), 4, 8
            coca cola (33cl), 10, 10
            chicken clubs (frozen), 1, 4
            carrots, 4, 1
            apples (red, 1Kg bag), 1, 2
            butter (500 g), 3, 6
            cheese (1Kg), 1, 7
            bacon ("tasty" brand, 3 pack), 2, 7
            orange juice (1L), 2, 3
            cheese (gouda, 1Kg), 1, 5
            bottled water (1.5L), 5, 5
            twixies (1 whole box, 3 rows, 5 per row), 1, 20
            sirloin (100g), 1, 30
            tomatoes, 12, 3
            bananas, 3, 1
            """
        )

        RecordOfSales(rosFile.absolutePath).withCategories(
            """
            bread, wheat and pasta
            eggs, animalic
            milk, dairy
            coca cola, sodas
            chicken, meat
            beef, meat
            carrots, greens
            apples, fruit
            butter, dairy
            cheese, dairy
            bacon, meat
            juice, drinks
            water, drinks
            twixies, candy
            tomatoes, greens
            bananas, fruit 
            """
        ).generateReport() shouldBe """
            wheat and pasta: 2
            animalic: 2
            dairy: 62
            sodas: 100
            meat: 18
            greens: 40
            fruit: 5
            drinks: 31
            candy: 20
            total: 310
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
