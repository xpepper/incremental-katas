package grocerystore

import java.io.File

internal fun writeRosFileWith(fileContent: String) = File.createTempFile("ros", ".txt").apply {
    writeText(fileContent.trimIndent())
}.also { it.deleteOnExit() }
