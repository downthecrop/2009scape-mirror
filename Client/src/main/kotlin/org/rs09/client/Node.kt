package org.rs09.client

open class Node : Linkable() {
    @JvmField
    var nodeKey: Long = 0
    @JvmField
    var previousNode: Node? = null
    @JvmField
    var nextNode: Node? = null

    fun unlinkNode() {
        if (previousNode != null) {
            previousNode!!.nextNode = nextNode
            nextNode!!.previousNode = previousNode
            nextNode = null
            previousNode = null
        }
    }

    companion object {
        @JvmStatic
        fun splice(insert: Node, target: Node) {
            if (target.previousNode != null) {
                target.unlinkNode()
            }
            target.previousNode = insert
            target.nextNode = insert.nextNode
            target.previousNode!!.nextNode = target
            target.nextNode!!.previousNode = target
        }
    }
}
