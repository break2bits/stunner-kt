package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.header.StunHeader
import java.net.DatagramPacket
import java.nio.ByteBuffer

class StunMessageSerializer {
    fun serialize(message: StunMessage): DatagramPacket {
        val outputSizeBytes = message.getTotalLengthBytes()
        val buffer = ByteBuffer.allocate(outputSizeBytes)
        writeHeader(buffer, message.header)
        writeAttributes(buffer, message.attributes)
        val output = buffer.array()
        return DatagramPacket(output, output.size)
    }

    private fun writeHeader(buffer: ByteBuffer, header: StunHeader) {
        buffer.putShort(header.type.value.toShort())

        val lengthBytes = header.messageLengthBytes.toShort()
        buffer.putShort(lengthBytes)

        buffer.putInt(header.magicCookie)

        buffer.put(header.transactionId)
    }

    private fun writeAttributes(buffer: ByteBuffer, attributes: List<StunAttribute>) {
        attributes.forEach {
            writeAttribute(buffer, it)
        }
    }

    private fun writeAttribute(buffer: ByteBuffer, attribute: StunAttribute) {
        buffer.putShort(attribute.type.value.toShort())
        buffer.putShort(attribute.valueLengthBytes.toShort())

        // this kind of breaks the pattern a bit but we really need to do it this way due
        // to polymorphism
        attribute.value.writeBytes(buffer)
    }
}