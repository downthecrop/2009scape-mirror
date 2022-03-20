package org.rs09.client.data

import org.rs09.client.Node

class Queue<T : Node?> {
    private val tail = Node()
    private var current: Node? = null

    fun size(): Int {
        var size = 0
        var var3 = tail.nextNode
        while (var3 !== tail) {
            var3 = var3!!.nextNode
            ++size
        }
        return size
    }

    fun getFront(): T? {
        val front = tail.nextNode
        if (tail === front) {
            current = null
            return null
        }
        current = front!!.nextNode
        return front as T?
    }

    fun poll(): T? {
        val next = tail.nextNode
        if (next === tail) {
            return null
        }
        next!!.unlinkNode()
        return next as T?
    }

    operator fun next(): T? {
        val current = current
        if (current === tail) {
            this.current = null
            return null
        }
        this.current = current!!.nextNode
        return current as T?
    }

    fun offer(node: T) {
        if (node!!.previousNode != null) {
            node.unlinkNode()
        }
        node.previousNode = tail.previousNode
        node.nextNode = tail
        node.previousNode!!.nextNode = node
        node.nextNode!!.previousNode = node
    }

    fun clear() {
        while (true) {
            val next = tail.nextNode
            if (tail === next) {
                current = null
                return
            }
            next!!.unlinkNode()
        }
    }

    init {
        tail.nextNode = tail
        tail.previousNode = tail
    }
}
