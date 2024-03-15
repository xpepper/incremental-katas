package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(.+),\s*(\d+),\s*(\d+)${'$'}""")

class RecordOfSales(rosFilePath: String) {
    private val entries: List<Entry> = parseToEntries(File(rosFilePath))

    fun computeGrandTotalIncome(): Long = entries.sumOf { entry -> entry.quantity * entry.price }

    private fun parseToEntries(rosFile: File): List<Entry> = rosFile.readLines().map(::parse)

    private fun parse(rawEntry: String): Entry {
        val groups = quantityAndPriceRegex.find(rawEntry)?.groups ?: throw InvalidContentException(rawEntry)

        val product = groups[1]?.value?.trim() ?: throw InvalidContentException(rawEntry)
        val quantity = groups[2]?.value?.toLong() ?: throw InvalidContentException(rawEntry)
        val price = groups[3]?.value?.toLong() ?: throw InvalidContentException(rawEntry)
        return Entry(product, quantity, price)
    }

    fun withCategories(rawCategories: String) = CategorizedRecordOfSales(this, rawCategories)

    private data class Entry(val product: String, val quantity: Long, val price: Long)

    class CategorizedRecordOfSales(private val recordOfSales: RecordOfSales, private val rawCategories: String) {
        fun generateReport(): String = categoriesFrom(rawCategories).joinToString("\n") { category ->
            recordOfSales.entries.filter {
                val product = it.product
                product.contains(category.first.name)
            }.sumOf { it.quantity * it.price }.let {
                "${category.second.name}: $it"
            }
        } + "\ntotal: ${recordOfSales.computeGrandTotalIncome()}"

        private fun categoriesFrom(rawCategories: String): List<Pair<ProductFamily, Category>> =
            rawCategories.split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.split(", ") }
                .map { ProductFamily(it[0]) to Category(it[1]) }
    }
}

@JvmInline
value class Category(internal val name: String)

@JvmInline
value class ProductFamily(internal val name: String)
