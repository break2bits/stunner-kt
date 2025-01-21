package com.hal.stunner.handler

import com.hal.stunner.message.StunMessage
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValue

class FingerprintAttributeValueValidator(
    private val fingerprintCalculator: FingerprintCalculator
) {

    fun maybeValidate(message: StunMessage, rawBytes: ByteArray): Boolean {
        if (!message.hasAttributeOfType(StunAttributeType.FINGERPRINT)) {
            return false
        }
        validate(message, rawBytes)
        return true
    }

    private fun validate(message: StunMessage, rawBytes: ByteArray) {
        val fingerprintAttribute = message.getAttributeOfType(StunAttributeType.FINGERPRINT)!!
        if (message.attributes.last() != fingerprintAttribute) {
            throw StunAttributeValidationException("Fingerprint attribute not last attribute in message")
        }

        // get bytes of message up to but not including the fingerprint attribute
        val bytesNotIncludingFingerprint = rawBytes.sliceArray(IntRange(0, rawBytes.size - fingerprintAttribute.getTotalLengthBytes()))
        val expectedFingerprintValue = fingerprintAttribute.value as FingerprintStunAttributeValue
        val actualFingerprintValue = fingerprintCalculator.calculateFingerprint(bytesNotIncludingFingerprint)
        if (expectedFingerprintValue.crc != actualFingerprintValue) {
            throw StunAttributeValidationException("Fingerprint values do not match")
        }
    }
}