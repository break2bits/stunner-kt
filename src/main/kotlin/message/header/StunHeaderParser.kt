package com.hal.stunner.message.header

import com.hal.stunner.binary.BinaryHelper
import com.hal.stunner.message.StunMessageType
import com.hal.stunner.message.StunParseException

class StunHeaderParser {

    @OptIn(ExperimentalStdlibApi::class)
    fun parse(bytes: ByteArray): StunHeader {
        if (bytes.size < StunHeader.HEADER_SIZE_BYTES) {
            throw StunParseException("Stun message must be at least ${StunHeader.HEADER_SIZE_BYTES} bytes")
        }

        if (!areTopTwoBitsZero(bytes[0])) {
            throw StunParseException("First two bits must be 0")
        }

        val type = getType(bytes)

        // does not include the 20-byte header size
        val messageLengthBytes = getLength(bytes)
        if (bytes.size != (StunHeader.HEADER_SIZE_BYTES + messageLengthBytes)) {
            throw StunParseException("Incorrect value of message length. Received $messageLengthBytes but expected ${bytes.size - 20}")
        }

        val magicCookie = getMagicCookie(bytes)
        val expectedMagicCookie = StunHeader.MAGIC_COOKIE
        if (magicCookie != expectedMagicCookie) {
            throw StunParseException("Incorrect magic cookie value. Received ${magicCookie.toHexString()} but expected ${StunHeader.MAGIC_COOKIE}")
        }

        val transactionId = getTransactionId(bytes)
        return StunHeader(
            type = type,
            messageLengthBytes = messageLengthBytes,
            magicCookie = magicCookie,
            transactionId = transactionId
        )
    }

    private fun getMagicCookie(bytes: ByteArray): Int {
        return BinaryHelper.getBytesAsInt(bytes, StunHeader.MAGIC_COOKIE_START_IDX, StunHeader.MAGIC_COOKIE_SIZE_BYES)
    }

    private fun getTransactionId(bytes: ByteArray): ByteArray {
        return bytes.sliceArray(IntRange(StunHeader.TRANSACTION_ID_START_IDX, StunHeader.TRANSACTION_ID_START_IDX + StunHeader.TRANSACTION_ID_SIZE_BYTES - 1))
    }

    private fun areTopTwoBitsZero(byte: Byte): Boolean {
        return (byte.toInt() shr 6) == 0
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getType(bytes: ByteArray): StunMessageType {
        val messageTypeValue = BinaryHelper.getBytesAsInt(bytes, StunHeader.TYPE_START_IDX, StunHeader.TYPE_SIZE_BYTES)
        val messageType = StunMessageType.fromValue(messageTypeValue)
        if (messageType == null) {
            throw StunParseException("Unrecognized message type for value ${messageTypeValue.toHexString()}")
        }
        return messageType
    }

    private fun getLength(bytes: ByteArray): Int {
        return BinaryHelper.getBytesAsInt(bytes, StunHeader.LENGTH_START_IDX, StunHeader.LENGTH_SIZE_BYTES)
    }
}