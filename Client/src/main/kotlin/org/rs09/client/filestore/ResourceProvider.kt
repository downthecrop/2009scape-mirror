package org.rs09.client.filestore

// TODO Merge with Class151
abstract class ResourceProvider {
    abstract fun getReferenceTable(): ReferenceTable?

    abstract fun request(file: Int)

    abstract fun percentComplete(file: Int): Int

    abstract fun get(file: Int): ByteArray?
}