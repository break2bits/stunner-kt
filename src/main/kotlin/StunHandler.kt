package com.hal.stunner

import com.hal.stunner.binary.toByteArray
import com.hal.stunner.binary.xor
import com.hal.stunner.message.*
import com.hal.stunner.message.attribute.IpAddressFamily
import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.XorMappedAddressStunAttributeValue
import com.hal.stunner.message.header.StunHeader
import java.net.Inet4Address
import java.net.Inet6Address

class StunHandler {

    // handles binding only right now
    fun handle(request: StunRequest): StunMessage {
        if (request.message.header.type != StunMessageType.BINDING_REQUEST) {
            throw NotImplementedError("handle is not implemented")
        }
        return StunMessageBuilder
            .fromTransactionId(request.message.header.transactionId)
            .addAttribute(buildXorMappedAddressStunAttribute(request))
            .build()
    }

    private fun buildXorMappedAddressStunAttribute(request: StunRequest): StunAttribute {
        val family = getIpAddressFamily(request.metadata)
        return StunAttribute(
            type = StunAttributeType.XOR_MAPPED_ADDRESS,
            valueLengthBytes = getValueLengthBytes(family),
            value = XorMappedAddressStunAttributeValue(
                family = family,
                xport = getXport(request.metadata),
                xAddress = getXaddress(family, request.metadata, request.message.header.transactionId)
            )
        )
    }

    private fun getIpAddressFamily(metadata: StunMetadata): IpAddressFamily {
        return when (metadata.ip) {
            is Inet4Address -> IpAddressFamily.IPV4
            is Inet6Address -> IpAddressFamily.IPV6
            else -> throw BadRequestException("Unrecognized inet address type in request metadata")
        }
    }

    private fun getXport(metadata: StunMetadata): ByteArray {
        val portBytes = metadata.port.toByteArray()
        val magicCookieBytes = StunHeader.MAGIC_COOKIE.toByteArray().sliceArray(IntRange(0, 1))
        return portBytes xor magicCookieBytes
    }

    private fun getXaddress(family: IpAddressFamily, metadata: StunMetadata, transactionId: ByteArray): ByteArray {
        val magicCookieBytes = StunHeader.MAGIC_COOKIE.toByteArray()
        val address = metadata.ip.address
        return when (family) {
            IpAddressFamily.IPV4 -> address xor magicCookieBytes
            IpAddressFamily.IPV6 -> address xor magicCookieBytes.plus(transactionId)
        }
    }

    private fun getValueLengthBytes(family: IpAddressFamily): Int {
        return when (family) {
            IpAddressFamily.IPV4 -> 8
            IpAddressFamily.IPV6 -> 20
        }
    }
}
