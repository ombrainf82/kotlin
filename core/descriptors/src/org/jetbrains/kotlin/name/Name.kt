/*
 * Copyright 2010-2015 JetBrains s.r.o.
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
 */

package org.jetbrains.kotlin.name

class Name private constructor(private val name: String, val isSpecial: Boolean) : Comparable<Name> {

    val identifier: String
        get() {
            if (isSpecial) {
                throw IllegalStateException("not identifier: $this")
            }
            return asString()
        }

    fun asString(): String = name

    override fun compareTo(other: Name): Int = name.compareTo(other.name)

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Name) return false

        if (isSpecial != other.isSpecial) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (isSpecial) 1 else 0
        return result
    }

    companion object {

        @JvmStatic
        fun identifier(name: String): Name {
            return Name(name, false)
        }

        @JvmStatic
        fun isValidIdentifier(name: String): Boolean {
            if (name.isEmpty() || name.startsWith("<")) return false
            for (i in 0 until name.length) {
                val ch = name[i]
                if (ch == '.' || ch == '/' || ch == '\\') {
                    return false
                }
            }

            return true
        }

        @JvmStatic
        fun special(name: String): Name {
            if (!name.startsWith("<")) {
                throw IllegalArgumentException("special name must start with '<': $name")
            }
            return Name(name, true)
        }

        @JvmStatic
        fun guessByFirstCharacter(name: String): Name {
            return if (name.startsWith("<")) special(name) else identifier(name)
        }
    }
}
