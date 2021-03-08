package org.rs09.client.data.reference

import org.rs09.client.Node

abstract class ObjectReference<T: Any>: Node() {
    abstract fun getValue(): T?
    abstract fun isSoftReference(): Boolean
}