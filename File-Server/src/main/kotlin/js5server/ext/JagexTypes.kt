package js5server.ext

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import java.nio.ByteBuffer
import kotlin.text.toByteArray

private val CHARSET = charset("UTF-8")

suspend fun ByteReadChannel.readMedium(): Int {
    return (readByte().toInt() shl 16) + (readByte().toInt() shl 8) + readByte().toInt()
}

suspend fun ByteReadChannel.readUByte(): Int {
    return readByte().toInt() and 0xff
}

suspend fun ByteReadChannel.readUMedium(): Int {
    return (readUByte() shl 16) + (readUByte() shl 8) + readUByte()
}

fun ByteBuffer.putSmart(value: Int) {
    if (value >= 128) {
        putShort((value + 32768).toShort())
    } else {
        put(value.toByte())
    }
}

fun ByteBuffer.putJagexString(string: String) {
    put(0)
    put(string.toByteArray(CHARSET))
    put(0)
}

fun ByteBuffer.putShortA(value: Int) {
    put(((value shr 8) and 0xFFFF).toByte())
    put(((value + 128) and 0xFF).toByte())
}

fun ByteBuffer.putIntB(value: Int) {
    put((value shr 16).toByte())
    put((value shr 24).toByte())
    put((value).toByte())
    put((value shr 8).toByte())
}

fun ByteBuffer.putUnsignedByteS(value: Byte) {
    put(((value + 128) and 0xFF).toByte())
}

fun ByteBuffer.putUnsignedShort(value: Int) {
    putShort(((value) and 0xFFFF).toShort())
}

fun BytePacketBuilder.writeUnsignedByteSubtract(value: Int) {
    writeByte(((value + 128) and 0xFF).toByte())
}

fun BytePacketBuilder.writeUnsignedShort(value: Int) {
    writeShort(((value) and 0xFFFF).toShort())
}

fun BytePacketBuilder.writeShortAdd(value: Int) {
    writeByte((value shr 8).toByte())
    writeByte((value + 128).toByte())
}

fun BytePacketBuilder.writeSmart(value: Int) {
    if (value >= 128) {
        writeShort((value + 32768).toShort())
    } else {
        writeByte(value.toByte())
    }
}

fun BytePacketBuilder.writeIntME(value: Int) {
    writeByte((value shr 16).toByte())
    writeByte((value shr 24).toByte())
    writeByte(value.toByte())
    writeByte((value shr 8).toByte())
}

fun BytePacketBuilder.writeVersionedString(value: String) {
    writeByte(0)
    writeText(value)
    writeByte(0)
}