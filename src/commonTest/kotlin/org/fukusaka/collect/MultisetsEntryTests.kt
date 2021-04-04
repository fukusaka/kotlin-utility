/*
 * Copyright 2021 Shoichi Fukusaka <shoichi.fukusaka@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Based on https://github.com/google/guava licensed under the Apache License 2.0
 */

package org.fukusaka.collect

import org.fukusaka.collect.Multisets.entry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MultisetsEntryTests {
    private val nullString: String? = null

    private fun <E> control(element: E, count: Int): Multiset.Entry<E> {
        return HashMultiset(List(count) { element }).entrySet.iterator().next()
    }

    @Test
    fun testToString() {
        assertEquals("foo", entry("foo", 1).toString())
        assertEquals("bar x 2", entry("bar", 2).toString())
    }

    @Test
    fun testToStringNull() {
        assertEquals("null", entry(nullString, 1).toString())
        assertEquals("null x 2", entry(nullString, 2).toString())
    }

    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun testEquals() {
        assertTrue(control("foo", 1).equals(entry("foo", 1)))
        assertTrue(control("bar", 2).equals(entry("bar", 2)))
        assertFalse(control("foo", 1).equals(entry("foo", 2)))
        assertFalse(entry("foo", 1).equals(control("bar", 1)))
        assertFalse(entry("foo", 1).equals(Any()))
        assertFalse(entry("foo", 1).equals(null))
    }

    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun testEqualsNull() {
        assertTrue(control(nullString, 1).equals(entry(nullString, 1)))
        assertFalse(control(nullString, 1).equals(entry(nullString, 2)))
        assertFalse(entry(nullString, 1).equals(control("bar", 1)))
        assertFalse(entry(nullString, 1).equals(Any()))
        assertFalse(entry(nullString, 1).equals(null))
    }

    @Test
    fun testHashCode() {
        assertEquals(control("foo", 1).hashCode(), entry("foo", 1).hashCode())
        assertEquals(control("bar", 2).hashCode(), entry("bar", 2).hashCode())
    }

    @Test
    fun testHashCodeNull() {
        assertEquals(control(nullString, 1).hashCode(), entry(nullString, 1).hashCode())
    }

    @Test
    fun testNegativeCount() {
        assertFailsWith(IllegalArgumentException::class) {
            entry("foo", -1)
        }
    }
}
