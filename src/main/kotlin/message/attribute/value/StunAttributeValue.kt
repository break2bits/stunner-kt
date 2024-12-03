package com.hal.stunner.message.attribute.value

import java.io.Serializable
import java.nio.ByteBuffer

fun interface StunAttributeValue : Serializable {
    fun writeBytes(buffer: ByteBuffer)
}