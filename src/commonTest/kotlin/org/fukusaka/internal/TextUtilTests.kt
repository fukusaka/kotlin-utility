package org.fukusaka.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class TextUtilTests {

    @Suppress("ArrayInDataClass")
    data class Fixture(
        val expected: String,
        val format: String,
        val args: Array<Any?>?,
    )

    private val fixtures = listOf(
        Fixture("something", "something", null),
        Fixture("something ABC", "something %s", arrayOf("ABC")),
        Fixture("something ABC DEF", "something %s %s", arrayOf("ABC", "DEF")),
        Fixture("something ABC DEF GHI", "something %s %s %s", arrayOf("ABC", "DEF", "GHI")),
        Fixture("something ABC %s", "something %s %s", arrayOf("ABC")),
        Fixture("something ABC DEF", "something %s %s", arrayOf("ABC", "DEF")),
        Fixture("something ABC DEF [GHI]", "something %s %s", arrayOf("ABC", "DEF", "GHI")),
    )

    @Test
    fun testLenientToString() {

        for (fixture in fixtures) {
            val actual: String = if (fixture.args == null) {
                fixture.format.lenientFormat()
            } else {
                fixture.format.lenientFormat(*fixture.args)
            }
            assertEquals(fixture.expected, actual)
        }
    }
}
