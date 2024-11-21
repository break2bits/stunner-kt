package com.hal.stunner

import com.hal.stunner.config.StunServerConfiguration
import com.hal.stunner.message.StunMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.DatagramPacket
import java.net.DatagramSocket

class StunServer(private val config: StunServerConfiguration) {
    fun listen() {
        val socket = DatagramSocket(config.port)
        val byteArray = ByteArray(100)
        val packet = DatagramPacket(byteArray, byteArray.size)

        socket.use { safeSocket ->
            while (true) {
                safeSocket.receive(packet)
                handlePacketAync(packet) {
                    safeSocket.send(it)
                }
            }
        }
    }

    private fun handlePacketAync(packet: DatagramPacket, transmitResponse: (DatagramPacket) -> Unit) {
        runBlocking {
            launch { // runs async
                transmitResponse(handlePacket(packet))
            }
        }
    }

    private fun handlePacket(packet: DatagramPacket): DatagramPacket {
        val request = StunMessage.fromDatagramPacket(packet) // deserialize request packet
        // val response = handler.handle(message) // handle request and create response
        // return stunMessageSerializer.toDatagramPacket(response) // serialize response packet
        return DatagramPacket(byteArrayOf(), 0)
    }
}