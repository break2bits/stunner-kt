package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttributeListParser
import com.hal.stunner.message.header.StunHeader
import com.hal.stunner.message.header.StunHeaderParser
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.net.DatagramPacket
import java.net.InetAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class StunMessageParserTest {

    @Test
    fun testParse() {
        val packetBytes = byteArrayOf()
        val messageLengthBytes = 1
        val expectedHeader = StunHeader(
            type = StunMessageType.BINDING_REQUEST,
            messageLengthBytes = messageLengthBytes,
            magicCookie = 0,
            transactionId = byteArrayOf()
        )
        val mockStunHeaderParser = mock<StunHeaderParser>()
        whenever(mockStunHeaderParser.parse(packetBytes)).thenReturn(expectedHeader)
        val mockAttributeListParser = mock<StunAttributeListParser>()
        whenever(mockAttributeListParser.parse(packetBytes, messageLengthBytes)).thenReturn(emptyList())

        val ipAddress = InetAddress.ofLiteral("127.0.0.1")
        val packet = DatagramPacket(packetBytes, 0, 0, ipAddress, 80)

        val parser = StunMessageParser(
            mockStunHeaderParser,
            mockAttributeListParser
        )

        val request = parser.parse(packet)
        val expected = StunRequest(
            metadata = StunMetadata(
                ip = ipAddress,
                port = 80
            ),
            message = StunMessage(
                header = expectedHeader,
                attributes = emptyList(),
            ),
            rawBytes = byteArrayOf()
        )

        assertEquals(request, expected)
    }
}