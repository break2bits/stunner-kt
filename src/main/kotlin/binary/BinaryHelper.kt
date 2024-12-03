package com.hal.stunner.binary

import java.nio.ByteBuffer
import kotlin.experimental.xor

class BinaryHelper {
    companion object {
        fun getBytesAsInt(bytes: ByteArray, start: Int, numBytes: Int): Int {
            var accumulator = 0.toUInt()
            for (i in start..<(start + numBytes)) {
                val byte = bytes[i]
                accumulator = accumulator shl 8
                accumulator = accumulator or byte.toUByte().toUInt()
            }
            return accumulator.toInt()
        }
    }
}

fun Int.toByteArray(): ByteArray {
    return ByteBuffer.allocate(4).putInt(this).array()
}

fun Short.toByteArray(): ByteArray {
    return ByteBuffer.allocate(2).putShort(this).array()
}

infix fun ByteArray.xor(other: ByteArray): ByteArray {
    if (other.size < size) {
        throw IllegalArgumentException("Second array in xor must be at least size of first")
    }
    val result = ByteArray(size)
    forEachIndexed { index, byte ->
        result[index] = byte xor other[index]
    }
    return result
}