package com.hal.stunner.message

import com.hal.stunner.binary.toByteArray
import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.StunAttributeValue
import com.hal.stunner.message.header.StunHeader
import java.net.DatagramPacket
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StunMessageSerializerTest {

    @Test
    fun testSerialize() {
        val firstAttribute = StunAttribute(
            type = StunAttributeType.XOR_MAPPED_ADDRESS,
            valueLengthBytes = 1,
            value = object : StunAttributeValue {
                override fun writeBytes(buffer: ByteBuffer) {
                    buffer.put(0x31)
                }
            }
        )
        val secondAttribute = StunAttribute(
            type = StunAttributeType.MESSAGE_INTEGRITY,
            valueLengthBytes = 1,
            value = object : StunAttributeValue {
                override fun writeBytes(buffer: ByteBuffer) {
                    buffer.put(0x41)
                }
            }
        )
        val transactionId = byteArrayOf(
            0x01,
            0x02,
            0x03,
            0x04,
            0x05,
            0x06,
            0x07,
            0x08,
            0x09,
            0x0a,
            0x0b,
            0x0c
        )

        val message = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_RESPONSE,
                messageLengthBytes = 10,
                magicCookie = StunHeader.MAGIC_COOKIE,
                transactionId = transactionId
            ),
            attributes = listOf(
                firstAttribute,
                secondAttribute
            )
        )

        val serializer = StunMessageSerializer()

        val output = serializer.serialize(message)

        val expectedOutputBytes = ByteArray(30)
        StunMessageType.BINDING_RESPONSE.value.toShort().toByteArray().copyInto(expectedOutputBytes)
        expectedOutputBytes[3] = message.header.messageLengthBytes.toByte()
        StunHeader.MAGIC_COOKIE.toByteArray().copyInto(expectedOutputBytes, 4)
        transactionId.copyInto(expectedOutputBytes, 8)
        expectedOutputBytes[21] = StunAttributeType.XOR_MAPPED_ADDRESS.value.toByte()
        expectedOutputBytes[23] = 1
        expectedOutputBytes[24] = 0x31
        expectedOutputBytes[26] = StunAttributeType.MESSAGE_INTEGRITY.value.toByte()
        expectedOutputBytes[28] = 1
        expectedOutputBytes[29] = 0x41

        assertContentEquals(expectedOutputBytes, output.data)
        assertEquals(expectedOutputBytes.size, output.length)
    }
}