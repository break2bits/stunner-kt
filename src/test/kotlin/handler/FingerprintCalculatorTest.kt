package handler

import com.hal.stunner.handler.FingerprintCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class FingerprintCalculatorTest {

    @Test
    public fun testCalculateFingerprint() {
        val testBytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        val expectedFingerprint = -446124413

        val calculator = FingerprintCalculator()

        val fingerprint = calculator.calculateFingerprint(testBytes)

        assertEquals(expectedFingerprint, fingerprint)
    }
}