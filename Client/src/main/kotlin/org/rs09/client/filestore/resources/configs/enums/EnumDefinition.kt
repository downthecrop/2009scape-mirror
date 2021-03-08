package org.rs09.client.filestore.resources.configs.enums

import org.rs09.client.Linkable
import org.rs09.client.LinkableInt
import org.rs09.client.Node
import org.rs09.client.data.HashTable
import org.runite.client.*

class EnumDefinition : Node() {
    companion object {
        private val NULL = RSString.parse("null")
    }

    var keyType = 0
    var valueType = 0

    var values: HashTable<Linkable>? = null
    var valueLookup: HashTable<Linkable>? = null

    var defaultString = NULL
    var defaultInt = 0

    fun decode(buffer: DataBuffer) {
        while (true) {
            val opcode = buffer.readUnsignedByte()
            if (opcode == 0) break

            when (opcode) {
                1 -> keyType = buffer.readUnsignedByte()
                2 -> valueType = buffer.readUnsignedByte()
                3 -> defaultString = buffer.readString()
                4 -> defaultInt = buffer.readInt()
                5, 6 -> {
                    val size = buffer.readUnsignedShort()
                    val values = HashTable<Linkable>(Class95.method1585(94, size))

                    for (i in 0 until size) {
                        val key = buffer.readInt()

                        val value: Linkable = if (opcode == 5) LinkableRSString(buffer.readString())
                        else LinkableInt(buffer.readInt())

                        values.put(key.toLong(), value)
                    }

                    this.values = values
                }
                else -> throw IllegalArgumentException("unknown EnumDefinition opcode $opcode")
            }
        }
    }

    fun getString(key: Int): RSString {
        val values = this.values ?: return defaultString

        val value = values[key.toLong()] as LinkableRSString?
        return value?.value ?: defaultString
    }

    fun getInt(key: Int): Int {
        val values = this.values ?: return defaultInt

        val value = values[key.toLong()] as LinkableInt?
        return value?.value ?: defaultInt
    }

    private fun buildIntValueLookup() {
        val values = requireNotNull(this.values)
        val valueLookup = HashTable<Linkable>(values.capacity())

        var linkable = values.first() as LinkableInt?
        while (linkable != null) {
            val reversed = LinkableInt(linkable.linkableKey.toInt())
            valueLookup.put(linkable.value.toLong(), reversed)
            linkable = values.next() as LinkableInt?
        }

        this.valueLookup = valueLookup
    }

    private fun buildStringValueLookup() {
        val values = requireNotNull(this.values)
        val valueLookup = HashTable<Linkable>(values.capacity())

        var linkable = values.first() as LinkableRSString?
        while (linkable != null) {
            val reversed = Class3_Sub10(linkable.value, linkable.linkableKey.toInt())
            valueLookup.put(linkable.value.hash(), reversed)
            linkable = values.next() as LinkableRSString?
        }

        this.valueLookup = valueLookup
    }

    fun containsValue(value: Int): Boolean {
        if (values == null) {
            return false
        }

        if (valueLookup == null) {
            buildIntValueLookup()
        }

        val lookup = valueLookup ?: return false
        return lookup[value.toLong()] != null
    }

    fun containsValue(value: RSString): Boolean {
        if (values == null) {
            return false
        }

        if (valueLookup == null) {
            buildStringValueLookup()
        }

        val lookup = valueLookup ?: return false

        var head = lookup[value.hash()] as Class3_Sub10?
        while (head != null) {
            if (head.value.equalsString(value)) {
                return true
            }
            head = lookup.nextInBucket() as Class3_Sub10?
        }

        return false
    }

}