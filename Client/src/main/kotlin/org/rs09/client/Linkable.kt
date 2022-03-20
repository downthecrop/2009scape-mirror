package org.rs09.client

open class Linkable {
    @JvmField
    var linkableKey = 0L
    @JvmField
    var next: Linkable? = null
    @JvmField
    var previous: Linkable? = null

    fun isLinked(): Boolean = previous != null

    fun unlink() {
        if (null != previous) {
            previous!!.next = next
            next!!.previous = previous
            previous = null
            next = null
        }
    }
}