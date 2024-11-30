package com.hal.stunner.message

data class StunRequest(
    val metadata: StunMetadata,
    val message: StunMessage
)