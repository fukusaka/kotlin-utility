package org.fukusaka.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlatformTextUtilTests {

    @Test
    fun testBasicTypeArraysLenientToStringJVM() {
        // Byte, Short, Int, Long
        // Float, Double
        // Boolean
        // Char, String
        val a01: ByteArray = byteArrayOf(12, 21)
        val a02: ShortArray = shortArrayOf(34, 43)
        val a03: IntArray = intArrayOf(56, 65)
        val a04: LongArray = longArrayOf(78L, 87L)
        val a09: FloatArray = floatArrayOf(123.4F, 432.1F)
        val a10: DoubleArray = doubleArrayOf(567.8, 876.5)
        val a11: BooleanArray = booleanArrayOf(false, true)
        val a12: CharArray = charArrayOf('a', 'b')

        // Java VM Type Signatures
        assertTrue(a01.lenientToString().startsWith("[B@"))
        assertTrue(a02.lenientToString().startsWith("[S@"))
        assertTrue(a03.lenientToString().startsWith("[I@"))
        assertTrue(a04.lenientToString().startsWith("[J@"))

        assertTrue(a09.lenientToString().startsWith("[F@"))
        assertTrue(a10.lenientToString().startsWith("[D@"))
        assertTrue(a11.lenientToString().startsWith("[Z@"))
        assertTrue(a12.lenientToString().startsWith("[C@"))
    }

    @Test
    fun testThrewExceptionLenientToStringJVM() {
        val a01: Sample01 = Sample01()

        assertTrue {
            val b01 = a01.lenientToString()
            Regex("<org\\.fukusaka\\.internal\\.Sample01@[0-9a-f]+ threw org\\.fukusaka\\.internal\\.Sample01Exception>")
                .matches(b01)
        }
    }

    @Test
    fun testCollectionsLenientToStringJVM() {
        val a01 = arrayOf(12, 21)
        val a02 = listOf(34, 43)

        assertEquals("12,21", a01.lenientToString())
        assertEquals("[34, 43]", a02.lenientToString())
    }
}
