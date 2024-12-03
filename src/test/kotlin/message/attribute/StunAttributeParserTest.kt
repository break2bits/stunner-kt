package com.hal.stunner.message.attribute

import com.hal.stunner.message.attribute.value.StunAttributeValueParser
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StunAttributeParserTest {
    @Test
    fun parse_invalidAttributeType() {
        val bytes = byteArrayOf(0x00, 0x00, 0x0c)
        val mockAttributeValueParserFactory = mock<StunAttributeValueParserFactory>()

        val parser = StunAttributeParser(mockAttributeValueParserFactory)

        val thrown = assertThrows<StunAttributeParseException> {
            parser.parse(bytes, 1)
        }
        assertContains(thrown.message!!, "Unknown attribute type")
    }

    @Test
    fun parse() {
        val attributeValueLengthBytes: Byte = 5
        val bytes = byteArrayOf(0x00, 0x00, 0x01, 0x00, attributeValueLengthBytes)
        val attributeValue = {_: ByteBuffer -> }

        val mockStunAttributeValueParser = mock<StunAttributeValueParser>()
        whenever(mockStunAttributeValueParser.parse(bytes, 5, attributeValueLengthBytes.toInt())).thenReturn(attributeValue)

        val mockAttributeValueParserFactory = mock<StunAttributeValueParserFactory>()
        whenever(mockAttributeValueParserFactory.get(StunAttributeType.MAPPED_ADDRESS)).thenReturn(mockStunAttributeValueParser)

        val parser = StunAttributeParser(mockAttributeValueParserFactory)

        val attribute = parser.parse(bytes, 1)

        val expectedAttribute = StunAttribute(
            type = StunAttributeType.MAPPED_ADDRESS,
            valueLengthBytes = attributeValueLengthBytes.toInt(),
            value = attributeValue
        )
        assertEquals(expectedAttribute, attribute)
    }
}