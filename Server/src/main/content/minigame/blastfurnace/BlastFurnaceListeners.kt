package content.minigame.blastfurnace

import content.region.misc.keldagrim.handlers.BlastFurnaceDoorDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Items


class BlastFurnaceListeners : InteractionListener {
    companion object {
        var initialized = false
        val PUMP_LOC = Location.create(1950, 4961, 0)
        val PUMP_FACELOC = Location.create(1949, 4961, 0)
        val PEDAL_LOC = Location.create(1947, 4966, 0)
        val PEDAL_DISMOUNT = Location.create(1947, 4967, 0)
        val PEDAL_FACELOC = Location.create(1946, 4966, 0)
        val PUMP_ANIM = 2432
        val PEDAL_ANIM = 2433
        val PEDAL_SCENERY = getScenery(PEDAL_LOC)!!
        val PUMP_SCENERY = getScenery(PUMP_LOC)!!
        val validOreIds = intArrayOf(
            Items.IRON_ORE_440,
            Items.COPPER_ORE_436,
            Items.TIN_ORE_438,
            Items.COAL_453,
            Items.MITHRIL_ORE_447,
            Items.ADAMANTITE_ORE_449,
            Items.SILVER_ORE_442,
            Items.GOLD_ORE_444,
            Items.RUNITE_ORE_451
        )
    }

    override fun defineListeners() {
        if (initialized) return
        initialized = true

        on (BlastConsts.STAIR_ENTRANCE_ID, IntType.SCENERY, "climb-down") { p, _ ->
            val hasCharos = inEquipment(p, Items.RING_OF_CHAROS_4202) || inEquipment(p, Items.RING_OF_CHAROSA_6465)
            val fee = BlastFurnace.getEntranceFee(hasCharos, getStatLevel(p, Skills.SMITHING))
            if (fee > 0 && !hasTimerActive<BFTempEntranceTimer>(p)) {
                openDialogue(p, BlastFurnaceDoorDialogue(fee))
                return@on true
            }
            BlastFurnace.enter(p, false)
            return@on true
        }

        on (BlastConsts.STAIR_EXIT_ID, IntType.SCENERY, "climb-up") { p, _ ->
            teleport(p, BlastConsts.EXIT_LOC)
            return@on true
        }

        on (BlastConsts.PUMP, IntType.SCENERY, "operate") { p, _ ->
            if (getDynLevel(p, Skills.STRENGTH) >= 30 && BlastFurnace.pumper == null) {
                forceMove(p, p.location, PUMP_LOC, 0, 30) {
                    animate(p, PUMP_ANIM)
                    removeScenery(PUMP_SCENERY)
                    submitIndividualPulse(p, object : Pulse() {
                        override fun pulse(): Boolean {
                            face(p, PUMP_FACELOC)
                            animate(p, PUMP_ANIM)
                            BlastFurnace.pumper = p
                            return false
                        }

                        override fun stop() {
                            resetAnimator(p)
                            BlastFurnace.pumper = null
                            addScenery(PUMP_SCENERY)
                            super.stop()
                        }
                    })
                }
            }
            else if (BlastFurnace.pumper != null)
                sendMessage(p, "Someone else is already doing that.")
            else
                sendMessage(p, "You need a Strength level of 30 to do that.")
            return@on true
        }
        setDest(IntType.SCENERY, BlastConsts.PUMP) { _, _ -> Location.create(1951, 4961, 0) }

        on (BlastConsts.PEDALS, IntType.SCENERY, "pedal") { p, n ->
            if (getDynLevel(p, Skills.AGILITY) > 30 && BlastFurnace.pedaler == null) {
                forceMove(p, p.location, PEDAL_LOC, 0, 30) {
                    animate(p, PEDAL_ANIM)
                    removeScenery(PEDAL_SCENERY)
                    submitIndividualPulse(p, object : Pulse() {
                        override fun pulse(): Boolean {
                            if (p.settings.runEnergy < 1.0) {
                                teleport(p, PEDAL_DISMOUNT)
                                return true
                            }
                            face(p, PEDAL_FACELOC)
                            animate(p, PEDAL_ANIM)
                            BlastFurnace.pedaler = p
                            return false
                        }

                        override fun stop() {
                            resetAnimator(p)
                            BlastFurnace.pedaler = null
                            addScenery(PEDAL_SCENERY)
                            super.stop()
                        }
                    })
                }
            }
            else if (BlastFurnace.pedaler != null)
                sendMessage(p, "Someone else is already doing that.")
            else
                sendMessage(p, "You need an Agility level of 30 to do that.")
            return@on true
        }
        setDest(IntType.SCENERY, BlastConsts.PEDALS) { _, _ -> Location.create(1948, 4966, 0) }

        on (BlastConsts.COKE, SCENERY, "collect") { player, _ ->
            if (inInventory(player, Items.SPADE_952, 1)) {
                if(removeItem(player, Items.SPADE_952, Container.INVENTORY) && addItem(player, Items.SPADEFUL_OF_COKE_6448, 1)) {
                    lockInteractions(player,1)
                    animate(player, 2441)
                }
            } else {
                sendMessage(player, "You need a spade to do this!")
            }
            return@on true
        }

        on (Items.SPADEFUL_OF_COKE_6448, ITEM, "empty") {p, n ->
            if (removeItem(p, n)) addItem(p, Items.SPADE_952)
            return@on true
        }

        on (BlastConsts.STOVE, SCENERY, "refuel") { player, _ ->
            if (inInventory(player, Items.SPADEFUL_OF_COKE_6448, 1))  {
                if (BlastFurnace.state.cokeInStove >= BlastConsts.COKE_LIMIT) {
                    sendMessage(player, "The stove is already full of coke!")
                    return@on true
                }
                if (getDynLevel(player, Skills.FIREMAKING) < 30) {
                    sendMessage(player, "You need a Firemaking level of 30 to do this.")
                    return@on true
                }
                animate(player, 2442)
                lock(player,2)
                submitIndividualPulse(player, object : Pulse() {
                    override fun pulse(): Boolean {
                        return if(removeItem(player, Items.SPADEFUL_OF_COKE_6448, Container.INVENTORY) && addItem(player, Items.SPADE_952, 1)) {
                            animate(player, 2443)
                            rewardXP(player, Skills.FIREMAKING, 5.0)
                            BlastFurnace.state.addCoke(1)
                            true
                        } else {
                            false
                        }
                    }
                })
            } else {
                sendMessage(player,"You need some coke to do that!")
            }
            return@on true
        }

        on (BlastConsts.BELT, IntType.SCENERY, "put-ore-on") { p, _ ->
            openDialogue(p, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendDialogue(p, "Really place all your ore on the belt?").also { stage++ }
                        1 -> showTopics(
                            Topic("Yes.", 100, true),
                            Topic("Nevermind.", END_DIALOGUE, true)
                        )
                        100 -> {
                            end()
                            BlastFurnace.placeAllOre(p, accountForSkill = true)
                        }
                    }
                }
            })
            return@on true
        }

        onUseWith (IntType.SCENERY, validOreIds, BlastConsts.BELT) { p, used, _ ->
            BlastFurnace.placeAllOre(p, used.id, accountForSkill = true)
            return@onUseWith true
        }

        on(BlastConsts.TEMP_GAUGE, IntType.SCENERY, "read") { p, _ ->
            openInterface(p, 30)
            return@on true
        }

        on(BlastConsts.DISPENSER, IntType.SCENERY, "search", "take") { p, _ ->
            openInterface(p, 28)
            return@on true
        }

        onUseWith(SCENERY, Items.BUCKET_OF_WATER_1929, *BlastConsts.DISPENSER){ p, _, _ ->
            when (getVarbit(p, BFPlayerState.DISPENSER_STATE)) {
                1,2 -> {
                    var barsCooled = false
                    for (player in BlastFurnace.playersInArea) {
                        val state = BlastFurnace.getPlayerState(player)
                        if (state.barsNeedCooled) {
                            state.coolBars()
                            barsCooled = true
                        }
                    }
                    if (barsCooled) {
                        removeItem(p, Items.BUCKET_OF_WATER_1929, Container.INVENTORY)
                        addItem(p, Items.BUCKET_1925)
                    }
                }
                0 -> sendDialogue(p,"There's nothing to cool off!")
                3 -> sendDialogue(p,"These bars have already cooled off!")
            }
            return@onUseWith true
        }

        on(BFSceneryController.BROKEN_POT_PIPE, SCENERY, "repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if (!BlastFurnace.state.potPipeBroken) {
                    sendMessage(player, "You can't fix something that isn't broken.")
                    return@on true
                }
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.state.potPipeBroken = false
                } else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            } else {
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(BFSceneryController.BROKEN_PUMP_PIPE, SCENERY, "repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if (!BlastFurnace.state.pumpPipeBroken) {
                    sendMessage(player, "You can't fix something that isn't broken.")
                    return@on true
                }
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.state.pumpPipeBroken = false
                } else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            } else {
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(BFSceneryController.BROKEN_BELT, SCENERY, "repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if (!BlastFurnace.state.beltBroken) {
                    sendMessage(player, "You can't fix something that isn't broken.")
                    return@on true
                }
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.state.beltBroken = false
                } else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            } else {
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(BFSceneryController.BROKEN_COG, SCENERY, "repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if (!BlastFurnace.state.cogBroken) {
                    sendMessage(player, "You can't fix something that isn't broken.")
                    return@on true
                }
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.state.cogBroken = false
                } else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            } else {
                sendDialogue(player,"I need 30 Craft in order to do this")
            }
            return@on true
        }

        on(BlastConsts.SINK, SCENERY,"fill-bucket"){ player, _ ->
            player.pulseManager.run(object : Pulse(1){
                override fun pulse(): Boolean {
                    if(removeItem(player, Items.BUCKET_1925))
                    {
                        animate(player, 832)
                        sendMessage(player, "You fill the bucket from the sink.")
                        addItemOrDrop(player, Items.BUCKET_OF_WATER_1929)
                    }
                    return true
                }
            })
            return@on true
        }
    }
}
