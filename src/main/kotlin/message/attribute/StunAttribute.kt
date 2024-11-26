package com.hal.stunner.message.attribute

import com.hal.stunner.message.attribute.value.StunAttributeValue

data class StunAttribute(
    val type: StunAttributeType,
    val valueLengthBytes: Int,
    val value: StunAttributeValue
) {
    companion object {
        const val ATTRIBUTE_TYPE_SIZE_BYTES = 2
        const val ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES = 2
    }

    fun getTotalLengthBytes(): Int {
        return valueLengthBytes + ATTRIBUTE_TYPE_SIZE_BYTES + ATTRIBUTE_VALUE_LENGTH_SIZE_BYTES
    }
}