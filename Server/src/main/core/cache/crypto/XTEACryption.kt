package core.cache.crypto

import java.nio.ByteBuffer

/** Holds XTEA(eXtended Tiny Encryption Algorithm) encryption/decryption methods. Usually used for RS Map decryption.  */
class XTEACryption {
    companion object {
        // Purely Static Class
        // XTEA consts https://scispace.com/pdf/xtea-cryptography-implementation-in-android-chatting-app-3pg6ownewx.pdf
        /** Fibonacci hashing. Math.pow(2,32) * Math.sqrt(5)-1)/2 => 32 bits(int) multiply by reciprocal of golden ratio.  */
        private const val DELTA = -1640531527 // Formula cast to unsigned int.
        /** DELTA << 5. Essentially multiply DELTA by NUM_ROUNDS or number of rounds.  */
        private const val SUM = -957401312 // 0xC6EF3720
        /** The amount of "cryption cycles".  */
        private const val NUM_ROUNDS = 32
        /** Constant to manipulate the key index in decipher/encrypt. This was a magic number randomly selected by RS.  */
        private const val KEY_SCRAMBLE = 0x1933

        /**
         * Decrypts the buffer data.
         * @param keys The keys.
         * @param buffer The buffer to decrypt.
         * @param offset The offset of the data to decrypt.
         * @param length The length.
         * @return The decrypted data.
         */
        fun decrypt(keys: IntArray, buffer: ByteBuffer, offset: Int, length: Int): ByteBuffer {
            val numBlocks = (length - offset) / 8
            val block = IntArray(2)
            for (i in 0 until numBlocks) {
                val index = i * 8 + offset
                block[0] = buffer.getInt(index)
                block[1] = buffer.getInt(index + 4)
                decipher(keys, block)
                buffer.putInt(index, block[0])
                buffer.putInt(index + 4, block[1])
            }
            return buffer
        }

        /**
         * Deciphers the values of the block using the Tiny Encryption Algorithm (TEA).
         * This algorithm relies on a Feistel network, using bitwise operations and
         * modular subtraction to remove confusion(block[0] line) and diffusion(block[1] line).
         *
         * @param keys 128-bit key (4x32-bit int).
         * @param block 64-bit data block (2x32-bit int) to be decrypted.
         */
        private fun decipher(keys: IntArray, block: IntArray) {
            var sum =
                SUM.toLong() // Running decreasing sum to ensure different round constants - should become 0 at the end.
            for (i in 0 until NUM_ROUNDS) {
                block[1] -= (keys[(sum and KEY_SCRAMBLE.toLong() ushr 11).toInt()] + sum xor (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()).toInt()
                sum -= DELTA.toLong()
                block[0] -= (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
            }
        }
        /** === Encryption functions below are not used unless you are modifying the cache. ===  */
        /**
         * Encrypts the buffer data.
         * @param keys The keys.
         * @param buffer The buffer to encrypt.
         * @param offset The offset of the data to encrypt.
         * @param length The length.
         * @return The encrypted data.
         */
        fun encrypt(keys: IntArray, buffer: ByteBuffer, offset: Int, length: Int) {
            val numBlocks = (length - offset) / 8
            val block = IntArray(2)
            for (i in 0 until numBlocks) {
                val index = i * 8 + offset
                block[0] = buffer.getInt(index)
                block[1] = buffer.getInt(index + 4)
                encipher(keys, block)
                buffer.putInt(index, block[0])
                buffer.putInt(index + 4, block[1])
            }
        }

        /**
         * Enciphers the values of the block using the Tiny Encryption Algorithm (TEA).
         * This algorithm relies on a Feistel network, using bitwise operations and
         * modular addition to provide confusion(block[0] line) and diffusion(block[1] line).
         *
         * @param keys 128-bit key (4x32-bit int).
         * @param block 64-bit data block (2x32-bit int) to be encrypted.
         */
        private fun encipher(keys: IntArray, block: IntArray) {
            var sum: Long = 0 // Running sum to ensure different round constants - should match SUM at the end.
            for (i in 0 until NUM_ROUNDS) {
                block[0] += (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
                sum += DELTA.toLong()
                block[1] += (keys[(sum and KEY_SCRAMBLE.toLong() ushr 11).toInt()] + sum xor (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()).toInt()
            }
        }
    }
}