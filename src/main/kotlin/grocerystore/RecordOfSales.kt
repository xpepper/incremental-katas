package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(\d+),\s*(\d+)$""")

class RecordOfSales(rosFilePath: String) {
    private val entries: List<Entry> = compute(File(rosFilePath))

    fun computeGrandTotalIncome(): Long = entries.sumOf { entry -> entry.quantity * entry.price }

    private fun compute(rosFile: File): List<Entry> = rosFile.readLines().map(::parse)

    private fun parse(rawEntry: String): Entry {
        val groups = quantityAndPriceRegex.find(rawEntry)?.groups
        val quantity = groups?.get(1)?.value?.toLong() ?: 0
        val price = groups?.get(2)?.value?.toLong() ?: 0
        return Entry(quantity, price)
    }

    private data class Entry(val quantity: Long, val price: Long)
}
