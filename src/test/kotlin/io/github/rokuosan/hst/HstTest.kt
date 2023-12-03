package io.github.rokuosan.hst

import com.github.ajalt.clikt.testing.test
import io.github.rokuosan.hst.commands.Add
import kotlin.test.Test
import kotlin.test.assertEquals

class HstTest {
    @Test
    fun testHst() {
        val cmd = Add()
        val result = cmd.test()
        assertEquals(result.output, "")
    }
}
