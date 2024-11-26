package com.hal.stunner.message.attribute.value

import com.hal.stunner.message.attribute.IpAddressFamily
import java.net.InetAddress

data class MappedAddressStunAttributeValue(
    val family: IpAddressFamily,
    val port: Int,
    val address: InetAddress,
): StunAttributeValue