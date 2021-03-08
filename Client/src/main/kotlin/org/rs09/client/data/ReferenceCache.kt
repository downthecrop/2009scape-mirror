package org.rs09.client.data

import org.rs09.client.Node.Companion.splice
import org.rs09.client.data.reference.HardObjectReference
import org.rs09.client.data.reference.ObjectReference
import org.rs09.client.data.reference.SoftObjectReferenceTransformer.transform
import java.util.*

class ReferenceCache<T : Any>(capacity: Int) {
    private var remaining: Int
    private val capacity: Int
    private val history = Queue<ObjectReference<T>>()
    private val table: HashTable<ObjectReference<T>>

    fun put(value: T, key: Long) {
        this.remove(key)
        if (remaining == 0) {
            val last = history.poll()!!
            Objects.requireNonNull(last).unlink()
            last.unlinkNode()
        } else {
            remaining--
        }
        val reference = HardObjectReference(value)
        table.put(key, reference)
        history.offer(reference)
        reference.nodeKey = 0L
    }

    // TODO I added the return value here, but is that OK or will it break things
    fun remove(key: Long): T? {
        val previous = table[key]
        if (previous != null) {
            previous.unlink()
            previous.unlinkNode()
            remaining++
            return previous.getValue()
        }
        return null
    }

    fun hardCount(): Int {
        var count = 0
        var reference = history.getFront()
        while (reference != null) {
            if (!reference.isSoftReference()) {
                count++
            }
            reference = history.next()
        }
        return count
    }

    fun sweep(maximumAge: Int) {
        var reference: ObjectReference<T>? = history.getFront()
        while (null != reference) {
            if (!reference.isSoftReference()) {
                if (++reference.nodeKey > maximumAge.toLong()) {
                    val soft: ObjectReference<T> = transform(reference)
                    table.put(reference.linkableKey, soft)
                    splice(reference, soft)
                    reference.unlink()
                    reference.unlinkNode()
                }
            } else if (null == reference.getValue()) {
                reference.unlink()
                reference.unlinkNode()
                ++remaining
            }
            reference = history.next()
        }
    }

    fun clearSoftReferences() {
        var reference = history.getFront()
        while (reference != null) {
            if (reference.isSoftReference()) {
                reference.unlink()
                reference.unlinkNode()
                remaining++
            }
            reference = history.next()
        }
    }

    fun clear() {
        history.clear()
        table.clear()
        remaining = capacity
    }

    operator fun get(key: Long): T? {
        val reference = table[key] ?: return null
        val value = reference.getValue()
        return if (value == null) {
            reference.unlink()
            reference.unlinkNode()
            remaining++
            null
        } else {
            if (reference.isSoftReference()) {
                val hard = HardObjectReference<T>(value)
                table.put(reference.linkableKey, hard)
                history.offer(hard)
                hard.nodeKey = 0L
                reference.unlink()
                reference.unlinkNode()
            } else {
                history.offer(reference)
                reference.nodeKey = 0L
            }
            value
        }
    }

    init {
        var size = 1
        while (size + size < capacity) {
            size += size
        }
        this.capacity = capacity
        remaining = capacity
        table = HashTable(size)
    }
}