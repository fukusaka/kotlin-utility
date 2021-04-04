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

import org.fukusaka.internal.lenientToString

object Multisets {

    fun <E> inferDistinctElements(elements: Iterable<E>): Int {
        return when (elements) {
            is Multiset -> elements.elementSet.size
            else -> {
                // initial capacity will be rounded up to 16
                11
            }
        }
    }

    fun <E> entry(element: E, count: Int): Multiset.Entry<E> =
        Entry(element, count)

    class Entry<E>(
        override val element: E,
        override val count: Int
    ) : AbstractEntry<E>() {
        init {
            require(count >= 0) { "count cannot be negative but was: $count" }
        }
    }

    fun <E> mutableEntry(element: E, count: Int): MutableMultiset.MutableEntry<E> =
        MutableEntry(element, count)

    class MutableEntry<E>(
        override var element: E,
        override var count: Int
    ) : AbstractMutableEntry<E>() {
        init {
            require(count >= 0) { "count cannot be negative but was: $count" }
        }
    }

    /**
     * Implementation of the equals/hashCode/toString
     */
    abstract class AbstractEntry<E> : Multiset.Entry<E> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AbstractEntry<*>) return false
            return (count == other.count && element == other.element)
        }

        override fun hashCode(): Int = element.hashCode() xor count
        override fun toString(): String = if (count == 1) element.lenientToString() else "${element.lenientToString()} x $count"
    }

    abstract class AbstractMutableEntry<E> : AbstractEntry<E>(), MutableMultiset.MutableEntry<E>

    abstract class AbstractMutableElementSet<E> : AbstractMutableSet<E>() {
        abstract fun multiset(): MutableMultiset<E>

        override val size: Int get() = multiset().entrySet.size
        abstract override operator fun iterator(): MutableIterator<E>
        override fun add(element: E): Boolean = throw UnsupportedOperationException()

        override fun isEmpty(): Boolean = multiset().isEmpty()
        override operator fun contains(element: @UnsafeVariance E): Boolean = multiset().contains(element)
        override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean = multiset().containsAll(elements)

        override fun remove(element: E): Boolean = multiset().remove(element, Int.MAX_VALUE) > 0
        override fun clear(): Unit = multiset().clear()
    }

    abstract class AbstractMutableEntrySet<E> : AbstractMutableSet<Multiset.Entry<E>>() {
        abstract fun multiset(): MutableMultiset<E>

        abstract override val size: Int
        abstract override operator fun iterator(): MutableIterator<Multiset.Entry<E>>
        override fun add(element: Multiset.Entry<E>): Boolean = throw UnsupportedOperationException()

        override fun contains(element: Multiset.Entry<E>): Boolean =
            (element.count > 0) && multiset().count(element.element) == element.count

        override fun remove(element: Multiset.Entry<E>): Boolean =
            (element.count > 0) && multiset().setCount(element.element, element.count, 0)

        override fun clear(): Unit = multiset().clear()
    }

    /**
     * An implementation of Multiset#equals
     */
    fun <E> equalsImpl(multiset: Multiset<E>, that: Any?): Boolean {
        if (that === multiset) return true
        if (that !is Multiset<*>) return false
        @Suppress("UNCHECKED_CAST")
        return equalsImpl(multiset, that as Multiset<E>)
    }

    private fun <E> equalsImpl(multiset: Multiset<E>, that: Multiset<out E>): Boolean {
        if (that === multiset) return true
        if (multiset.size != that.size || multiset.entrySet.size != that.entrySet.size) return false
        return that.entrySet.all { multiset.count(it.element) == it.count }
    }

    /**
     * An implementation of Multiset#addAll
     */
    fun <E> addAllImpl(self: MutableMultiset<E>, elements: Collection<E>): Boolean =
        when {
            elements is Multiset -> addAllImpl(self, elements)
            elements.isEmpty() -> false
            else -> {
                val iterator = elements.iterator()
                var wasModified = false
                while (iterator.hasNext()) {
                    wasModified = wasModified or self.add(iterator.next())
                }
                wasModified
            }
        }

    private fun <E> addAllImpl(self: MutableMultiset<E>, elements: Multiset<out E>): Boolean =
        when {
            elements.isEmpty() -> false
            else -> true.also { elements.entrySet.forEach { self.add(it.element, it.count) } }
        }

    /**
     * An implementation of Multiset#removeAll
     */
    fun removeAllImpl(self: MutableMultiset<*>, elementsToRemove: Collection<*>): Boolean =
        when (elementsToRemove) {
            is Multiset -> elementsToRemove.elementSet
            else -> elementsToRemove
        }.let { collection -> self.elementSet.removeAll(collection) }

    /**
     * An implementation of Multiset#retainAll
     */
    fun retainAllImpl(self: MutableMultiset<*>, elementsToRetain: Collection<*>): Boolean =
        when (elementsToRetain) {
            is Multiset -> elementsToRetain.elementSet
            else -> elementsToRetain
        }.let { collection -> self.elementSet.retainAll(collection) }

    /**
     * An implementation of Multiset#setCount(,Int)
     */
    fun <E> setCountImpl(self: MutableMultiset<E>, element: E, count: Int): Int {
        require(count >= 0) { "count" }
        return self.count(element).also { oldCount ->
            val delta = count - oldCount
            when {
                delta > 0 -> self.add(element, delta)
                delta < 0 -> self.remove(element, -delta)
            }
        }
    }

    /**
     * An implementation of Multiset#setCount(,Int, Int)
     */
    fun <E> setCountImpl(self: MutableMultiset<E>, element: E, oldCount: Int, newCount: Int): Boolean {
        require(oldCount >= 0) { "oldCount" }
        require(newCount >= 0) { "newCount" }
        return when (oldCount) {
            self.count(element) -> true.also { self.setCount(element, newCount) }
            else -> false
        }
    }

    /**
     * An implementation of Multiset#iterator
     */
    fun <E> iteratorImpl(multiset: Multiset<E>): Iterator<E> =
        MultisetIterator(multiset, multiset.entrySet.iterator())

    fun <E> iteratorImpl(multiset: MutableMultiset<E>): MutableIterator<E> =
        MutableMultisetIterator(multiset, multiset.entrySet.iterator())

    open class MultisetIterator<E>(
        protected open val multiset: Multiset<E>,
        protected open val entryIterator: Iterator<Multiset.Entry<E>>
    ) : Iterator<E> {

        protected var currentEntry: Multiset.Entry<E>? = null
            private set(newValue) {
                field = newValue
                val count = newValue?.count ?: 0
                totalCount = count
                laterCount = count
            }

        /** Count of subsequent elements equal to current element  */
        private var laterCount = 0

        /** Count of all elements equal to current element  */
        protected var totalCount = 0

        override fun hasNext(): Boolean = laterCount > 0 || entryIterator.hasNext()

        override fun next(): E {
            if (!hasNext()) throw NoSuchElementException()

            if (laterCount == 0) currentEntry = entryIterator.next()
            laterCount--

            return checkNotNull(currentEntry).element
        }
    }

    class MutableMultisetIterator<E>(
        override val multiset: MutableMultiset<E>,
        override val entryIterator: MutableIterator<Multiset.Entry<E>>
    ) : MultisetIterator<E>(multiset, entryIterator), MutableIterator<E> {

        private var canRemove = false

        override fun next(): E = super.next().also { canRemove = true }

        override fun remove() {
            check(canRemove) { "no calls to next() since the last call to remove()" }

            if (totalCount == 1) {
                entryIterator.remove()
            } else {
                multiset.remove(checkNotNull(currentEntry).element)
            }

            totalCount--
            canRemove = false
        }
    }
}
