package grocerystore

import java.io.File

class RecordOfSales(private val rosFilePath: String) {
    fun computeGrandTotalIncome(): Long = File(rosFilePath).readLines()
        .map { entry ->
            val (product, quantity, price) = entry.split(", ").map { it.trim() }
            quantity.toLong() * price.toLong()
        }.sum()

}
