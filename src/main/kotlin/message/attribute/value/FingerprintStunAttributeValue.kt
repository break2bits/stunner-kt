package com.hal.stunner.message.attribute.value

import java.nio.ByteBuffer

data class FingerprintStunAttributeValue(
    val crc: Int
) : StunAttributeValue {
    companion object {
        const val CRC32_LENGTH_BYTES = 4
        const val CRC32_XOR_VALUE: Int = 0x5354554e
    }

    override fun writeBytes(buffer: ByteBuffer) {
        buffer.putInt(crc)
    }
}