package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(.+),\s*(\d+),\s*(\d+)${'$'}""")

class RecordOfSales(rosFilePath: String) {
    private val entries: List<Entry> = parseToEntries(File(rosFilePath))

    fun computeGrandTotalIncome(): Long = entries.sumOf { entry -> entry.quantity * entry.price }

    private fun parseToEntries(rosFile: File): List<Entry> = rosFile.readLines().map(::parse)

    private fun parse(rawEntry: String): Entry {
        val groups = quantityAndPriceRegex.find(rawEntry)?.groups
        val product = groups?.get(1)?.value?.trim() ?: ""
        val quantity = groups?.get(2)?.value?.toLong() ?: 0
        val price = groups?.get(3)?.value?.toLong() ?: 0
        return Entry(product, quantity, price)
    }

    fun withCategories(rawCategories: String) = CategorizedRecordOfSales(this, rawCategories)

    private data class Entry(val product: String, val quantity: Long, val price: Long)

    class CategorizedRecordOfSales(private val recordOfSales: RecordOfSales, private val rawCategories: String) {
        fun generateReport(): String = categoriesFrom(rawCategories).joinToString("\n") { category ->
            recordOfSales.entries.filter {
                it.product.contains(category.first.name)
            }.sumOf { it.quantity * it.price }.let {
                "${category.second.name}: $it"
            }
        } + "\ntotal: ${recordOfSales.computeGrandTotalIncome()}"

        private fun categoriesFrom(rawCategories: String): List<Pair<ProductItem, Category>> =
            rawCategories.split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.split(", ") }
                .map { ProductItem(it[0]) to Category(it[1]) }
    }
}

@JvmInline
value class Category(internal val name: String)

@JvmInline
value class ProductItem(internal val name: String)
