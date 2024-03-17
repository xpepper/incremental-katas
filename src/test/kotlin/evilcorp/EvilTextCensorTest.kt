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
        val textCensor = EvilTextCensor(blacklistedWords = setOf("nice", "honest", "generous"))

        textCensor.censor("You are a nice, honest and generous person") shouldBe "You are a XXXX, XXXXXX and XXXXXXXX person"
    }
}
