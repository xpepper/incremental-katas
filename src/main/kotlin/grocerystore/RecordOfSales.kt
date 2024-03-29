package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(?<product>.+),\s*(?<quantity>\d+),\s*(?<price>\d+)${'$'}""")

class RecordOfSales(rosFilePath: String) {
    private val entries: List<Entry> = parseToEntries(File(rosFilePath))

    fun computeGrandTotalIncome(): Long = entries.grantTotalIncome()

    private fun parseToEntries(rosFile: File): List<Entry> =
        rosFile.bufferedReader().useLines { lines -> lines.map(::parse).toList() }

    private fun parse(rawEntry: String): Entry {
        val groups = quantityAndPriceRegex.find(rawEntry)?.groups ?: throw InvalidContentException(rawEntry)

        val product = groups["product"]?.value?.trim() ?: throw InvalidContentException(rawEntry)
        val quantity = groups["quantity"]?.value?.toLong() ?: throw InvalidContentException(rawEntry)
        val price = groups["price"]?.value?.toLong() ?: throw InvalidContentException(rawEntry)
        return Entry(ProductItem(product), quantity, price)
    }

    fun withCategories(rawCategories: String) = CategorizedRecordOfSales(this, rawCategories)


    class CategorizedRecordOfSales(private val recordOfSales: RecordOfSales, private val rawCategories: String) {
        fun generateReport(): String = categoriesFrom(rawCategories).map { (category, productFamilies) ->
            recordOfSales.entries
                .filter { entry -> productFamilies.any { entry.product.belongsTo(it) } }
                .grantTotalIncome()
                .let { categoryTotal -> "${category.name}: $categoryTotal" }
        }.joinToString("\n") + "\ntotal: ${recordOfSales.computeGrandTotalIncome()}"

        private fun categoriesFrom(rawCategories: String): Map<Category, Set<ProductFamily>> =
            rawCategories.split("\n")
                .asSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.split(", ") }
                .map { Category(it[1]) to ProductFamily(it[0]) }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, value) -> value.toSet() }
    }
}

private data class Entry(val product: ProductItem, val quantity: Long, val price: Long)

private fun List<Entry>.grantTotalIncome() = sumOf { (_, quantity, price) -> quantity * price }

@JvmInline
value class Category(internal val name: String)

@JvmInline
value class ProductFamily(internal val name: String)

@JvmInline
value class ProductItem(private val name: String) {
    fun belongsTo(productFamily: ProductFamily) = name.contains(productFamily.name)
}

