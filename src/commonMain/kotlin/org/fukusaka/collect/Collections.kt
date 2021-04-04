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

fun <E> multisetOf(): Multiset<E> = HashMultiset()

fun <E> multisetOf(vararg elements: E): Multiset<E> {
    return if (elements.isEmpty()) HashMultiset() else HashMultiset(elements.asIterable())
}

fun <E> mutableMultisetOf(): MutableMultiset<E> = HashMultiset()

fun <E> mutableMultisetOf(vararg elements: E): MutableMultiset<E> {
    return if (elements.isEmpty()) HashMultiset() else HashMultiset(elements.asIterable())
}
