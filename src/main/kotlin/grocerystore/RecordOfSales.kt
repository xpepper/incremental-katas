package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(\d+),\s*(\d+)$""")

class RecordOfSales(private val rosFilePath: String) {
    fun computeGrandTotalIncome(): Long {
        return File(rosFilePath).readLines()
            .map { entry ->
                val groups = quantityAndPriceRegex.find(entry)?.groups
                val quantity = groups?.get(1)?.value?.toLong() ?: 0
                val price = groups?.get(2)?.value?.toLong() ?: 0
                quantity * price
            }.sum()
    }

}
