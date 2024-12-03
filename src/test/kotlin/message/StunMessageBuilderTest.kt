package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.header.StunHeader
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

class StunMessageBuilderTest {

    @Test
    fun testBuild() {
        val txnId = byteArrayOf(0x0a)
        val firstAttribute = StunAttribute(
            type = StunAttributeType.XOR_MAPPED_ADDRESS,
            valueLengthBytes = 1,
            value = {_: ByteBuffer -> }
        )
        val secondAttribute = StunAttribute(
            type = StunAttributeType.MESSAGE_INTEGRITY,
            valueLengthBytes = 1,
            value = {_: ByteBuffer -> }
        )
        val expectedMessage = StunMessage(
            header = StunHeader(
                type = StunMessageType.BINDING_RESPONSE,
                messageLengthBytes = 10,
                magicCookie = StunHeader.MAGIC_COOKIE,
                transactionId = txnId
            ),
            attributes = listOf(firstAttribute, secondAttribute)
        )

        val builder = StunMessageBuilder.fromTransactionId(txnId)
        builder.addAttribute(firstAttribute)
        builder.addAttribute(secondAttribute)
        val message = builder.build()

        assertEquals(
            expectedMessage,
            message
        )
    }
}