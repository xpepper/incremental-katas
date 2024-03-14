package grocerystore

import java.io.File

class RecordOfSales(private val rosFilePath: String) {
    fun computeGrandTotalIncome(): Long = File(rosFilePath).readLines()
        .map {
            val (product, quantity, price) = it.split(", ")
            quantity.toLong() * price.toLong()
        }.sum()

}
