package com.hal.stunner.binary

class BinaryHelper {
    companion object {
        fun getBytesAsInt(bytes: ByteArray, start: Int, numBytes: Int): Int {
            var accumulator = 0
            for (i in start..(start + numBytes)) {
                val byte = bytes[i]
                accumulator = accumulator or byte.toInt()
                accumulator shl 8
            }
            return accumulator
        }
    }
}