package com.hal.stunner.message

import com.hal.stunner.message.attribute.StunAttribute
import com.hal.stunner.message.header.StunHeader

data class StunMessage(
    val metadata: StunMetadata,
    val header: StunHeader,
    val attributes: List<StunAttribute>
)