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
        return Entry(ProductItem(product), quantity, price)
    }

    fun withCategories(rawCategories: String) = CategorizedRecordOfSales(this, rawCategories)

    private data class Entry(val product: ProductItem, val quantity: Long, val price: Long)

    class CategorizedRecordOfSales(private val recordOfSales: RecordOfSales, private val rawCategories: String) {
        fun generateReport(): String = categoriesFrom(rawCategories).joinToString("\n") { (productFamily, category) ->
            recordOfSales.entries
                .filter { (product, _, _) -> product.belongsTo(productFamily) }
                .sumOf { (_, quantity, price) -> quantity * price }
                .let { categoryTotal -> "${category.name}: $categoryTotal" }
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

@JvmInline
value class ProductItem(private val name: String) {
    fun belongsTo(productFamily: ProductFamily) = name.contains(productFamily.name)
}
