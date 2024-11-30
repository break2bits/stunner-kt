package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttributeListParser
import com.hal.stunner.message.header.StunHeaderParser
import java.net.DatagramPacket

class StunMessageParser(
    private val headerParser: StunHeaderParser,
    private val attributeListParser: StunAttributeListParser
) {
    fun parse(packet: DatagramPacket): StunRequest {
        val metadata = StunMetadata.fromDatagramPacket(packet)
        val header = headerParser.parse(packet.data)
        val attributes = attributeListParser.parse(packet.data, header.messageLengthBytes)
        return StunRequest(
            metadata = metadata,
            message = StunMessage(
                header = header,
                attributes = attributes
            ),
        )
    }
}