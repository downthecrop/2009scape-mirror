package rs09.net

import java.nio.ByteBuffer

/* Extension methods for the Java ByteBuffer
 * These are the same method names used in the client for buffer operations
 */

/**
 * P1 - puts a byte to the buffer
 * @param value as Byte
 */
fun ByteBuffer.p1(value: Byte) {
    put(value)
}

/**
 * P1 - puts a byte to the buffer
 * @param value as Int
 */
fun ByteBuffer.p1(value: Int) {
    put(value.toByte())
}

/**
 * G1 - gets an unsigned byte from the buffer
 * @return value as Int
 */
fun ByteBuffer.g1(): Int {
    return get().toInt() and 0xFF
}

/**
 * G1s - gets a signed byte from the buffer
 * @return value as Int
 */
fun ByteBuffer.g1s(): Int {
    return get().toInt()
}

/**
 * P2 - puts a short to the buffer
 * @param value as Int
 */
fun ByteBuffer.p2(value: Int) {
    putShort(value.toShort())
}

/**
 * G2 - gets an unsigned short from the buffer
 * @return value as Int
 */
fun ByteBuffer.g2(): Int {
    return short.toInt() and 0xFFFF
}

/**
 * G2s - gets a signed short from the buffer
 * @return value as Int
 */
fun ByteBuffer.g2s(): Int {
    return short.toInt()
}

/**
 * G3 - gets an unsigned medium (3 bytes) from the buffer
 * @return value as Int
 */
fun ByteBuffer.g3(): Int {
    return ((get().toInt() and 0xFF) shl 16) + ((get().toInt() and 0xFF) shl 8) + (get().toInt() and 0xFF)
}

/**
 * P4 - puts an integer to the buffer
 * @param value as Int
 */
fun ByteBuffer.p4(value: Int) {
    putInt(value)
}