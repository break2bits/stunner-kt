package com.hal.stunner.message

class StunMessage(private val stunHeader: StunHeader) {
    companion object {

        fun fromBytes(bytes: ByteArray) {
            val header = StunHeader.fromBytes(bytes)
            // parse attributes using length from header
        }

    }
}