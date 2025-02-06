package handler

import com.hal.stunner.handler.FingerprintCalculator
import com.hal.stunner.handler.StunMessageBuilder
import com.hal.stunner.handler.StunMessageBuilderFactory
import com.hal.stunner.message.StunMessageSerializer
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals

class StunMessageBuilderFactoryTest {

    @Test
    public fun testFromTransactionId() {
        val txnId = byteArrayOf(0x01)
        val serializer = mock<StunMessageSerializer>()
        val calculator = mock<FingerprintCalculator>()
        val factory = StunMessageBuilderFactory(
            stunMessageSerializer = serializer,
            fingerprintCalculator = calculator
        )

        val builder = factory.fromTransactionId(txnId)
        val expected = StunMessageBuilder(
            stunMessageSerializer = serializer,
            fingerprintCalculator = calculator,
            transactionId = txnId
        )

        assertEquals(expected, builder)
    }
}