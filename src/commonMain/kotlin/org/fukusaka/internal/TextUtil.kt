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

package org.fukusaka.internal

internal fun String.lenientFormat(vararg args: Any?): String =
    buildString(this.length + 16 * args.size) {
        val template = this@lenientFormat
        var index = 0

        var templateStart = 0
        while (index < args.size) {
            val placeholderStart = template.indexOf("%s", templateStart)
            if (placeholderStart < 0) break
            append(template, templateStart, placeholderStart)
            append(args[index++].lenientToString())
            templateStart = placeholderStart + 2
        }
        append(template, templateStart, template.length)

        if (index >= args.size) return@buildString

        append(" [")
        while (true) {
            append(args[index++].lenientToString())
            if (index >= args.size) break
            append(", ")
        }
        append(']')
    }
