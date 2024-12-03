package com.hal.stunner.message.attribute.value

import com.hal.stunner.message.attribute.IpAddressFamily
import java.net.InetAddress
import java.nio.ByteBuffer

data class MappedAddressStunAttributeValue(
    val family: IpAddressFamily,
    val port: Int,
    val address: InetAddress,
): StunAttributeValue {
    companion object {
        const val FAMILY_SIZE_BYTES = 2
        const val PORT_SIZE_BYTES = 2

        const val IPV4_ADDRESS_SIZE_BYTES = 4
        const val IPV6_ADDRESS_SIZE_BYTES = 16
    }

    override fun writeBytes(buffer: ByteBuffer) {
        buffer.putShort(family.value.toShort())
        buffer.putShort(port.toShort())
        buffer.put(address.address)
    }
}