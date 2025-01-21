package message.attribute.value

import com.hal.stunner.message.attribute.StunAttributeParseException
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValueParser
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StunAttributeValueParserFactoryTest {
    @Test
    fun testGet_returnsMappedAddressStunAttributeValueParser() {
        val mockMappedAddressStunAttributeValueParser = mock<MappedAddressStunAttributeValueParser>()

        val factory = StunAttributeValueParserFactory(
            mappedAddressStunAttributeValueParser = mockMappedAddressStunAttributeValueParser,
            fingerprintStunAttributeValueParser = mock()
        )

        val attributeValueParser = factory.get(StunAttributeType.MAPPED_ADDRESS)

        assertEquals(
            mockMappedAddressStunAttributeValueParser,
            attributeValueParser
        )
    }

    @Test
    fun testGet_throwsUnsupportedAttributeType() {
        val mockMappedAddressStunAttributeValueParser = mock<MappedAddressStunAttributeValueParser>()

        val factory = StunAttributeValueParserFactory(
            mappedAddressStunAttributeValueParser = mockMappedAddressStunAttributeValueParser,
            fingerprintStunAttributeValueParser = mock()
        )

        val thrown = assertThrows<StunAttributeParseException> {
            factory.get(StunAttributeType.ALTERNATE_SERVER)
        }
        assertContains(thrown.message!!, "Unsupported attribute type")
    }
}