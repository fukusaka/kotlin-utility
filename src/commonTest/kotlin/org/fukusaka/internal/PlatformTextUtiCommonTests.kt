package org.fukusaka.internal

import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
class PlatformTextUtiCommonTests {

    @Test
    fun testBasicTypeLenientToString() {
        // Byte, Short, Int, Long
        // UByte, UShort, UInt, ULong
        // Float, Double
        // Boolean
        // Char, String
        val a01: Byte = 12
        val a02: Short = 34
        val a03: Int = 56
        val a04: Long = 78L
        val a05: UByte = 120U
        val a06: UShort = 340U
        val a07: UInt = 560U
        val a08: ULong = 780UL
        val a09: Float = 123.4F
        val a10: Double = 567.8
        val a11: Boolean = false
        val a12: Boolean = true
        val a13: Char = 'a'
        val a14: String = "abc"
        val a15 = null

        assertEquals("12", a01.lenientToString())
        assertEquals("34", a02.lenientToString())
        assertEquals("56", a03.lenientToString())
        assertEquals("78", a04.lenientToString())
        assertEquals("120", a05.lenientToString())
        assertEquals("340", a06.lenientToString())
        assertEquals("560", a07.lenientToString())
        assertEquals("780", a08.lenientToString())
        assertEquals("123.4", a09.lenientToString())
        assertEquals("567.8", a10.lenientToString())
        assertEquals("false", a11.lenientToString())
        assertEquals("true", a12.lenientToString())
        assertEquals("a", a13.lenientToString())
        assertEquals("abc", a14.lenientToString())
        assertEquals("null", a15.lenientToString())
    }

    @Test
    fun testBasicTypeArraysLenientToString() {
        // UByte, UShort, UInt, ULong
        val a05: UByteArray = ubyteArrayOf(120U, 210U)
        val a06: UShortArray = ushortArrayOf(340U, 430U)
        val a07: UIntArray = uintArrayOf(560U, 650U)
        val a08: ULongArray = ulongArrayOf(780UL, 870UL)

        assertEquals("UByteArray(storage=[120, -46])", a05.lenientToString())
        assertEquals("UShortArray(storage=[340, 430])", a06.lenientToString())
        assertEquals("UIntArray(storage=[560, 650])", a07.lenientToString())
        assertEquals("ULongArray(storage=[780, 870])", a08.lenientToString())
    }

    @Test
    fun testCollectionsLenientToString() {
        val a03 = mutableListOf(56, 65)
        val a04 = setOf(78, 87)
        val a05 = mutableSetOf(91, 19)
        val a06 = mapOf(1 to "a", 2 to "b")
        val a07 = mapOf(3 to "c", 4 to "d")
        val a08 = mutableMapOf(5 to "e", 6 to "f")
        val a09 = mutableMapOf(7 to "g", 8 to "h")

        assertEquals("[56, 65]", a03.lenientToString())
        assertEquals("[78, 87]", a04.lenientToString())
        assertEquals("[91, 19]", a05.lenientToString())
        assertEquals("{1=a, 2=b}", a06.lenientToString())
        assertEquals("{3=c, 4=d}", a07.lenientToString())
        assertEquals("{5=e, 6=f}", a08.lenientToString())
        assertEquals("{7=g, 8=h}", a09.lenientToString())
    }
}
