package com.hal.stunner.message

data class StunHeader(
    val type: StunMessageType,
    val length: Int,
    val magicCookie: Int,
    val transactionId: ByteArray
)
