package core.api.utils

import core.game.node.item.Item
import core.tools.RandomFunction

class WeightedItem(var id: Int, var minAmt: Int, var maxAmt: Int, var weight: Double, var guaranteed: Boolean = false) {
    fun getItem(): Item {
        return Item(id,RandomFunction.random(minAmt,maxAmt))
    }
}