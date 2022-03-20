package org.rs09.client.data

import org.rs09.client.Linkable
import java.util.*

class HashTable<T : Linkable?>(var capacity: Int) {
    private var retrievalLinkable: Linkable? = null
    private var iterationLinkable: Linkable? = null
    private var retrievalKey: Long = 0
    private var iterationIndex = 0

    var buckets: Array<Linkable?> = arrayOfNulls(capacity)

    fun clear() {
        var i = 0
        while (i < capacity) {
            val bucket = buckets[i]
            while (true) {
                val next = bucket!!.next
                if (bucket === next) {
                    ++i
                    break
                }
                next!!.unlink()
            }
        }
        iterationLinkable = null
        retrievalLinkable = null
    }

    fun first(): T? {
        iterationIndex = 0
        return next()
    }

    operator fun next(): T? {
        var next: Linkable?
        if (iterationIndex > 0 && iterationLinkable !== buckets[iterationIndex - 1]) {
            next = iterationLinkable
        } else {
            do {
                if (capacity <= iterationIndex) {
                    return null
                }
                next = buckets[iterationIndex++]!!.next
            } while (buckets[iterationIndex + -1] === next)
        }
        iterationLinkable = next!!.next
        return next as? T
    }

    fun put(key: Long, value: T) {
        if (value!!.previous != null) {
            value.unlink()
        }
        val bucket = buckets[(key and (capacity - 1).toLong()).toInt()]
        value.next = bucket
        value.linkableKey = key
        value.previous = bucket!!.previous
        value.previous!!.next = value
        value.next!!.previous = value
    }

    operator fun get(key: Long): T? {
        retrievalKey = key
        val head = Objects.requireNonNull(buckets)[(key and (capacity - 1).toLong()).toInt()]!!
        retrievalLinkable = head.next
        while (head !== retrievalLinkable) {
            if (retrievalLinkable!!.linkableKey == key) {
                val value = retrievalLinkable
                retrievalLinkable = retrievalLinkable!!.next
                return value as T?
            }
            retrievalLinkable = retrievalLinkable!!.next
        }
        retrievalLinkable = null
        return null
    }

    fun size(): Int {
        var size = 0
        for (i in 0 until capacity) {
            val bucket = buckets[i]
            var next = bucket!!.next
            while (next !== bucket) {
                next = next!!.next
                ++size
            }
        }
        return size
    }

    fun values(values: Array<T?>) {
        var count = 0
        for (i in 0 until capacity) {
            val head = buckets[i]
            var next = head!!.next
            while (next !== head) {
                values[count++] = next as T?
                next = next!!.next
            }
        }
    }

    fun nextInBucket(): T? {
        if (retrievalLinkable == null) {
            return null
        }
        val linkable = buckets[(retrievalKey and (-1 + capacity).toLong()).toInt()]
        while (linkable !== retrievalLinkable) {
            if (retrievalLinkable!!.linkableKey == retrievalKey) {
                val value = retrievalLinkable
                retrievalLinkable = retrievalLinkable!!.next
                return value as T?
            }
            retrievalLinkable = retrievalLinkable!!.next
        }
        retrievalLinkable = null
        return null
    }

    fun capacity(): Int {
        return capacity
    }

    init {
        for (i in 0 until capacity) {
            val bucket = Linkable()
            buckets[i] = bucket
            bucket.previous = bucket
            bucket.next = bucket
        }
    }
}
