package org.rs09.client.data

import org.rs09.client.Node
import java.util.*

// TODO Better name
class NodeCache<T : Node?>(private val capacity: Int) {
    private var empty = Node()
    private val table: HashTable<T>
    private var remaining: Int
    private val history = Queue<T>()

    operator fun get(key: Long): T? {
        val value = table[key]
        if (value != null) {
            history.offer(value)
        }
        return value
    }

    fun first(): T? {
        return table.first()
    }

    fun put(key: Long, value: T) {
        if (remaining == 0) {
            var history: Node? = history.poll()
            Objects.requireNonNull(history)!!.unlink()
            history!!.unlinkNode()
            if (empty === history) {
                history = this.history.poll()
                Objects.requireNonNull(history)!!.unlink()
                history!!.unlinkNode()
            }
        } else {
            remaining--
        }
        table.put(key, value)
        history.offer(value)
    }

    operator fun next(): T? {
        return table.next()
    }

    fun clear() {
        history.clear()
        table.clear()
        empty = Node()
        this.remaining = capacity
    }

    init {
        this.remaining = capacity
        var size = 1
        while (size - -size < remaining) {
            size += size
        }
        table = HashTable(size)
    }
}
