package com.hal.stunner

import com.hal.stunner.binary.toByteArray
import com.hal.stunner.message.StunMessage
import com.hal.stunner.message.StunMessageType
import com.hal.stunner.message.StunMetadata
import com.hal.stunner.message.StunRequest
import com.hal.stunner.message.attribute.IpAddressFamily
import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.attribute.StunAttributeType
import com.hal.stunner.message.attribute.value.XorMappedAddressStunAttributeValue
import com.hal.stunner.message.header.StunHeader
import java.net.Inet4Address
import java.net.Inet6Address
import java.nio.ByteBuffer

class StunHandler {

    // handles binding only right now
    fun handle(request: StunRequest): StunMessage {
        throw NotImplementedError("handle is not implemented")
    }
//        throw NotImplementedError("handle is not implemented")
//        if (request.message.header.type == StunMessageType.BINDING_REQUEST)
//        val response = StunMessage(
//            header = StunHeader(
//                type = StunMessageType.BINDING_RESPONSE,
//                messageLengthBytes = messageLengthBytes,
//                magicCookie = StunHeader.MAGIC_COOKIE,
//                transactionId = request.message.header.transactionId
//            ),
//            attributes =
//        )
//    }
//
//    private fun buildXorMappedAddressStunAttribute(request: StunRequest): StunAttribute {
//        return StunAttribute(
//            type = StunAttributeType.XOR_MAPPED_ADDRESS,
//            value = XorMappedAddressStunAttributeValue(
//                family = getIpAddressFamily(request.metadata),
//                xport = getXport(request.metadata)
//            )
//        )
//    }
//
//    private fun getIpAddressFamily(metadata: StunMetadata): IpAddressFamily {
//        if (metadata.ip is Inet4Address) {
//            return IpAddressFamily.IPV4
//        } else if (metadata.ip is Inet6Address) {
//            return IpAddressFamily.IPV6
//        } else {
//            throw BadRequestException("Unrecognized inet address type in request metadata")
//        }
//    }
//
//    private fun getXport(metadata: StunMetadata): ByteArray {
//        val portBytes = metadata.port.toByteArray()
//        val magicCookieBytes = StunHeader.MAGIC_COOKIE.toByteArray().slice(IntRange(0, 1))
//
//    }
}
