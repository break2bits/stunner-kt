package message.attribute.value

import com.hal.stunner.helper.toByteArray
import com.hal.stunner.message.attribute.IpAddressFamily
import com.hal.stunner.message.attribute.StunAttributeParseException
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValue
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValueParser
import org.junit.jupiter.api.assertThrows
import java.net.Inet4Address
import java.net.InetAddress
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class MappedStunAttributeValueParserTest {
    @Test
    fun testParse_unrecognizedAddressFamily_Error() {
        val bytes = byteArrayOf(0x11, 0x00, 0x03)
        val offset = 1
        val lengthBytes = 2
        val parser = MappedAddressStunAttributeValueParser()

        val thrown = assertThrows<StunAttributeParseException> {
            parser.parse(bytes, offset, lengthBytes)
        }
        assertContains(thrown.message!!, "Unrecognized ip address family")
    }

    @Test
    fun testParse_ipv4Address() {
        val bytes = ByteArray(9)
        bytes[2] = 0x1 // family

        val portBytes = 8080.toByteArray(2)
        portBytes.copyInto(bytes, 3)

        val expectedAddress = InetAddress.ofLiteral("127.0.0.1")
        expectedAddress.address.copyInto(bytes, 5)
        val parser = MappedAddressStunAttributeValueParser()

        val attributeValue = parser.parse(bytes, 1, 8)

        assertEquals(
            MappedAddressStunAttributeValue(
                family = IpAddressFamily.IPV4,
                port = 8080,
                address = expectedAddress
            ),
            attributeValue
        )
    }

    @Test
    fun testParse_ipv6Address() {
        val bytes = ByteArray(21)
        bytes[2] = 0x2 // family

        val portBytes = 8080.toByteArray(2)
        portBytes.copyInto(bytes, 3)

        val expectedAddress = InetAddress.ofLiteral("0:0:0:0:0:0:0:1")
        expectedAddress.address.copyInto(bytes, 5)
        val parser = MappedAddressStunAttributeValueParser()

        val attributeValue = parser.parse(bytes, 1, 20)

        assertEquals(
            MappedAddressStunAttributeValue(
                family = IpAddressFamily.IPV6,
                port = 8080,
                address = expectedAddress
            ),
            attributeValue
        )
    }
}