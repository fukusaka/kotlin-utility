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

import org.fukusaka.internal.lenientFormat

abstract class AbstractMapBasedMultiset<E> protected constructor(
    protected open var backingMap: MutableMap<E, Count>
) : AbstractMutableMultiset<E>() {

    private var totalSize: Long = 0

    override val size: Int
        get() = totalSize.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

    override fun elementIterator(): MutableIterator<E> {
        val backingEntries = backingMap.entries.iterator()

        return object : MutableIterator<E> {
            var toRemove: MutableMap.MutableEntry<E, Count>? = null

            override fun hasNext(): Boolean = backingEntries.hasNext()
            override fun next(): E = backingEntries.next().also { toRemove = it }.key

            override fun remove() {
                totalSize -= checkNotNull(toRemove).value.getAndSet(0)
                backingEntries.remove()
                toRemove = null
            }
        }
    }

    override fun entryIterator(): MutableIterator<Multiset.Entry<E>> {
        val backingEntries = backingMap.entries.iterator()

        return object : MutableIterator<Multiset.Entry<E>> {
            var toRemove: MutableMap.MutableEntry<E, Count>? = null

            override fun hasNext(): Boolean = backingEntries.hasNext()
            override fun next(): Multiset.Entry<E> {
                return object : Multisets.AbstractEntry<E>() {
                    private val mapEntry: Map.Entry<E, Count> = backingEntries.next().also { toRemove = it }

                    override val element: E
                        get() = mapEntry.key

                    override val count: Int
                        get() = when (val count = mapEntry.value.get()) {
                            0 -> backingMap[element]?.get() ?: 0
                            else -> count
                        }
                }
            }

            override fun remove() {
                totalSize -= checkNotNull(toRemove).value.getAndSet(0)
                backingEntries.remove()
                toRemove = null
            }
        }
    }

    override fun distinctElements(): Int = backingMap.size

    override fun count(element: E): Int = backingMap[element]?.get() ?: 0

    override fun add(element: E, occurrences: Int): Int {
        require(occurrences >= 0) { "occurrences cannot be negative: %s".lenientFormat(occurrences) }

        if (occurrences == 0) return count(element)

        val frequency: Count? = backingMap[element]
        val oldCount: Int = frequency?.get() ?: 0

        if (frequency == null) {
            backingMap[element] = Count(occurrences)
        } else {
            val newCount = oldCount.toLong() + occurrences.toLong()
            check(newCount <= Int.MAX_VALUE) { "too many occurrences: %s".lenientFormat(newCount) }
            frequency.add(occurrences)
        }

        totalSize += occurrences.toLong()

        return oldCount
    }

    override fun remove(element: E, occurrences: Int): Int {
        require(occurrences >= 0) { "occurrences cannot be negative: %s".lenientFormat(occurrences) }

        if (occurrences == 0) return count(element)

        val frequency: Count? = backingMap[element]
        val oldCount: Int = frequency?.get() ?: return 0

        val numberRemoved: Int = oldCount.coerceAtMost(occurrences)
        if (oldCount <= occurrences) {
            backingMap.remove(element)
        }
        frequency.add(-numberRemoved)

        totalSize -= numberRemoved.toLong()

        return oldCount
    }

    override fun setCount(element: E, count: Int): Int {
        require(count >= 0) { "count" }

        val existingCounter: Count? = backingMap[element]
        val oldCount: Int = existingCounter?.getAndSet(count) ?: 0

        when {
            count == 0 -> backingMap.remove(element)
            existingCounter == null -> backingMap[element] = Count(count)
        }

        totalSize += (count - oldCount).toLong()

        return oldCount
    }

    override fun clear() {
        backingMap.values.forEach { it.set(0) }
        backingMap.clear()
        totalSize = 0L
    }

    override fun iterator(): MutableIterator<E> =
        MapBasedMultisetIterator()

    /**
     * Not subclassing AbstractMultiset$MultisetIterator because next() needs to
     * retrieve the Map.Entry<E, Count> entry, which can then be used for
     * a more efficient remove() call.
     */
    private inner class MapBasedMultisetIterator : MutableIterator<E> {
        val entryIterator: MutableIterator<MutableMap.MutableEntry<E, Count>> = backingMap.entries.iterator()

        var currentEntry: MutableMap.MutableEntry<E, Count>? = null
            private set(newValue) {
                field = newValue
                val count = newValue?.value?.get() ?: 0
                laterCount = count
            }

        var laterCount = 0
        var canRemove = false

        override fun hasNext(): Boolean = laterCount > 0 || entryIterator.hasNext()

        override fun next(): E {
            if (!hasNext()) throw NoSuchElementException()

            if (laterCount == 0) currentEntry = entryIterator.next()
            laterCount--

            canRemove = true
            return checkNotNull(currentEntry).key
        }

        override fun remove() {
            check(canRemove) { "no calls to next() since the last call to remove()" }
            val entry = currentEntry ?: throw IllegalStateException()

            if (entry.value.get() <= 0) throw ConcurrentModificationException()

            if (entry.value.addAndGet(-1) == 0) {
                entryIterator.remove()
            }
            totalSize--
            canRemove = false
        }
    }
}
