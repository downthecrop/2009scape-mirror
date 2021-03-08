package org.rs09.client.filestore.resources.configs.structs

import org.rs09.client.Linkable
import org.rs09.client.LinkableInt
import org.rs09.client.Node
import org.rs09.client.data.HashTable
import org.runite.client.Class95
import org.runite.client.DataBuffer
import org.runite.client.LinkableRSString
import org.runite.client.RSString

class StructDefinition: Node() {
    var values: HashTable<Linkable>? = null

    fun decode(buffer: DataBuffer) {
        while (true) {
            val opcode = buffer.readUnsignedByte()
            if (opcode == 0) break

            when (opcode) {
                249 -> {
                    val size = buffer.readUnsignedByte()
                    val values = HashTable<Linkable>(Class95.method1585(105, size))

                    for (i in 0 until size) {
                        val isString = buffer.readUnsignedByte() == 1
                        val key = buffer.readMedium()

                        val value = if (isString) LinkableRSString(buffer.readString())
                        else LinkableInt(buffer.readInt())

                        values.put(key.toLong(), value)
                    }

                    this.values = values
                }
                else -> throw IllegalArgumentException("unknown StructDefinition opcode $opcode")
            }
        }
    }

    fun getInt(key: Int, orElse: Int): Int {
        val values = this.values ?: return orElse
        val value = values[key.toLong()] as LinkableInt?
        return value?.value ?: orElse
    }

    fun getString(key: Int, orElse: RSString): RSString {
        val values = this.values ?: return orElse

        val value = values[key.toLong()] as LinkableRSString?
        return value?.value ?: orElse
    }
}