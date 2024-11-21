package com.hal.stunner.message

enum class StunMessageType(val value: Int) {
    BINDING_REQUEST(0x0001),
    BINDING_RESPONSE(0x0101);

    companion object {
        fun forValue(value: Int): StunMessageType? {
            return entries.find { it.value == value }
        }
    }
}