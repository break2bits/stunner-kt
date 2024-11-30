package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.header.StunHeader

class StunMessageBuilder private constructor(
    private val transactionId: ByteArray,
) {
    private val type = StunMessageType.BINDING_RESPONSE
    private val attributes = mutableListOf<StunAttribute>()

    companion object {
        fun fromTransactionId(transactionId: ByteArray): StunMessageBuilder {
            return StunMessageBuilder(transactionId)
        }
    }

    fun build(): StunMessage {
        return StunMessage(
            header = StunHeader(
                type = type,
                messageLengthBytes = computeMessageLengthBytes(),
                magicCookie = StunHeader.MAGIC_COOKIE,
                transactionId = transactionId
            ),
            attributes = attributes
        )
    }

    fun addAttribute(attribute: StunAttribute): StunMessageBuilder {
        attributes.add(attribute)
        return this
    }

    private fun computeMessageLengthBytes(): Int {
        return StunHeader.SIZE_BYTES + computeAttributesLengthBytes()
    }

    private fun computeAttributesLengthBytes(): Int {
        return attributes.sumOf {
            it.getTotalLengthBytes()
        }
    }
}