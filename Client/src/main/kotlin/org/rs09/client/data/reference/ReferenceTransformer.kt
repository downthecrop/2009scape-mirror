package org.rs09.client.data.reference

abstract class ReferenceTransformer {
    abstract fun <T : Any> transform(from: ObjectReference<T>): ObjectReference<T>
}