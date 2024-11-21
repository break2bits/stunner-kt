package com.hal.stunner

import com.hal.stunner.config.StunServerConfiguration
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

    private fun handlePacketAync(request: DatagramPacket, transmitResponse: (DatagramPacket) -> Unit) {
        runBlocking {
            launch {
                transmitResponse(handlePacket(request))
            }
        }
    }

    private fun handlePacket(request: DatagramPacket): DatagramPacket {
        return DatagramPacket(byteArrayOf(), 0)
    }
}