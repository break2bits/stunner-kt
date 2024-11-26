package print

import com.hal.stunner.print.toPrettyBinaryString
import kotlin.test.Test
import kotlin.test.assertEquals

class BinaryStringifierExtensionsTest {
    @Test
    fun testToPrettyBinaryString() {
        val number = 0b10110111

        val binaryString = number.toPrettyBinaryString(8)

        assertEquals("0b10110111", binaryString)
    }

    @Test
    fun testToPrettyBinaryString_zero_fourDigits() {
        val number = 0

        val binaryString = number.toPrettyBinaryString(4)

        assertEquals("0b0000", binaryString)
    }

    @Test
    fun testToPrettyBinaryString_zeroDigits() {
        val number = 0b10110111

        val binaryString = number.toPrettyBinaryString(0)

        assertEquals("0b", binaryString)
    }
}