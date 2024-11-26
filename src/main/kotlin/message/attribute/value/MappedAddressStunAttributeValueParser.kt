package com.hal.stunner.message.attribute.value

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.message.attribute.IpAddressFamily
import com.hal.stunner.message.attribute.StunAttributeParseException
import java.net.Inet4Address
import java.net.InetAddress

class MappedAddressStunAttributeValueParser : StunAttributeValueParser {
    companion object {
        private const val IPV4_ADDRESS_SIZE_BYTES = 4
        private const val IPV6_ADDRESS_SIZE_BYTES = 16
    }
    @OptIn(ExperimentalStdlibApi::class)
    override fun parse(bytes: ByteArray, offset: Int, lengthBytes: Int): StunAttributeValue {
        val addressFamilyValue = BinaryHelper.getBytesAsInt(bytes, offset, 2)
        val addressFamily = IpAddressFamily.fromValue(addressFamilyValue)
        if (addressFamily == null) {
            throw StunAttributeParseException("Unrecognized ip address family ${addressFamilyValue.toHexString()}")
        }
        val port = BinaryHelper.getBytesAsInt(bytes, offset + 2, 2)

        when (addressFamily) {
            IpAddressFamily.IPV4 -> return MappedAddressStunAttributeValue(
                family = addressFamily,
                port = port,
                address = readIpV4Address(bytes, offset + 4)
            )
            IpAddressFamily.IPV6 -> return MappedAddressStunAttributeValue(
                family = addressFamily,
                port = port,
                address = readIpV6Address(bytes, offset + 4)
            )
            else -> throw RuntimeException("Unreachable!")
        }
    }

    private fun readIpV4Address(bytes: ByteArray, offset: Int): Inet4Address {
        val addressBytes = bytes.sliceArray(IntRange(offset, offset + IPV4_ADDRESS_SIZE_BYTES))
        return InetAddress.getByAddress(addressBytes) as Inet4Address
    }

    private fun readIpV6Address(bytes: ByteArray, offset: Int): Inet4Address {
        val addressBytes = bytes.sliceArray(IntRange(offset, offset + IPV6_ADDRESS_SIZE_BYTES))
        return InetAddress.getByAddress(addressBytes) as Inet4Address
    }
}