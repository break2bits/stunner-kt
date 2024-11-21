package com.hal.stunner.message

import java.net.DatagramPacket
import java.net.InetAddress

data class StunMetadata(
    val ip: InetAddress,
    val port: Int
) {
    companion object {
        fun fromDatagramPacket(packet: DatagramPacket): StunMetadata {
            return StunMetadata(
                ip = packet.address,
                port = packet.port
            )
        }
    }
}