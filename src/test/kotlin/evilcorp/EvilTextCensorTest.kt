package evilcorp

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class EvilTextCensorTest {
    @Test
    fun `censor text with a single blacklisted word`() {
        val textCensor = EvilTextCensor(blacklistedWords = setOf("nice"))

        textCensor.censor("You are a nice person") shouldBe "You are a XXXX person"
    }

    @Test
    fun `censor text with some blacklisted words`() {
        val textCensor = EvilTextCensor(
            blacklistedWords = setOf("nice", "pony", "sun", "light", "fun", "happy", "funny", "joy")
        )

        textCensor.censor("Such a nice day with a bright sun, makes me happy") shouldBe
                "Such a XXXX day with a bright XXX, makes me XXXXX"
    }

    @Test @Disabled
    fun `censor the whole word which the blacklisted word is a prefix of`() {
        val textCensor = EvilTextCensor(setOf("friend"))

        textCensor.censor("You are so friendly!") shouldBe "You are so XXXXXXXXX"
    }
}
