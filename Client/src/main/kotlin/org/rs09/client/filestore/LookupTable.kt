package org.rs09.client.filestore

class LookupTable(identifiers: IntArray) {
    private val table: IntArray

    operator fun get(identifier: Int): Int {
        val mask = (table.size shr 1) + -1
        var i = mask and identifier

        while (true) {
            val id = table[1 + i + i]
            if (id == -1) {
                return -1
            }
            if (identifier == table[i + i]) {
                return id
            }
            i = i - -1 and mask
        }
    }

    init {
        var i = 1
        while (i <= (identifiers.size shr 1) + identifiers.size) {
            i = i shl 1
        }

        table = IntArray(i + i)
        for (j in 0 until i + i) table[j] = -1

        for (j in identifiers.indices) {
            var k = identifiers[j] and i - 1
            while (-1 != table[1 + (k + k)]) {
                k = 1 + k and i - 1
            }
            table[k + k] = identifiers[j]
            table[k + k + 1] = j
        }
    }
}
