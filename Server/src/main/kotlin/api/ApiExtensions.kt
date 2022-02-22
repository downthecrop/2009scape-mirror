package api

import core.game.node.item.Item

fun IntRange.toIntArray(): IntArray {
    if (last < first)
        return IntArray(0)

    val result = IntArray(last - first + 1)
    var index = 0
    for (element in this)
        result[index++] = element
    return result
}

fun Int.asItem() : Item {
    return Item(this)
}

fun Collection<IntArray>.toIntArray() : IntArray {
    val list = ArrayList<Int>()
    this.forEach { arr -> arr.forEach { list.add(it) } }
    return list.toIntArray()
}