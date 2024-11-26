package com.hal.stunner.message.header

import com.hal.stunner.helper.toByteArray
import com.hal.stunner.message.StunMessageType
import com.hal.stunner.message.StunParseException
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StunHeaderParserTest {
    @Test
    fun testParse_throwsException_tooFewBytes() {
        val bytes = byteArrayOf()
        val parser = StunHeaderParser()
        val exception = assertThrows<StunParseException> {
            parser.parse(bytes)
        }
        assertContains(exception.message!!,"Stun message must be at least 20 bytes")
    }

    @Test
    fun testParse_throwsException_firstTwoBitsnNonzero() {
        val bytes = ByteArray(20)
        bytes[0] = 0b1100000
        val parser = StunHeaderParser()
        val exception = assertThrows<StunParseException> {
            parser.parse(bytes)
        }
        assertContains(exception.message!!,"First two bits must be 0")
    }

    @Test
    fun testParse_throwsException_unrecognizedType() {
        val bytes = ByteArray(20)
        val typeBytes = 0x001f.toByteArray(2)
        bytes[0] = typeBytes[0]
        bytes[1] = typeBytes[1]
        val parser = StunHeaderParser()
        val exception = assertThrows<StunParseException> {
            parser.parse(bytes)
        }
        assertContains(exception.message!!,"Unrecognized message type for value 0x0000001f")
    }

    @Test
    fun testParse_throwsException_invcorrectMessageLength() {
        val bytes = ByteArray(21)

        val typeBytes = 0x0001.toByteArray(2)
        bytes[0] = typeBytes[0]
        bytes[1] = typeBytes[1]

        val parser = StunHeaderParser()
        val exception = assertThrows<StunParseException> {
            parser.parse(bytes)
        }
        assertContains(exception.message!!,"Incorrect value of message length")
    }

    @Test
    fun testParse_throwsException_invalidMagicCookie() {
        val bytes = ByteArray(21)
        val typeBytes = 0x0001.toByteArray(2)
        bytes[0] = typeBytes[0]
        bytes[1] = typeBytes[1]

        bytes[3] = 0b1

        val parser = StunHeaderParser()
        val exception = assertThrows<StunParseException> {
            parser.parse(bytes)
        }
        assertContains(exception.message!!,"Incorrect magic cookie value")
    }

    @Test
    fun testParse() {
        val bytes = ByteArray(21)
        val typeBytes = 0x0001.toByteArray(2)
        typeBytes.copyInto(bytes, 0)

        bytes[3] = 0b1

        val magicCookieBytes = StunHeader.MAGIC_COOKIE.toByteArray(4)
        magicCookieBytes.copyInto(bytes, 4)

        val txnIdBytes = 0x120012.toByteArray(12)
        txnIdBytes.copyInto(bytes, 8)

        val parser = StunHeaderParser()

        val message = parser.parse(bytes)

        val expected = StunHeader(
            type = StunMessageType.BINDING_REQUEST,
            messageLengthBytes = 1,
            magicCookie = StunHeader.MAGIC_COOKIE,
            transactionId = txnIdBytes
        )
        assertEquals(expected, message)
    }
}