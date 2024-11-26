package com.hal.stunner.message.attribute

import com.hal.stunner.message.StunHeader

class StunAttributeListParser(private val attributeParser: StunAttributeParser) {
    fun parse(header: StunHeader, bytes: ByteArray): List<StunAttribute> {
        if (header.length == 0) {
            return emptyList()
        }

        val attributeList = mutableListOf<StunAttribute>()
        var consumed = 0
        while (consumed < header.length) {
            val attribute = attributeParser.parse(bytes, consumed + StunHeader.SIZE_BYTES)
            consumed += attribute.getTotalLengthBytes()
            attributeList.add(attribute)
        }

        return attributeList
    }
}