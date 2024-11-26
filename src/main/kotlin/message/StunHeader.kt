package com.hal.stunner.message

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.print.toPrettyBinaryString

data class StunHeader(
    val type: StunMessageType,
    val length: Int,
    val magicCookie: Int,
    val transactionId: ByteArray
) {
    companion object {
        const val SIZE_BYTES = 20

        private const val HEADER_SIZE_BYTES = 20
        private const val MAGIC_COOKIE = 0x2112A442

        private const val TYPE_START_IDX = 0
        private const val TYPE_SIZE_BYTES = 2

        private const val LENGTH_START_IDX = 2
        private const val LENGTH_SIZE_BYTES = 2

        private const val MAGIC_COOKIE_START_IDX = 4
        private const val MAGIC_COOKIE_SIZE_BYES = 4

        private const val TRANSACTION_ID_START_IDX = 8
        private const val TRANSACTION_ID_SIZE_BYTES = 12

        @OptIn(ExperimentalStdlibApi::class)
        fun fromBytes(bytes: ByteArray): StunHeader {
            if (bytes.size < HEADER_SIZE_BYTES) {
                throw StunParseException("Stun message must be at least 20 bytes")
            }

            if (!areTopTwoBitsZero(bytes[0])) {
                throw StunParseException("First two bits must be 0")
            }

            val type = getType(bytes)

            // does not include the 20-byte header size
            val messageLength = getLength(bytes)
            if (bytes.size != (HEADER_SIZE_BYTES + messageLength)) {
                throw StunParseException("Incorrect value of message length. Received $messageLength but expected ${bytes.size - 20}")
            }

            val magicCookie = getMagicCookie(bytes)
            if (magicCookie != MAGIC_COOKIE) {
                throw StunParseException("Incorrect magic cookie value. Received ${magicCookie.toHexString()} but expected $MAGIC_COOKIE")
            }

            val transactionId = getTransactionId(bytes)
            return StunHeader(
                type = type,
                length = messageLength,
                magicCookie = magicCookie,
                transactionId = transactionId
            )
        }

        private fun getMagicCookie(bytes: ByteArray): Int {
            return BinaryHelper.getBytesAsInt(bytes, MAGIC_COOKIE_START_IDX, MAGIC_COOKIE_SIZE_BYES)
        }

        private fun getTransactionId(bytes: ByteArray): ByteArray {
            return bytes.sliceArray(IntRange(TRANSACTION_ID_START_IDX, TRANSACTION_ID_START_IDX + TRANSACTION_ID_SIZE_BYTES))
        }

        private fun areTopTwoBitsZero(byte: Byte): Boolean {
            return (byte.toInt() shr 6) == 0
        }

        private fun getType(bytes: ByteArray): StunMessageType {
            val messageTypeValue = BinaryHelper.getBytesAsInt(bytes, TYPE_START_IDX, TYPE_SIZE_BYTES)
            val messageType = StunMessageType.fromValue(messageTypeValue)
            if (messageType == null) {
                throw StunParseException("Unrecognized message type for value ${messageTypeValue.toPrettyBinaryString(16)}")
            }
            return messageType
        }

        private fun getLength(bytes: ByteArray): Int {
            return BinaryHelper.getBytesAsInt(bytes, LENGTH_START_IDX, LENGTH_SIZE_BYTES)
        }
    }
}
