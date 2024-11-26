package com.hal.stunner.message.attribute

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.message.StunParseException
import com.hal.stunner.message.attribute.StunAttribute.Companion.ATTRIBUTE_TYPE_SIZE_BYTES
import com.hal.stunner.message.attribute.StunAttribute.Companion.ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory

class StunAttributeParser(private val attributeValueParserFactory: StunAttributeValueParserFactory) {
    @OptIn(ExperimentalStdlibApi::class)
    fun parse(bytes: ByteArray, offset: Int): StunAttribute {
        val attributeTypeValue = BinaryHelper.getBytesAsInt(bytes, offset, ATTRIBUTE_TYPE_SIZE_BYTES)
        val attributeType = StunAttributeType.fromValue(attributeTypeValue)
        if (attributeType == null) {
            throw StunParseException("uknown attribute type with value ${attributeTypeValue.toHexString()}")
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