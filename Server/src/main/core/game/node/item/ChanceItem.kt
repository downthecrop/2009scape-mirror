package core.game.node.item

import core.tools.RandomFunction

/**
 * Represents an item with a chance-rate.
 * @author Emperor
 * @author Bishop (Kotlin conversion)
 */

class ChanceItem(id: Int, @JvmField val minimumAmount: Int, @JvmField val maximumAmount: Int, @JvmField val chanceRate: Double) : Item(id, minimumAmount) {

    override fun toString(): String {
        return "ChanceItem ${super.toString()} [chanceRate=$chanceRate, minimumAmount=$minimumAmount, maximumAmount=$maximumAmount]"
    }

    fun rollWithQuantity(): Item {
        return Item(id, RandomFunction.random(minimumAmount, maximumAmount + 1))
    }

}