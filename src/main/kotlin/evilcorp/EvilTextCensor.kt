package evilcorp

class EvilTextCensor(private val blacklistedWords: Set<String>) {
    fun censor(text: String): String = blacklistedWords.fold(text) { censoredText, blacklistedWord ->
        censoredText.replace(blacklistedWord, "X".repeat(blacklistedWord.length))
    }
}
