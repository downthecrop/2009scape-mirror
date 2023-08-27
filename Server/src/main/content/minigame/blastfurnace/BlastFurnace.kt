package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.json.simple.JSONObject
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import kotlin.math.max

class BlastFurnace : MapArea, PersistPlayer, TickListener {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(bfArea)
    }

    override fun savePlayer(player: Player, save: JSONObject) {
        val state = playerStates[player.details.uid]
        if (state != null) {
            save["bf-state"] = state.toJson()
        }
    }

    override fun parsePlayer(player: Player, data: JSONObject) {
        playerStates.remove(player.details.uid)
        if (data.containsKey("bf-state")) {
            val stateObj = data["bf-state"] as JSONObject
            getPlayerState(player).readJson(stateObj)
        }
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            playersInArea.add(entity)
            val state = getPlayerState(entity)
            for (ore in state.oresOnBelt)
                ore.createNpc()
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            playersInArea.remove(entity)
            val state = getPlayerState(entity)
            for (ore in state.oresOnBelt) {
                ore.npcInstance?.clear()
                ore.npcInstance = null
            }
        }
    }

    override fun tick() {
        if (state.beltBroken || state.cogBroken) pedaler = null
        if (state.potPipeBroken || state.pumpPipeBroken) pumper = null
        state.tick(pumper != null, pedaler != null)

        if (playersInArea.size > 0) {
            updateVisuals()
            if (getWorldTicks() % 2 == 0) {
                updatePedaler()
                updatePumper()
            }
            processBars()
        }

        //Reset each tick
        pumper = null
        pedaler = null
    }

    private fun updatePumper() {
        if (pumper != null) {
            if (state.stoveTemp == 0) return
            if (state.furnaceTemp == 100 && RandomFunction.roll(5)) {
                impact(pumper!!, (0.2 * pumper!!.skills.maximumLifepoints).toInt())
                sendMessage(pumper!!, "A blast of hot air cooks you a bit.")
                pumper!!.pulseManager.clear()
                return
            }
            rewardXP(pumper!!, Skills.STRENGTH, 4.0)
        }
    }

    private fun updatePedaler() {
        if (pedaler == null) return
        var oresPedaled = false
        for (state in playerStates.values) {
            if (state.oresOnBelt.isEmpty()) continue
            state.updateOres()
            oresPedaled = true
        }
        if (oresPedaled) {
            rewardXP(pedaler!!, Skills.AGILITY, 2.0)
            pedaler!!.settings.runEnergy -= 2
        }
    }

    private fun updateVisuals() {
        sceneryController.updateStove(state.stoveTemp)
        sceneryController.updateBreakable(
            state.potPipeBroken,
            state.pumpPipeBroken,
            state.beltBroken,
            state.cogBroken
        )
        sceneryController.updateAnimations(pedaler != null, state.beltBroken, state.cogBroken)
    }

    private fun processBars() {
        if (state.furnaceTemp !in 51..66) return
        var totalProcessed = 0
        for (player in playersInArea) {
            if (getPlayerState(player).processOresIntoBars())
                totalProcessed++
        }
        if (totalProcessed == 0) return
        for (player in playersInArea)
            rewardXP(player, Skills.SMITHING, totalProcessed.toDouble())
    }

    companion object {
        val bfArea = ZoneBorders (1934, 4955, 1958, 4975)
        val playersInArea = ArrayList<Player>()
        val playerStates = HashMap<Int, BFPlayerState>()
        val state = BlastState()
        val sceneryController = BFSceneryController()
        var pedaler: Player? = null
        var pumper: Player? = null


        fun insideBorders (player: Player) : Boolean {
            return bfArea.insideBorder(player.location)
        }

        fun placeAllOre (p: Player, id: Int = -1, accountForSkill: Boolean = false) {
            val oreCounts = HashMap<Int, Int>()
            val oreContainer = getOreContainer(p)
            val level = if (accountForSkill) getStatLevel(p, Skills.SMITHING) else 99

            for (item in p.inventory.toArray()) {
                if (item == null) continue
                if (getNpcForOre(item.id) == -1) continue
                if (id != -1 && item.id != id) continue

                val bar = getBarForOreId(item.id, oreContainer.coalAmount(), level)!!
                if (bar.level > level) continue

                oreCounts[item.id] = (oreCounts[item.id] ?: 0) + item.amount
            }

            for ((oreId, amount) in oreCounts) {
                var maxAmt = oreContainer.getAvailableSpace(oreId, level)

                if (oreId == Items.COPPER_ORE_436 || oreId == Items.TIN_ORE_438)
                    maxAmt += (BlastConsts.ORE_LIMIT - getAmountOnBelt(p, oreId))

                if (oreId == Items.COAL_453)
                    maxAmt -= getAmountOnBelt(p, oreId)
                else
                    maxAmt -= getTotalOreOnBelt(p)

                maxAmt = maxAmt.coerceAtMost(amount).coerceAtLeast(0)
                if (maxAmt == 0) continue

                if (removeItem(p, Item(oreId, maxAmt)))
                    addOreToBelt(p, oreId, maxAmt)
            }
        }

        fun getPlayerState (p: Player) : BFPlayerState {
            if (playerStates[p.details.uid] != null)
                return playerStates[p.details.uid]!!
            val state = BFPlayerState(p)
            playerStates[p.details.uid] = state
            return state
        }

        fun getOreContainer (p: Player) : BFOreContainer {
            return getPlayerState(p).container
        }

        fun addOreToBelt (p: Player, id: Int, amount: Int) : BFBeltOre {
            val beltOre = BFBeltOre(p, id, amount, BFBeltOre.ORE_START_LOC)
            beltOre.createNpc()
            getPlayerState(p).oresOnBelt.add(beltOre)
            return beltOre
        }

        fun getAmountOnBelt (p: Player, id: Int) : Int {
            var total = 0
            for (ore in getPlayerState(p).oresOnBelt) {
                if (ore.id == id)
                    total += ore.amount
            }
            return total
        }

        fun getTotalOreOnBelt (p: Player) : Int {
            var total = 0
            for (ore in getPlayerState(p).oresOnBelt)
                if (ore.id != Items.COAL_453) total += ore.amount
            return total
        }

        fun getNeededCoal (bar: Bar) : Int {
            var coalAmount = 0

            if (bar.ores.size == 1)
                return coalAmount

            for (ore in bar.ores) {
                if (ore.id == Items.COAL_453) {
                    coalAmount = ore.amount
                    break
                }
            }
            if (coalAmount > 1) coalAmount /= 2
            return coalAmount
        }


        fun getBarForOreId (id: Int, coalAmount: Int, level: Int) : Bar? {
            return when (id) {
                Items.COPPER_ORE_436, Items.TIN_ORE_438 -> Bar.BRONZE
                Items.IRON_ORE_440 -> if (coalAmount >= 1 && level >= Bar.STEEL.level) Bar.STEEL else Bar.IRON
                else -> Bar.forOre(id)
            }
        }

        fun getNpcForOre (id: Int) : Int {
            return when (id) {
                Items.IRON_ORE_440 -> NPCs.IRON_ORE_2556
                Items.COPPER_ORE_436 -> NPCs.COPPER_ORE_2555
                Items.TIN_ORE_438 -> NPCs.TIN_ORE_2554
                Items.COAL_453 -> NPCs.COAL_2562
                Items.MITHRIL_ORE_447 -> NPCs.MITHRIL_ORE_2557
                Items.ADAMANTITE_ORE_449 -> NPCs.ADAMANTITE_ORE_2558
                Items.SILVER_ORE_442 -> NPCs.SILVER_ORE_2560
                Items.GOLD_ORE_444 -> NPCs.GOLD_ORE_2561
                Items.RUNITE_ORE_451 -> NPCs.RUNITE_ORE_2559
                else -> -1
            }
        }

        fun getEntranceFee (hasCharos: Boolean, smithLevel: Int) : Int {
            if (smithLevel >= BlastConsts.SMITH_REQ) return 0
            return if (hasCharos) BlastConsts.ENTRANCE_FEE / 2 else BlastConsts.ENTRANCE_FEE
        }

        fun enter (player: Player, feePaid: Boolean) {
            if (feePaid && !hasTimerActive<BFTempEntranceTimer>(player))
                registerTimer(player, BFTempEntranceTimer())
            teleport(player, BlastConsts.ENTRANCE_LOC)
        }
    }
}