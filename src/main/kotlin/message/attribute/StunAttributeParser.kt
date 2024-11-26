package com.hal.stunner.message.attribute

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.message.attribute.StunAttribute.Companion.ATTRIBUTE_TYPE_SIZE_BYTES
import com.hal.stunner.message.attribute.StunAttribute.Companion.ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory
import com.hal.stunner.print.toPrettyHexString

class StunAttributeParser(private val attributeValueParserFactory: StunAttributeValueParserFactory) {
    fun parse(bytes: ByteArray, offset: Int): StunAttribute {
        val attributeTypeValue = BinaryHelper.getBytesAsInt(bytes, offset, ATTRIBUTE_TYPE_SIZE_BYTES)
        val attributeType = StunAttributeType.fromValue(attributeTypeValue)
        if (attributeType == null) {
            throw StunAttributeParseException("Unknown attribute type with value ${attributeTypeValue.toPrettyHexString()}")
        }
        val attributeValueLengthBytes = BinaryHelper.getBytesAsInt(bytes, offset + ATTRIBUTE_TYPE_SIZE_BYTES, ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES)

        val attributeValueParser = attributeValueParserFactory.get(attributeType)
        val attributeValue = attributeValueParser.parse(bytes, offset + ATTRIBUTE_TYPE_SIZE_BYTES + ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES, attributeValueLengthBytes)
        return StunAttribute(
            type = attributeType,
            valueLengthBytes = attributeValueLengthBytes,
            value = attributeValue
        )
    }
}