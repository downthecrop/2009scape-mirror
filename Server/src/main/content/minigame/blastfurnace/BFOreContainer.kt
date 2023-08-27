package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import content.minigame.blastfurnace.BlastConsts.BAR_LIMIT
import content.minigame.blastfurnace.BlastFurnace.Companion.getBarForOreId
import content.minigame.blastfurnace.BlastFurnace.Companion.getNeededCoal
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items

class BFOreContainer {
    private var coalRemaining = 0 //this is the actual important value needed for the calculations and is unit tested
    private var ores = Array(BlastConsts.ORE_LIMIT * 2) {-1}
    private var barAmounts = Array(9) {0}

    fun addCoal(amount: Int) : Int {
        val maxAdd = BlastConsts.COAL_LIMIT - coalRemaining
        val toAdd = amount.coerceAtMost(maxAdd)
        coalRemaining += toAdd
        return amount - toAdd
    }

    fun coalAmount() : Int {
        return coalRemaining
    }

    fun addOre (id: Int, amount: Int): Int {
        if (id == Items.COAL_453)
            return addCoal(amount)

        var limit = BlastConsts.ORE_LIMIT
        if (getBarForOreId(id, -1, 99) == Bar.BRONZE)
            limit *= 2

        var amountLeft = amount
        var maxAdd = getAvailableSpace(id, 99)
        for (i in 0 until limit) {
            if (ores[i] == -1) {
                ores[i] = id
                if (--amountLeft == 0 || --maxAdd == 0) break
            }
        }
        return amountLeft
    }

    fun getOreAmount (id: Int) : Int {
        if (id == Items.COAL_453)
            return coalAmount()

        var oreCount = 0
        for (i in 0 until BlastConsts.ORE_LIMIT) {
            if (ores[i] == id) oreCount++
        }
        return oreCount
    }

    fun indexOfOre (id: Int) : Int {
        for (i in ores.indices)
            if (ores[i] == id) return i
        return -1
    }

    fun getOreAmounts() : HashMap<Int, Int> {
        val map = HashMap<Int,Int>()
        for (ore in ores) {
            if (ore == -1) break
            map[ore] = (map[ore] ?: 0) + 1
        }
        return map
    }

    fun convertToBars(level: Int = 99) : Double {
        val newOres = Array(BlastConsts.ORE_LIMIT * 2) {-1}
        var oreIndex = 0
        var xpReward = 0.0
        for (i in 0 until BlastConsts.ORE_LIMIT) {
            val bar = getBarForOreId(ores[i], coalRemaining, level)

            if (bar == null) {
                ores[i] = -1
                continue
            }

            if (barAmounts[bar.ordinal] >= BAR_LIMIT) {
                newOres[oreIndex++] = ores[i]
                continue
            }

            val coalNeeded = getNeededCoal(bar)

            //special handling for bronze bar edge case, no other ore does this.
            if (bar == Bar.BRONZE) {
                val indexOfComplement = when (ores[i]) {
                    Items.COPPER_ORE_436 -> indexOfOre(Items.TIN_ORE_438)
                    Items.TIN_ORE_438 -> indexOfOre(Items.COPPER_ORE_436)
                    else -> -1
                }
                if (indexOfComplement == -1) {
                    newOres[oreIndex++] = ores[i]
                    continue
                }
                ores[indexOfComplement] = -1
            }

            if (coalRemaining >= coalNeeded) {
                coalRemaining -= coalNeeded
                ores[i] = -1
                barAmounts[bar.ordinal]++
                xpReward += bar.experience
            } else {
                newOres[oreIndex++] = ores[i]
            }
        }
        ores = newOres
        return xpReward
    }

    fun getBarAmount (bar: Bar) : Int {
        return barAmounts[bar.ordinal]
    }

    fun getTotalBarAmount() : Int {
        var total = 0
        for (i in barAmounts) total += i
        return total
    }

    fun takeBars (bar: Bar, amount: Int) : Item? {
        val amt = amount.coerceAtMost(barAmounts[bar.ordinal])
        if (amt == 0) return null

        barAmounts[bar.ordinal] -= amt
        return Item(bar.product.id, amt)
    }

    fun getAvailableSpace (ore: Int, level: Int = 99) : Int {
        if (ore == Items.COAL_453)
            return BlastConsts.COAL_LIMIT - coalRemaining

        var freeSlots = 0
        val bar = getBarForOreId(ore, coalRemaining, level)!!
        val oreAmounts = HashMap<Int,Int>()
        for (i in 0 until BlastConsts.ORE_LIMIT)
            if (ores[i] == -1) {
                var oreLimit = BlastConsts.ORE_LIMIT
                if (bar == Bar.BRONZE) oreLimit *= 2
                freeSlots = oreLimit - i
                break
            }
            else oreAmounts[ores[i]] = (oreAmounts[ores[i]] ?: 0) + 1

        val currentAmount = oreAmounts[ore] ?: 0
        freeSlots = (BlastConsts.ORE_LIMIT - currentAmount).coerceAtMost(freeSlots)

        return (freeSlots - getBarAmount(bar)).coerceAtLeast(0)
    }

    fun hasAnyOre() : Boolean {
        return ores[0] != -1
    }

    fun toJson() : JSONObject {
        val root = JSONObject()
        val ores = JSONArray()
        val bars = JSONArray()

        for (ore in this.ores)
            ores.add(ore.toString())
        for (amount in barAmounts)
            bars.add(amount.toString())

        root["ores"] = ores
        root["bars"] = bars
        root["coal"] = coalRemaining.toString()
        return root
    }

    companion object {
        fun fromJson (root: JSONObject) : BFOreContainer {
            val cont = BFOreContainer()
            val jsonOres = root["ores"] as? JSONArray ?: return cont
            val jsonBars = root["bars"] as? JSONArray ?: return cont


            for (i in 0 until BlastConsts.ORE_LIMIT)
                cont.ores[i] = jsonOres[i].toString().toIntOrNull() ?: -1
            for (i in 0 until 9)
                cont.barAmounts[i] = jsonBars[i].toString().toIntOrNull() ?: 0
            cont.coalRemaining = root["coal"].toString().toIntOrNull() ?: 0
            return cont
        }
    }
}