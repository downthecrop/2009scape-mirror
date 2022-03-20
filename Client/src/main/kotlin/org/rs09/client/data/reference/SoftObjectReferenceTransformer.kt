package org.rs09.client.data.reference

/**
 * Transforms all references it receives into soft references
 */
object SoftObjectReferenceTransformer : ReferenceTransformer() {
    override fun <T : Any> transform(from: ObjectReference<T>): ObjectReference<T> = SoftObjectReference(from.getValue())
}