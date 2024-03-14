package grocerystore

import java.io.File

class RecordOfSales(private val rosFilePath: String) {
    fun computeGrandTotalIncome(): Long {
        var gti: Long = 0
        File(rosFilePath).forEachLine {
            val (product, quantity, price) = it.split(", ")
            gti += quantity.toLong() * price.toLong()
        }
        return gti
    }

}
