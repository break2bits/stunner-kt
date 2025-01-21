package com.hal.stunner.message.attribute.value

import com.hal.stunner.binary.BinaryHelper

class FingerprintStunAttributeValueParser : StunAttributeValueParser {
    override fun parse(byteArray: ByteArray, offset: Int, lengthBytes: Int): StunAttributeValue {
        val crc32 = BinaryHelper.getBytesAsInt(byteArray, offset, FingerprintStunAttributeValue.CRC32_LENGTH_BYTES)
        return FingerprintStunAttributeValue(crc32)
    }
}