package com.hal.stunner.message

data class StunRequest(
    val metadata: StunMetadata,
    val message: StunMessage,
    val rawBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StunRequest

        if (metadata != other.metadata) return false
        if (message != other.message) return false
        if (!rawBytes.contentEquals(other.rawBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metadata.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + rawBytes.contentHashCode()
        return result
    }
}