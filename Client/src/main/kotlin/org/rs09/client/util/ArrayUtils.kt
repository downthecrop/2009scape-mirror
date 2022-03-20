package org.rs09.client.util

@Suppress("NAME_SHADOWING")
object ArrayUtils {
    @JvmStatic
    fun arraycopy(source: LongArray, sourcePos: Int, dest: LongArray, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 3
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 3
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 3
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 3
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun arraycopy(source: ByteArray, sourcePos: Int, dest: ByteArray, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun arraycopy(source: IntArray, sourcePos: Int, dest: IntArray, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun arraycopy(source: FloatArray, sourcePos: Int, dest: FloatArray, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun arraycopy(source: ShortArray, sourcePos: Int, dest: ShortArray, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun arraycopy(source: Array<Any?>, sourcePos: Int, dest: Array<Any?>, destPos: Int, count: Int) {
        var sourcePos = sourcePos
        var destPos = destPos
        var count = count
        if (source === dest) {
            if (sourcePos == destPos) {
                return
            }
            if (destPos > sourcePos && destPos < sourcePos + count) {
                --count
                sourcePos += count
                destPos += count
                count = sourcePos - count
                count += 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                    dest[destPos--] = source[sourcePos--]
                }
                count -= 7
                while (sourcePos >= count) {
                    dest[destPos--] = source[sourcePos--]
                }
                return
            }
        }
        count += sourcePos
        count -= 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
            dest[destPos++] = source[sourcePos++]
        }
        count += 7
        while (sourcePos < count) {
            dest[destPos++] = source[sourcePos++]
        }
    }

    @JvmStatic
    fun fill(arr: IntArray, offset: Int, length: Int, value: Int) {
        var offset = offset
        var length = length
        length = offset + length - 7
        while (offset < length) {
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
            arr[offset++] = value
        }
        length += 7
        while (offset < length) {
            arr[offset++] = value
        }
    }

    @JvmStatic
    fun zero(arr: IntArray, offset: Int, length: Int) {
        var offset = offset
        var length = length
        length = offset + length - 7
        while (offset < length) {
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
            arr[offset++] = 0
        }
        length += 7
        while (offset < length) {
            arr[offset++] = 0
        }
    }
}