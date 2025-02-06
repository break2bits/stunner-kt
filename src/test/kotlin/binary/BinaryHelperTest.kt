package com.hal.stunner.binary

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BinaryHelperTest {

    @Test
    fun testGetBytesAsInt() {
        val bytes = byteArrayOf(0x01, 0x11, 0x0f, 0x1, 0x71, 0x13)
        val start = 2
        val numBytes = 4
        val bytesAsInt: Int = BinaryHelper.getBytesAsInt(bytes, start, numBytes)

        assertEquals(251752723, bytesAsInt)
    }

    @Test
    fun testByteArrayXorExtension() {
        val first = byteArrayOf(0b0101, 0b0000, 0b1010, -0b1111111)
        val second = byteArrayOf(0b0101, 0b1111, 0b0101, -0b10)

        val result = first xor second

        assertContentEquals(
            byteArrayOf(0b0, 0b1111, 0b1111, 0b1111111),
            result
        )
    }

    @Test
    fun testByteArrayXorExtension_tooShort() {
        val first = byteArrayOf(0b0101, 0b0000, 0b1010, -0b1111111)
        val second = byteArrayOf(0b0101)

        assertThrows<IllegalArgumentException> {
            first xor second
        }
    }

    @Test
    fun testIntExtension_toByteArray() {
        val result = (-1).toByteArray()

        assertContentEquals(
            byteArrayOf(-1, -1, -1, -1),
            result
        )
    }

    @Test
    fun testShortExtension_toByteArray() {
        val result: Short = -1

        val bytes = result.toByteArray()

        assertContentEquals(
            byteArrayOf(-1, -1),
            bytes
        )
    }
}