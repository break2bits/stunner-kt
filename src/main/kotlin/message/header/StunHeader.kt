package com.hal.stunner.message.header

import com.hal.stunner.message.StunMessageType

data class StunHeader(
    val type: StunMessageType,
    val messageLengthBytes: Int, // the message length not including the header size
    val magicCookie: Int,
    val transactionId: ByteArray
) {
    companion object {
        const val SIZE_BYTES = 20

        const val HEADER_SIZE_BYTES = 20
        const val MAGIC_COOKIE = 0x2112A442

        const val TYPE_START_IDX = 0
        const val TYPE_SIZE_BYTES = 2

        const val LENGTH_START_IDX = 2
        const val LENGTH_SIZE_BYTES = 2

        const val MAGIC_COOKIE_START_IDX = 4
        const val MAGIC_COOKIE_SIZE_BYES = 4

        const val TRANSACTION_ID_START_IDX = 8
        const val TRANSACTION_ID_SIZE_BYTES = 12
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is StunHeader) {
            return false
        }
        return type == other.type
                && messageLengthBytes == other.messageLengthBytes
                && magicCookie == other.magicCookie
                && transactionId.contentEquals(other.transactionId)
    }
}