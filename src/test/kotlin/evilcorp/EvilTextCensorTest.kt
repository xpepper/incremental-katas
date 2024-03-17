package evilcorp

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class EvilTextCensorTest {
    //Thus, if the word 'nice' is blacklisted, our customer would expect the input
    //text 'You are a nice person' to become 'You are a XXXX person'.
    @Test
    fun `censor a single blacklisted word`() {
        val textCensor = EvilTextCensor(blacklistedWords = setOf("nice"))

        textCensor.censor("You are a nice person") shouldBe "You are a XXXX person"
    }
}
