package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class BFPlayerState (val player: Player) {
    var container = BFOreContainer()
    val oresOnBelt = ArrayList<BFBeltOre>()
    var barsNeedCooled = false
        private set

    fun processOresIntoBars() : Boolean {
        if (barsNeedCooled && getVarbit(player, DISPENSER_STATE) == 1) {
            setVarbit(player, DISPENSER_STATE, 2, true)
            return false
        }
        if (getVarbit(player, DISPENSER_STATE) != 0 || !container.hasAnyOre())
            return false

        val xpReward = container.convertToBars(getStatLevel(player, Skills.SMITHING))
        if (xpReward > 0) {
            rewardXP(player, Skills.SMITHING, xpReward)
            setVarbit(player, DISPENSER_STATE, 1, true)
            barsNeedCooled = true
            return true
        }

        return false
    }

    fun updateOres() {
        val toRemove = ArrayList<BFBeltOre>()
        for (ore in oresOnBelt) {
            if (ore.tick())
                toRemove.add(ore)
        }
        oresOnBelt.removeAll(toRemove)
    }

    fun coolBars() {
        barsNeedCooled = false
        setVarbit(player, DISPENSER_STATE, 3, true)
    }

    fun checkBars() {
        if (getVarbit(player, DISPENSER_STATE) == 3)
            setVarbit(player, DISPENSER_STATE, 0, true)
    }

    fun hasBarsClaimable(): Boolean {
        return container.getTotalBarAmount() > 0
    }

    fun claimBars (bar: Bar, amount: Int) : Boolean {
        if (barsNeedCooled) return false

        val maxAmt = amount.coerceAtMost(freeSlots(player))
        if (maxAmt == 0) return false

        val reward = container.takeBars(bar, maxAmt) ?: return false
        addItem(player, reward.id, reward.amount)
        setBarClaimVarbits()
        return true
    }

    fun setBarClaimVarbits() {
        for (bar in Bar.values()) {
            val amount = container.getBarAmount(bar)
            val varbit = getVarbitForBar(bar)
            if (varbit == 0) continue

            if (getVarbit(player, varbit) == amount)
                continue

            setVarbit(player, varbit, amount, true)
        }

        var totalCoalNeeded = 0
        val level = getStatLevel(player, Skills.SMITHING)
        for ((id, amount) in container.getOreAmounts()) {
            val barType = BlastFurnace.getBarForOreId(id, container.coalAmount(), level)
            totalCoalNeeded += BlastFurnace.getNeededCoal(barType!!) * amount
        }

        setVarbit(player, COAL_NEEDED, (totalCoalNeeded - container.coalAmount()).coerceAtLeast(0))
    }

    private fun getVarbitForBar (bar: Bar) : Int {
        return when (bar) {
            Bar.BRONZE -> BRONZE_COUNT
            Bar.IRON -> IRON_COUNT
            Bar.STEEL -> STEEL_COUNT
            Bar.MITHRIL -> MITHRIL_COUNT
            Bar.ADAMANT -> ADDY_COUNT
            Bar.RUNITE -> RUNITE_COUNT
            Bar.SILVER -> SILVER_COUNT
            Bar.GOLD -> GOLD_COUNT
            else -> 0
        }
    }

    fun toJson() : JSONObject {
        val save = JSONObject()
        save["bf-ore-cont"] = container.toJson()

        val beltOres = JSONArray()
        for (ore in oresOnBelt) {
            beltOres.add(ore.toJson())
        }

        if (beltOres.isNotEmpty())
            save["bf-belt-ores"] = beltOres
        save["barsHot"] = barsNeedCooled

        return save
    }

    fun readJson (data: JSONObject) {
        oresOnBelt.clear()
        if (data.containsKey("bf-ore-cont")) {
            val contJson = data["bf-ore-cont"] as JSONObject
            container = BFOreContainer.fromJson(contJson)
        }
        if (data.containsKey("bf-belt-ores")) {
            val beltArray = data["bf-belt-ores"] as JSONArray
            for (oreObj in beltArray) {
                val oreInfo = oreObj as JSONObject
                val id = oreInfo["id"].toString().toInt()
                val amount = oreInfo["amount"].toString().toInt()
                val location = Location.fromString(oreInfo["location"].toString())
                val ore = BFBeltOre(player, id, amount, location)
                oresOnBelt.add(ore)
            }
        }
        if (data.containsKey("barsHot"))
            barsNeedCooled = data["barsHot"] as Boolean
    }

    companion object {
        val DISPENSER_STATE = 936
        val COAL_NEEDED = 940
        val BRONZE_COUNT = 941
        val IRON_COUNT = 942
        val STEEL_COUNT = 943
        val MITHRIL_COUNT = 944
        val ADDY_COUNT = 945
        val RUNITE_COUNT = 946
        val GOLD_COUNT = 947
        val SILVER_COUNT = 948
    }
}