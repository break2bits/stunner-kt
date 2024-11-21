package com.hal.stunner.print

// 0b1
// 0b01
// 0b10
// 0b11

private const val ZERO = '0'
private const val ONE = '1'

// prints lower N digits in 0b format
fun Int.toPrettyBinaryString(bits: kotlin.Int): String {
    var thisShifted = this
    val builder = StringBuilder(bits + 2)
    repeat(bits) {
        if (this % 2 == 0) {
            builder.append(ZERO)
        } else {
            builder.append(ONE)
        }
        thisShifted = thisShifted shr 1
    }
    builder.append('b')
    builder.append('0')
    return builder.reverse().toString()
}