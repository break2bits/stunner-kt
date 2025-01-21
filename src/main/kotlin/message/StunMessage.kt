package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.header.StunHeader

data class StunMessage(
    val header: StunHeader,
    val attributes: List<StunAttribute>
) {
    fun getTotalLengthBytes(): Int {
        return StunHeader.SIZE_BYTES + header.messageLengthBytes
    }

    fun hasAttributeOfType(type: StunAttributeType): Boolean {
        return getAttributeOfType(type) != null
    }

    fun getAttributeOfType(type: StunAttributeType): StunAttribute? {
        return attributes.find { it.type == type }
    }
}