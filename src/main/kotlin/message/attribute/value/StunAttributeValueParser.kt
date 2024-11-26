package com.hal.stunner.message.attribute.value

interface StunAttributeValueParser {
    fun parse(byteArray: ByteArray, offset: Int, lengthBytes: Int): StunAttributeValue
}