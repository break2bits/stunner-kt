package com.hal.stunner.message.attribute

import com.hal.stunner.message.StunParseException
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValueParser
import com.hal.stunner.message.attribute.value.StunAttributeValueParser

class StunAttributeValueParserFactory {
    fun get(attributeType: StunAttributeType): StunAttributeValueParser {
        when (attributeType) {
            StunAttributeType.MAPPED_ADDRESS -> return MappedAddressStunAttributeValueParser(bytes, attributeValueOffset)
            else -> throw StunParseException("Unsupported attribute type $attributeType")
        }
    }
}