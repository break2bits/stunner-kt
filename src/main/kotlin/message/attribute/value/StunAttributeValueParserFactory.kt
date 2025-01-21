package com.hal.stunner.message.attribute.value

import com.hal.stunner.message.attribute.StunAttributeParseException
import com.hal.stunner.message.attribute.StunAttributeType

class StunAttributeValueParserFactory(
    private val mappedAddressStunAttributeValueParser: MappedAddressStunAttributeValueParser,
    private val fingerprintStunAttributeValueParser: FingerprintStunAttributeValueParser
) {

    fun get(attributeType: StunAttributeType): StunAttributeValueParser {
        when (attributeType) {
            StunAttributeType.MAPPED_ADDRESS -> return mappedAddressStunAttributeValueParser
            StunAttributeType.FINGERPRINT -> return fingerprintStunAttributeValueParser
            else -> throw StunAttributeParseException("Unsupported attribute type $attributeType")
        }
    }
}