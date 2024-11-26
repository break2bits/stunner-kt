package com.hal.stunner.message.attribute.value

import com.hal.stunner.message.StunParseException
import com.hal.stunner.message.attribute.StunAttributeType

class StunAttributeValueParserFactory(
    private val mappedAddressStunAttributeValueParser: MappedAddressStunAttributeValueParser
) {

    fun get(attributeType: StunAttributeType): StunAttributeValueParser {
        when (attributeType) {
            StunAttributeType.MAPPED_ADDRESS -> return mappedAddressStunAttributeValueParser
            else -> throw StunParseException("Unsupported attribute type $attributeType")
        }
    }
}