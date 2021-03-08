package org.rs09.client.data.reference

import java.lang.ref.SoftReference

class SoftObjectReference<T : Any>(value: T?) : ObjectReference<T>() {
    private val value = SoftReference(value)

    override fun getValue(): T? = value.get()
    override fun isSoftReference(): Boolean = true
}