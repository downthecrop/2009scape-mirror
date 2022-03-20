package org.rs09.client.util

// TODO This is literally so useless because arrays are never returned to the pool...
object ByteArrayPool {
    private val SMALL_BUFFERS = arrayOfNulls<ByteArray>(1000)
    private val MEDIUM_BUFFERS = arrayOfNulls<ByteArray>(250)
    private val LARGE_BUFFERS = arrayOfNulls<ByteArray>(50)
    private var SMALL_INDEX = 0
    private var MEDIUM_INDEX = 0
    private var LARGE_INDEX = 0

    @Synchronized
    fun getByteArray(size: Int): ByteArray = when {
        size == 100 && SMALL_INDEX > 0 -> {
            val arr = SMALL_BUFFERS[--SMALL_INDEX]!!
            SMALL_BUFFERS[SMALL_INDEX] = null
            arr
        }
        size == 5000 && MEDIUM_INDEX > 0 -> {
            val arr = MEDIUM_BUFFERS[--MEDIUM_INDEX]!!
            MEDIUM_BUFFERS[MEDIUM_INDEX] = null
            arr
        }
        size == 30000 && LARGE_INDEX > 0 -> {
            val arr = LARGE_BUFFERS[--LARGE_INDEX]!!
            LARGE_BUFFERS[LARGE_INDEX] = null
            arr
        }
        else -> ByteArray(size)

    }
}