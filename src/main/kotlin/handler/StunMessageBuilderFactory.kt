package com.hal.stunner.handler

import com.hal.stunner.message.StunMessageSerializer

class StunMessageBuilderFactory(
    private val stunMessageSerializer: StunMessageSerializer,
    private val fingerprintCalculator: FingerprintCalculator
) {
    fun fromTransactionId(transactionId: ByteArray): StunMessageBuilder {
        return StunMessageBuilder(stunMessageSerializer, fingerprintCalculator, transactionId)
    }
}