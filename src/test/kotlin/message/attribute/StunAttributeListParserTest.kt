package com.hal.stunner.message.attribute

import com.hal.stunner.message.attribute.value.StunAttributeValue
import com.hal.stunner.message.header.StunHeader
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StunAttributeListParserTest {
    @Test
    fun testParse_zeroLength() {
        val mockAttributeParser = mock<StunAttributeParser>()

        val parser = StunAttributeListParser(mockAttributeParser)

        val attributes =parser.parse(byteArrayOf(), 0)

        assertEquals(0, attributes.size)
    }

    @Test
    fun testParse() {
        val mockAttributeParser = mock<StunAttributeParser>()
        val bytes = byteArrayOf()
        val firstAttribute = StunAttribute(
            type = StunAttributeType.MAPPED_ADDRESS,
            valueLengthBytes = 4,
            value = object : StunAttributeValue {}
        )
        whenever(mockAttributeParser.parse(bytes, StunHeader.SIZE_BYTES)).thenReturn(firstAttribute)

        val secondAttribute = StunAttribute(
            type = StunAttributeType.XOR_MAPPED_ADDRESS,
            valueLengthBytes = 4,
            value = object : StunAttributeValue {}
        )
        whenever(mockAttributeParser.parse(bytes, StunHeader.SIZE_BYTES + 8)).thenReturn(secondAttribute)

        val parser = StunAttributeListParser(mockAttributeParser)

        val attributes = parser.parse(bytes, 12)

        assertContentEquals(listOf(firstAttribute, secondAttribute), attributes)
    }
}