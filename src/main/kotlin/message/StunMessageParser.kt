package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttributeListParser
import java.net.DatagramPacket

class StunMessageParser(private val attributeListParser: StunAttributeListParser) {
    fun parse(packet: DatagramPacket): StunMessage {
        val metadata = StunMetadata.fromDatagramPacket(packet)
        val header = StunHeader.fromBytes(packet.data)
        val attributes = attributeListParser.parse(header, packet.data)
        return StunMessage(
            metadata = metadata,
            header = header,
            attributes = attributes
        )
    }
}