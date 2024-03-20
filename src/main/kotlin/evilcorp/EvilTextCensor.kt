package evilcorp

class EvilTextCensor(private val blacklistedWords: Set<String>) {
    fun censor(text: String): String = blacklistedWords.fold(text) { censoredText, blacklistedWord ->
        censoredText.replace(Regex("\\b($blacklistedWord\\w*!*)")) { "X".repeat(it.value.length) }
    }
}
