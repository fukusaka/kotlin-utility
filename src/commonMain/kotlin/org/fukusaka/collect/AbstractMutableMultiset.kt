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

abstract class AbstractMutableMultiset<E> : AbstractMutableCollection<E>(), MutableMultiset<E> {

    abstract override val size: Int

    abstract fun elementIterator(): MutableIterator<E>
    abstract fun entryIterator(): MutableIterator<Multiset.Entry<E>>
    abstract fun distinctElements(): Int

    override val elementSet: MutableSet<E> by lazy { createElementSet() }
    override val entrySet: MutableSet<Multiset.Entry<E>> by lazy { createEntrySet() }

    abstract override fun count(element: E): Int
    abstract override fun add(element: E, occurrences: Int): Int
    abstract override fun remove(element: E, occurrences: Int): Int
    abstract override fun clear()

    override fun add(element: E): Boolean = true.also { this.add(element, 1) }
    override fun remove(element: E): Boolean = this.remove(element, 1) > 0
    override fun setCount(element: E, count: Int): Int = Multisets.setCountImpl(this, element, count)
    override fun setCount(element: E, oldCount: Int, newCount: Int): Boolean = Multisets.setCountImpl(this, element, oldCount, newCount)
    override fun addAll(elements: Collection<E>): Boolean = Multisets.addAllImpl(this, elements)
    override fun removeAll(elements: Collection<E>): Boolean = Multisets.removeAllImpl(this, elements)
    override fun retainAll(elements: Collection<E>): Boolean = Multisets.retainAllImpl(this, elements)

    override fun equals(other: Any?): Boolean = Multisets.equalsImpl(this, other)
    override fun hashCode(): Int = entrySet.hashCode()
    override fun toString(): String = entrySet.toString()

    override fun iterator(): MutableIterator<E> = Multisets.iteratorImpl(this)

    open fun createElementSet(): MutableSet<E> = MutableElementSet()
    open fun createEntrySet(): MutableSet<Multiset.Entry<E>> = MutableEntrySet()

    private inner class MutableElementSet : Multisets.AbstractMutableElementSet<E>() {
        override fun multiset(): MutableMultiset<E> = this@AbstractMutableMultiset
        override operator fun iterator(): MutableIterator<E> = elementIterator()
    }

    private inner class MutableEntrySet : Multisets.AbstractMutableEntrySet<E>() {
        override fun multiset(): MutableMultiset<E> = this@AbstractMutableMultiset
        override operator fun iterator(): MutableIterator<Multiset.Entry<E>> = entryIterator()
        override val size: Int get() = distinctElements()
    }
}
