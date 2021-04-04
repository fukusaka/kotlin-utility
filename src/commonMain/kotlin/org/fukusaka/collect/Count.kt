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

data class Count(private var value: Int) {

    fun get(): Int = value

    fun set(newValue: Int) {
        value = newValue
    }

    fun add(delta: Int) {
        set(get() + delta)
    }

    fun addAndGet(delta: Int): Int {
        add(delta)
        return get()
    }

    fun getAndSet(newValue: Int): Int =
        get().also { set(newValue) }

    override fun toString(): String = value.toString()
}