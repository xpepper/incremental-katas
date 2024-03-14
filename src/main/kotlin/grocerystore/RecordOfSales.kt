package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(.+),\s*(\d+),\s*(\d+)${'$'}""")

class RecordOfSales(rosFilePath: String) {
    private val entries: List<Entry> = parseToEntries(File(rosFilePath))

    fun computeGrandTotalIncome(): Long = entries.sumOf { entry -> entry.quantity * entry.price }

    fun allProducts(): List<String> = entries.map { it.product }

    private fun parseToEntries(rosFile: File): List<Entry> = rosFile.readLines().map(::parse)

    private fun parse(rawEntry: String): Entry {
        val groups = quantityAndPriceRegex.find(rawEntry)?.groups
        val product = groups?.get(1)?.value?.trim() ?: ""
        val quantity = groups?.get(2)?.value?.toLong() ?: 0
        val price = groups?.get(3)?.value?.toLong() ?: 0
        return Entry(product, quantity, price)
    }

    private data class Entry(val product: String, val quantity: Long, val price: Long)
}
