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
 * Based on https://github.com/JetBrains/kotlin-native licensed under the Apache License 2.0
 */

package org.fukusaka.internal

import kotlin.native.identityHashCode

internal actual fun Any?.lenientToString(): String {
    this ?: return "null"

    return try {
        toString()
    } catch (e: Exception) {
        // Default toString() behavior - see Any.toString()
        val className = this::class.qualifiedName ?: this::class.simpleName ?: "<object>"
        val unsignedHashCode = this.identityHashCode().toLong() and 0xffffffffL
        val hashCodeStr = unsignedHashCode.toString(16)
        val exceptionClassName: String = e::class.qualifiedName ?: e::class.simpleName ?: "<unknown>"

        // TODO: If there is a good Logger implementation, it will output a warning log.

        "<$className@$hashCodeStr threw $exceptionClassName>"
    }
}
