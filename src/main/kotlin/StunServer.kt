package com.hal.stunner

import com.hal.stunner.config.StunServerConfiguration
import com.hal.stunner.message.StunMessageParser
import com.hal.stunner.message.StunMessageSerializer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.DatagramPacket
import java.net.DatagramSocket

class StunServer(
    private val config: StunServerConfiguration,
    private val handler: StunHandler,
    private val parser: StunMessageParser,
    private val serializer: StunMessageSerializer
) {
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

    private fun handlePacketAync(
        packet: DatagramPacket,
        transmitResponse: (DatagramPacket) -> Unit,
    ) {
        runBlocking {
            launch {
                // runs async
                transmitResponse(handlePacket(packet))
            }
        }
    }

    private fun handlePacket(packet: DatagramPacket): DatagramPacket {
        val request = parser.parse(packet) // deserialize request packet
        val response = handler.handle(request) // handle request and create response
        return serializer.serialize(response)
    }
}