package com.hal.stunner.binary

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