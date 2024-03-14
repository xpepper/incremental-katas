package grocerystore

import java.io.File

private val quantityAndPriceRegex = Regex("""(\d+),\s*(\d+)$""")

class RecordOfSales(rosFilePath: String) {
    private val rosFile = File(rosFilePath)

    fun computeGrandTotalIncome(): Long {
        return rosFile.readLines()
            .map { entry ->
                val groups = quantityAndPriceRegex.find(entry)?.groups
                val quantity = groups?.get(1)?.value?.toLong() ?: 0
                val price = groups?.get(2)?.value?.toLong() ?: 0
                quantity * price
            }.sum()
    }

}
