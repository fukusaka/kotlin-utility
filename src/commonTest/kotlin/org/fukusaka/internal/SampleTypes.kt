package org.fukusaka.internal

class Sample01Exception : Exception()

class Sample01(val value01: Int = 123) {
    override fun toString(): String {
        throw Sample01Exception()
    }
}
