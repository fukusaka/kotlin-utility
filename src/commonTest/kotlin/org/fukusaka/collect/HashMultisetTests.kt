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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

typealias MultisetType<E> = HashMultiset<E>

class HashMultisetTests {

    @Test
    fun testCreate() {
        val multiset: MutableMultiset<String> = MultisetType()
        multiset.add("foo", 2)
        multiset.add("bar")
        assertEquals(3, multiset.size)
        assertEquals(2, multiset.count("foo"))
    }

    @Test
    fun testCreateWithSize() {
        val multiset: MutableMultiset<String> = MultisetType(50)
        multiset.add("foo", 2)
        multiset.add("bar")
        assertEquals(3, multiset.size)
        assertEquals(2, multiset.count("foo"))
    }

    @Test
    fun testCreateFromIterable() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        assertEquals(3, multiset.size)
        assertEquals(2, multiset.count("foo"))
    }

    @Test
    fun testCreateElementEntry() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())

        assertEquals(setOf("foo", "bar"), multiset.elementSet)
        assertEquals(
            setOf(
                Multisets.entry("foo", 2),
                Multisets.entry("bar", 1),
            ), multiset.entrySet
        )
    }

    @Test
    fun testEquals() {
        val multiset1: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        val multiset2: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        assertTrue(multiset1.equals(multiset2))
    }

    @Test
    fun testEqualsWithTypeMissmatch() {
        val multiset1: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        val multiset2: MutableMultiset<Long> = MultisetType(arrayOf(1L, 3L, 1L).asIterable())
        assertFalse(multiset1.equals(multiset2))
    }

    @Test
    fun testAddWithOccurrences() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.add("foo", 2)
        assertEquals(5, multiset.size)
        assertEquals(4, multiset.count("foo"))
    }

    @Test
    fun testRemove() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.remove("foo")
        assertEquals(2, multiset.size)
        assertEquals(1, multiset.count("foo"))
    }

    @Test
    fun testRemoveWithOccurrences() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.remove("foo", 2)
        assertEquals(1, multiset.size)
        assertEquals(0, multiset.count("foo"))
    }

    @Test
    fun testSetCount() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.setCount("foo", 3)
        assertEquals(4, multiset.size)
        assertEquals(3, multiset.count("foo"))
    }

    @Test
    fun testSetCountWithOld() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.setCount("foo", 2, 4)
        assertEquals(5, multiset.size)
        assertEquals(4, multiset.count("foo"))
    }

    @Test
    fun testSetCountWithInvalidOld() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())
        multiset.setCount("foo", 3, 4)
        assertEquals(3, multiset.size)
        assertEquals(2, multiset.count("foo"))
    }

    @Test
    fun testIterator() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())

        val iterator = multiset.iterator()
        for (element in iterator) {
            if (element == "foo") {
                iterator.remove()
                break
            }
        }
        assertEquals(2, multiset.size)
        assertEquals(1, multiset.count("foo"))
    }

    @Test
    fun testElementSetIterator() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())

        val iterator = multiset.elementSet.iterator()
        for (element in iterator) {
            if (element == "foo") {
                iterator.remove()
            }
        }
        assertEquals(setOf("bar"), multiset.elementSet)
        assertEquals(setOf(Multisets.entry("bar", 1)), multiset.entrySet)
    }

    @Test
    fun testEntrySetIterator() {
        val multiset: MutableMultiset<String> = MultisetType(arrayOf("foo", "bar", "foo").asIterable())

        val iterator = multiset.entrySet.iterator()
        for (entry in iterator) {
            if (entry.element == "foo") {
                iterator.remove()
            }
        }
        assertEquals(setOf("bar"), multiset.elementSet)
        assertEquals(setOf(Multisets.entry("bar", 1)), multiset.entrySet)
    }
}
