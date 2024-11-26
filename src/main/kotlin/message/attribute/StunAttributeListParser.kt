package com.hal.stunner.message.attribute

import com.hal.stunner.message.header.StunHeader

class StunAttributeListParser(private val attributeParser: StunAttributeParser) {
    fun parse(bytes: ByteArray, lengthBytes: Int): List<StunAttribute> {
        if (lengthBytes == 0) {
            return emptyList()
        }

        val attributeList = mutableListOf<StunAttribute>()
        var consumed = 0
        while (consumed < lengthBytes) {
            val attribute = attributeParser.parse(bytes, consumed + StunHeader.SIZE_BYTES)
            consumed += attribute.getTotalLengthBytes()
            attributeList.add(attribute)
        }

        return attributeList
    }
}