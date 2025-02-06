package com.hal.stunner.handler

import com.hal.stunner.message.StunMessage
import com.hal.stunner.message.StunMessageSerializer
import com.hal.stunner.message.StunMessageType
import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValue
import com.hal.stunner.message.header.StunHeader

class StunMessageBuilder(
    private val stunMessageSerializer: StunMessageSerializer,
    private val fingerprintCalculator: FingerprintCalculator,
    private val transactionId: ByteArray,
) {
    private val type = StunMessageType.BINDING_RESPONSE
    private val attributes = mutableListOf<StunAttribute>()
    private var addFingerprint = false

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is StunMessageBuilder) {
            return false
        }

        return other.stunMessageSerializer == stunMessageSerializer
                && other.fingerprintCalculator == fingerprintCalculator
                && transactionId.contentEquals(other.transactionId)
    }

    fun build(): StunMessage {
        val partialMessage = StunMessage(
            header = StunHeader(
                type = type,
                messageLengthBytes = computeAttributesLengthBytes(attributes),
                magicCookie = StunHeader.MAGIC_COOKIE,
                transactionId = transactionId
            ),
            attributes = attributes
        )
        val message: StunMessage
        if (addFingerprint) {
            val bytes = stunMessageSerializer.serializeBytes(partialMessage)
            val attributesWithFingerprint = attributes + buildFingerprint(bytes)
            message = StunMessage(
                header = StunHeader(
                    type = type,
                    messageLengthBytes = computeAttributesLengthBytes(attributesWithFingerprint),
                    magicCookie = StunHeader.MAGIC_COOKIE,
                    transactionId = transactionId
                ),
                attributes = attributesWithFingerprint
            )
        } else {
            message = partialMessage
        }
        return message
    }

    fun addAttribute(attribute: StunAttribute): StunMessageBuilder {
        attributes.add(attribute)
        return this
    }

    fun withFingerprint(): StunMessageBuilder {
        addFingerprint = true
        return this
    }

    private fun computeAttributesLengthBytes(messageAttributes: List<StunAttribute>): Int {
        return messageAttributes.sumOf {
            it.getTotalLengthBytes()
        }
    }

    private fun buildFingerprint(partialMessageBytes: ByteArray): StunAttribute {
        val fingerprintValueCrc32 = fingerprintCalculator.calculateFingerprint(partialMessageBytes)
        val fingerprintStunAttributeValue = FingerprintStunAttributeValue(fingerprintValueCrc32)
        return StunAttribute(
            type = StunAttributeType.FINGERPRINT,
            valueLengthBytes = FingerprintStunAttributeValue.CRC32_LENGTH_BYTES,
            fingerprintStunAttributeValue
        )
    }
}