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

interface Multiset<E> : Collection<E> {
    val elementSet: Set<E>
    val entrySet: Set<Entry<E>>

    fun count(element: E): Int

    interface Entry<E> {
        val element: E;
        val count: Int
    }
}

interface MutableMultiset<E> : Multiset<E>, MutableCollection<E> {
    override val elementSet: MutableSet<E>
    override val entrySet: MutableSet<Multiset.Entry<E>>

    fun add(element: E, occurrences: Int): Int
    fun remove(element: E, occurrences: Int): Int
    fun setCount(element: E, count: Int): Int
    fun setCount(element: E, oldCount: Int, newCount: Int): Boolean

    interface MutableEntry<E> : Multiset.Entry<E> {
        override var element: E
        override var count: Int
    }
}
