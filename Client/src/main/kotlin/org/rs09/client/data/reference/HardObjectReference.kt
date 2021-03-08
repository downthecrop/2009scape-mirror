package org.rs09.client.data.reference

class HardObjectReference<T : Any>(private val value: T?) : ObjectReference<T>() {
    override fun getValue(): T? = value
    override fun isSoftReference(): Boolean = false
}