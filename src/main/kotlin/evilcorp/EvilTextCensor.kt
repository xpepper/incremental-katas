package evilcorp

class EvilTextCensor(private val blacklistedWords: Set<String>) {
    fun censor(text: String): String {
        var censoredText = ""
        blacklistedWords.forEach { blacklistedWord ->
            censoredText = text.replace(blacklistedWord, "XXXX")
        }
        return censoredText
    }

}
