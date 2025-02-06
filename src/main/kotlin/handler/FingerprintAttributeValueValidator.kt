package com.hal.stunner.handler

import com.hal.stunner.message.StunMessage
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValue

class FingerprintAttributeValueValidator(
    private val fingerprintCalculator: FingerprintCalculator
) {

    fun shouldValidate(message: StunMessage): Boolean {
        return message.hasAttributeOfType(StunAttributeType.FINGERPRINT)
    }

    // returns whether or not the fingerprint was present, *NOT* whether or not it was valid
    fun validate(message: StunMessage, rawBytes: ByteArray) {
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