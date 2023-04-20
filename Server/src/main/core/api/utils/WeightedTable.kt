package core.api.utils

import core.tools.*

/**
  * Implementation of a weighted table that supports generic (particularly non-Item) types.
**/
class WeightedTable<T> : ArrayList<Pair<T?, Double>>() {
    var totalWeight: Double = 0.0

    fun add (element: T?, weight: Double) : Boolean {
        totalWeight += weight
        return super.add(Pair(element, weight))
    }

    fun remove (element: T?) : Boolean {
        var index = -1
        for ((i, pair) in this.withIndex()) {
            val (elem, _) = pair
            if (element == elem) {
                index = i
                break
            }
        }
        if (index == -1) return false

        this.removeAt(index)
        return true
    }

    override fun removeAt (index: Int) : Pair<T?, Double> {
        val (_, weight) = this[index]
        totalWeight -= weight
        return super.removeAt(index)
    }

    fun roll() : T? {
        if (this.size == 1) return this[0].component1()
        else if (this.size == 0) return null

        var shuffled = this.shuffled()
        var randWeight = RandomFunction.random(0.0, totalWeight)

        for ((element, weight) in shuffled) {
            randWeight -= weight
            if (randWeight <= 0)
                return element
        }

        return null
    }

    companion object {
        fun <T> create (vararg elements: Pair<T?, Double>) : WeightedTable<T> {
            var table = WeightedTable<T>()
            for ((element, weight) in elements)
                table.add (element, weight)
            return table
        }
    }
}
