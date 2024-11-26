package com.hal.stunner.message.attribute

enum class IpAddressFamily(private val value: Int) {
    IPV4(0x01),
    IPV6(0x02);

    companion object {
        fun fromValue(value: Int): IpAddressFamily? {
            return entries.find { it.value == value }
        }
    }
}