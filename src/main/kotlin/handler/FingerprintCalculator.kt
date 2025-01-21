package com.hal.stunner.handler

import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValue
import java.util.zip.CRC32

class FingerprintCalculator {
    fun calculateFingerprint(byteArray: ByteArray): Int {
        val crc32 = CRC32()
        crc32.update(byteArray)
        return (crc32.value xor FingerprintStunAttributeValue.CRC32_XOR_VALUE.toLong()).toInt()
    }
}