package com.hal.stunner.message.attribute.value

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.message.attribute.IpAddressFamily
import com.hal.stunner.message.attribute.StunAttributeParseException
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValue.Companion.FAMILY_SIZE_BYTES
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValue.Companion.IPV4_ADDRESS_SIZE_BYTES
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValue.Companion.IPV6_ADDRESS_SIZE_BYTES
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValue.Companion.PORT_SIZE_BYTES
import com.hal.stunner.print.toPrettyHexString
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

class MappedAddressStunAttributeValueParser : StunAttributeValueParser {
    companion object {
        fun getExpectedValueLengthBytes(family: IpAddressFamily): Int {
            return when (family) {
                IpAddressFamily.IPV4 -> FAMILY_SIZE_BYTES + PORT_SIZE_BYTES + IPV4_ADDRESS_SIZE_BYTES
                IpAddressFamily.IPV6 -> FAMILY_SIZE_BYTES + PORT_SIZE_BYTES + IPV6_ADDRESS_SIZE_BYTES
            }
        }
    }

    override fun parse(bytes: ByteArray, offset: Int, lengthBytes: Int): StunAttributeValue {
        val addressFamilyValue = BinaryHelper.getBytesAsInt(bytes, offset, FAMILY_SIZE_BYTES)
        val addressFamily = IpAddressFamily.fromValue(addressFamilyValue)
        if (addressFamily == null) {
            throw StunAttributeParseException("Unrecognized ip address family ${addressFamilyValue.toPrettyHexString()}")
        }
        validateLengthsMatch(lengthBytes, addressFamily)

        val port = BinaryHelper.getBytesAsInt(bytes, offset + FAMILY_SIZE_BYTES, PORT_SIZE_BYTES)

        return when (addressFamily) {
            IpAddressFamily.IPV4 -> MappedAddressStunAttributeValue(
                family = addressFamily,
                port = port,
                address = readIpV4Address(bytes, offset + FAMILY_SIZE_BYTES + PORT_SIZE_BYTES)
            )
            IpAddressFamily.IPV6 -> MappedAddressStunAttributeValue(
                family = addressFamily,
                port = port,
                address = readIpV6Address(bytes, offset + FAMILY_SIZE_BYTES + PORT_SIZE_BYTES)
            )
        }
    }

    private fun readIpV4Address(bytes: ByteArray, offset: Int): Inet4Address {
        val addressBytes = bytes.sliceArray(IntRange(offset, offset + IPV4_ADDRESS_SIZE_BYTES - 1))
        return InetAddress.getByAddress(addressBytes) as Inet4Address
    }

    private fun readIpV6Address(bytes: ByteArray, offset: Int): Inet6Address {
        val addressBytes = bytes.sliceArray(IntRange(offset, offset + IPV6_ADDRESS_SIZE_BYTES - 1))
        return InetAddress.getByAddress(addressBytes) as Inet6Address
    }

    private fun validateLengthsMatch(lengthBytes: Int, family: IpAddressFamily) {
        if (lengthBytes != getExpectedValueLengthBytes(family)) {
            throw StunAttributeParseException("Expected length for mapped address of family ${family.name} " +
                    "does not match attribute value length of $lengthBytes")
        }
    }
}