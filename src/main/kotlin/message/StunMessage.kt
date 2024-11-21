package com.hal.stunner.message

import java.net.DatagramPacket

class StunMessage(
    private val metadata: StunMetadata,
    private val header: StunHeader
) {
    companion object {

        fun fromDatagramPacket(packet: DatagramPacket): StunMessage {
            val metadata = StunMetadata.fromDatagramPacket(packet)
            val header = StunHeader.fromBytes(packet.data)
            return StunMessage(
                metadata = metadata,
                header = header
            )
        }
    }
}