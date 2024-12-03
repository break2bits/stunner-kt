package com.hal.stunner.message.attribute.value

import com.hal.stunner.message.attribute.IpAddressFamily
import java.nio.ByteBuffer

data class XorMappedAddressStunAttributeValue(
    val family: IpAddressFamily,
    val xport: ByteArray,
    val xAddress: ByteArray
) : StunAttributeValue {
    override fun writeBytes(buffer: ByteBuffer) {
        buffer.putShort(family.value.toShort())
        buffer.put(xport)
        buffer.put(xAddress)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XorMappedAddressStunAttributeValue

        if (family != other.family) return false
        if (!xport.contentEquals(other.xport)) return false
        if (!xAddress.contentEquals(other.xAddress)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = family.hashCode()
        result = 31 * result + xport.contentHashCode()
        result = 31 * result + xAddress.contentHashCode()
        return result
    }
}