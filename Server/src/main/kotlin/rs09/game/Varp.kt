package rs09.game

import core.game.node.entity.player.Player

/**
 * A class that represents Varps and aids in modifying/calculating them
 * @author Ceikry
 */
class Varp(val index: Int) {
    val varbits = HashMap<Int, Int>()
    var save = false

    fun setVarbit(offset: Int, value: Int): Varp{
        varbits[offset] = value
        return this
    }

    fun getValue(): Int{
        var config = 0
        for((index,value) in varbits.entries){
            config = config or (value shl index)
        }
        return config
    }

    fun getVarbit(offset: Int): Int {
        return getVarbitValue(offset)
    }

    fun getVarbitValue(offset: Int): Int {
        return varbits[offset] ?: 0
    }

    fun send(player: Player){
        player.packetDispatch.sendVarp(this)
    }

    fun clear(): Varp{
        varbits.clear()
        return this
    }

    fun clearBitRange(first : Int, last : Int){
        for(i in first..last) varbits.remove(i)
    }

    fun getBitRangeValue(first: Int, last: Int): Int{
        var product = 0
        for (i in first..last)
        {
            val value = varbits[i] ?: 0
            product = product or (value shl i)
        }
        return product
    }
}