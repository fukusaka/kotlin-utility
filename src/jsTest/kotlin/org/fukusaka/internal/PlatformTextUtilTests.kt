package org.fukusaka.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformTextUtilTests {

    @Test
    fun testBasicTypeArraysLenientToStringJS() {
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

        println("a01: $a01")
        println("a02: $a02")
        println("a03: $a03")
        println("a04: $a04")

        println("a09: $a09")
        println("a10: $a10")
        println("a11: $a11")
        println("a12: $a12")

        // JavaScript Array
        assertEquals("12,21", a01.lenientToString())
        assertEquals("34,43", a02.lenientToString())
        assertEquals("56,65", a03.lenientToString())
        assertEquals("78,87", a04.lenientToString())

        // assertEquals("123.4,432.1", a09.lenientToString())
        // assertEquals("567.8,876.5", a10.lenientToString())
        assertEquals("false,true", a11.lenientToString())
        assertEquals("97,98", a12.lenientToString())
    }

    @Test
    fun testThrewExceptionLenientToStringJS() {
        val a01: Sample01 = Sample01()

        assertEquals("<Sample01 threw Sample01Exception>", a01.lenientToString())
    }


    @Test
    fun testCollectionsLenientToStringJS() {
        val a01 = arrayOf(12, 21)
        val a02 = listOf(34, 43)

        assertEquals("12,21", a01.lenientToString())
        assertEquals("[34, 43]", a02.lenientToString())
    }
}
