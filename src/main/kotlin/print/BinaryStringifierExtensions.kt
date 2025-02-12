package com.hal.stunner.print

private const val ZERO = '0'
private const val ONE = '1'

// prints lower N digits in 0b format
fun Int.toPrettyBinaryString(bits: Int): String {
    var thisShifted = this
    val builder = StringBuilder(bits + 2)
    repeat(bits) {
        if (thisShifted % 2 == 0) {
            builder.append(ZERO)
        } else {
            builder.append(ONE)
        }
        thisShifted = thisShifted ushr 1
    }
    builder.append('b')
    builder.append('0')
    return builder.reverse().toString()
}


@OptIn(ExperimentalStdlibApi::class)
fun Int.toPrettyHexString(): String {
    return "0x" + this.toHexString()
}