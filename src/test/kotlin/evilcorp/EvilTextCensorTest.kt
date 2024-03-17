package evilcorp

import io.kotest.matchers.shouldBe
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
}
