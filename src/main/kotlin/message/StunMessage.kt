package com.hal.stunner.message

import com.hal.stunner.message.attribute.value.StunAttribute

class StunMessage(
    private val metadata: StunMetadata,
    private val header: StunHeader,
    private val attributes: List<StunAttribute>
)