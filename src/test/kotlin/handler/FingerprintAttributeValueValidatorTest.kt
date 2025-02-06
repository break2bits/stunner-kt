package handler

import com.hal.stunner.handler.FingerprintAttributeValueValidator
import com.hal.stunner.handler.FingerprintCalculator
import com.hal.stunner.handler.StunAttributeValidationException
import com.hal.stunner.message.StunMessage
import com.hal.stunner.message.StunMessageType
import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValue
import com.hal.stunner.message.header.StunHeader
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse

class FingerprintAttributeValueValidatorTest {

    @Test
    fun testShouldValidate_noFingerprint() {
        val message = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_REQUEST,
                messageLengthBytes = 10,
                transactionId = byteArrayOf(),
                magicCookie = StunHeader.MAGIC_COOKIE
            ),
            attributes = listOf()
        )
        val mockFingerprintCalculator = mock<FingerprintCalculator>()

        val validator = FingerprintAttributeValueValidator(
            mockFingerprintCalculator
        )

        val shouldValidate = validator.shouldValidate(message)

        assertFalse(shouldValidate)
    }

    @Test
    fun testValidate_fails_fingerprintNotLast() {
        val message = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_REQUEST,
                messageLengthBytes = 10,
                transactionId = byteArrayOf(),
                magicCookie = StunHeader.MAGIC_COOKIE
            ),
            attributes = listOf(
                StunAttribute(
                    type = StunAttributeType.FINGERPRINT,
                    valueLengthBytes = 0,
                    value = {  }
                ),
                StunAttribute(
                    type = StunAttributeType.XOR_MAPPED_ADDRESS,
                    valueLengthBytes = 0,
                    value = {  }
                )
            )
        )
        val mockFingerprintCalculator = mock<FingerprintCalculator>()

        val validator = FingerprintAttributeValueValidator(
            mockFingerprintCalculator
        )
        val rawBytes = byteArrayOf()

        val thrown = assertThrows<StunAttributeValidationException> {
            validator.validate(message, rawBytes)
        }
        assertContains(thrown.message!!, "not last attribute")
    }

    @Test
    fun testValidate_fails_valueMismatch() {
        val message = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_REQUEST,
                messageLengthBytes = 10,
                transactionId = byteArrayOf(),
                magicCookie = StunHeader.MAGIC_COOKIE
            ),
            attributes = listOf(
                StunAttribute(
                    type = StunAttributeType.FINGERPRINT,
                    valueLengthBytes = 0,
                    value = FingerprintStunAttributeValue(
                        crc = 20
                    )
                )
            )
        )
        val mockFingerprintCalculator = mock<FingerprintCalculator>()

        val validator = FingerprintAttributeValueValidator(
            mockFingerprintCalculator
        )
        val rawBytes = byteArrayOf()
        whenever(mockFingerprintCalculator.calculateFingerprint(rawBytes)).thenReturn(32)

        val thrown = assertThrows<StunAttributeValidationException> {
            validator.validate(message, rawBytes)
        }
        assertContains(thrown.message!!, "values do not match")
    }


    @Test
    fun testValidate() {
        val message = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_REQUEST,
                messageLengthBytes = 10,
                transactionId = byteArrayOf(),
                magicCookie = StunHeader.MAGIC_COOKIE
            ),
            attributes = listOf(
                StunAttribute(
                    type = StunAttributeType.FINGERPRINT,
                    valueLengthBytes = 0,
                    value = FingerprintStunAttributeValue(
                        crc = 32
                    )
                )
            )
        )
        val mockFingerprintCalculator = mock<FingerprintCalculator>()

        val validator = FingerprintAttributeValueValidator(
            mockFingerprintCalculator
        )
        val rawBytes = byteArrayOf()
        whenever(mockFingerprintCalculator.calculateFingerprint(rawBytes)).thenReturn(32)

        validator.validate(message, rawBytes)
    }

}