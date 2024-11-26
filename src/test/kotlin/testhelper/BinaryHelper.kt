package com.hal.stunner.helper

// big-endian
fun Int.toByteArray(numBytes: Int): ByteArray {
    var shiftedNumber = this
    val byteArray = ByteArray(numBytes)
    for (i in byteArray.size - 1 downTo 0) {
        byteArray[i] = shiftedNumber.toByte() // get lowest 8 bits
        shiftedNumber = shiftedNumber ushr 8 // shift right
    }
    return byteArray
}