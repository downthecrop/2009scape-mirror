package content.region.kandarin.seers.quest.elementalworkshop2

import content.data.Quests
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_ALL_COGS
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_CART_LEVER
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_COG_PIN1
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_COG_PIN2
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_COG_PIN3
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_CRANE_POSN
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_CRANE_STATE
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_TANK_DOOR
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_EXTRACTOR_GUN
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_FAN_LEVER
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_FAN_STATE
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_HATCH
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_JIG_CART
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_MACHINERY
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_PIPE
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_TANK_RACK
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_EAST_VALVE
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_WEST_VALVE
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.ATTR_DIPPED
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.ATTR_FLATTENED
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.ATTR_JIGGED
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.ATTR_WATERED
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.ATTR_WINDED
import core.api.*
import core.api.utils.PlayerCamera
import core.game.dialogue.DialogueLabeller
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.*

class EW2Listeners : InteractionListener {

    /**
     * Quest structure and testing notes:
     *
     * I've divided the listeners into the below sections for easier parsing. Overall, the main EW2 workshop floor is divided
     * into 4 stations. A jig cart rolls between them. Each station only accepts one specific state of the jig cart, and only
     * progresses the jig cart by one state.
     *
     * Each station must be checked that it's in the correct state to allow the cart to travel around.
     *
     * Each station must only progress the cart state if it's the correct step, and otherwise fail gracefully.
     *
     * Start
     * 0 - Listeners for the dig site books, finding keys, stairs, jig cart, etc.
     *
     * EW2 main floor
     * 1 - crane - takes ele bar. outputs hot ele bar
     * 2 - anvil - takes hot ele bar. outputs smashed hot bar
     * 3 - water - takes smashed hot bar. outputs smashed cooled bar
     * 4 - fan   - takes smashed cooled bar. outputs primed bar
     *
     * EW2 basement
     * 5 - mind imbuement station. Takes primed bar and outputs mind bar.
     *
     */

    companion object {

        /**
         * This companion object stores helper functions to save and recall workshop state.
         * All the machinery states are stored in varbits and are updated in various listeners,
         * then synced in the various sync() functions. The whole workshop is synced using the
         * EW2Session start trigger, so when the player comes back, the workshop is where they left it.
         */

        // various states the crane can be in.
        val craneStates = intArrayOf(
            // Varbit 2644 = 0: Broken
            Scenery.OLD_CRANE_18638, // broken crane facing south, claw down
            Scenery.OLD_CRANE_18637, // broken crane facing south, claw up
            Scenery.OLD_CRANE_18636, // broken crane facing north, claw down
            Scenery.OLD_CRANE_18635, // broken crane facing north, claw up

            // Varbit 2644 = 1: Fixed/Empty
            Scenery.OLD_CRANE_18626, // fixed crane facing south, claw down
            Scenery.OLD_CRANE_18625, // fixed crane facing south, claw up
            Scenery.OLD_CRANE_18624, // fixed crane facing north, claw down
            Scenery.OLD_CRANE_18623, // fixed crane facing north, claw up

            // Varbit 2644 = 2: Elemental Bar
            Scenery.OLD_CRANE_18630, // fixed crane with ele bar facing south, claw down
            Scenery.OLD_CRANE_18629, // fixed crane with ele bar facing south, claw up
            Scenery.OLD_CRANE_18628, // fixed crane with ele bar facing north, claw down
            Scenery.OLD_CRANE_18627, // fixed crane with ele bar facing north, claw up

            // Varbit 2644 = 3: Hot Bar
            Scenery.OLD_CRANE_18634, // fixed crane with hot bar facing south, claw down
            Scenery.OLD_CRANE_18633, // fixed crane with hot bar facing south, claw up
            Scenery.OLD_CRANE_18632, // fixed crane with hot bar facing north, claw down
            Scenery.OLD_CRANE_18631  // fixed crane with hot bar facing north, claw up
        )

        // cart state is stored in varbit 2643
        val jigCartStates = intArrayOf(
            NPCs.JIG_CART_4913, // 0 empty
            NPCs.JIG_CART_4914, // 1 ele bar
            NPCs.JIG_CART_4915, // 2 hot ele bar
            NPCs.JIG_CART_4916, // 3 smashed hot bar
            NPCs.JIG_CART_4917, // 4 smashed cooled bar
            NPCs.JIG_CART_4918, // 5 primed bar
        )

        // locations the jig cart can be at
        val jigCartLocations = arrayOf(
            Location(1954, 5147, 2), // south (start)
            Location(1946, 5155, 2), // west (hammer)
            Location(1953, 5162, 2), // north (water)
            Location(1961, 5155, 2)  // east (fans)
        )

        // tank door states stored in varbit 2653
        val tankDoorStates = intArrayOf(
            Scenery.DOOR_18651, // 0 door closed
            Scenery.DOOR_18652, // 1 door opened, grabber inside
            Scenery.DOOR_18653, // 2 door opened, grabber out
            Scenery.DOOR_18654, // 3 door closed, hot bar inside
            Scenery.DOOR_18655, // 4 door opened, hot bar inside
            Scenery.DOOR_18656, // 5 door opened, hot bar out
            Scenery.DOOR_18657, // 6 door closed, cooled bar inside
            Scenery.DOOR_18658, // 7 door opened, cooled bar inside
            Scenery.DOOR_18659  // 8 door opened, cooled bar out
        )

        // steam press scenery. right now I just use the third one to make the hammer appear
        val steamPressStates = intArrayOf(
            Scenery.STEAM_PRESS_18643, // top of the steam press
            Scenery.STEAM_PRESS_18644, // press with ele bar cart
            Scenery.STEAM_PRESS_18645  // press with nothing under it
        )

        // position the crane based on varbit states
        fun syncCrane(player: Player) {
            val mat = getVarbit(player, EW2_CRANE_STATE)
            val pos = getVarbit(player, EW2_CRANE_POSN) // 0: South Down, 1: South Up, 2: North Down, 3: North Up
            val targetIndex = (mat * 4) + pos
            val newId = craneStates[targetIndex]

            val craneLoc = toInstance(player, Location(1952, 5143, 2))
            val currentObject = getScenery(craneLoc) ?: return

            if (!craneStates.contains(currentObject.id)) return
            if (currentObject.id == newId) return

            // get the crane's current position
            val currentIndex = craneStates.indexOf(currentObject.id)
            val currentPos = if (currentIndex != -1) currentIndex % 4 else pos

            // select the anim and sound
            var animId = -1
            var soundId = -1
            if (currentPos != pos) {
                when {
                    // lowering into lava
                    currentPos == 1 && pos == 0 -> {
                        animId = Animations.EW2_CRANE_LOWER_LAVA_4889
                        soundId = Sounds.EW2_CRANE_LOWER_3169
                    }
                    // raising from lava
                    currentPos == 0 && pos == 1 -> {
                        animId = Animations.EW2_CRANE_RAISE_LAVA_4890
                        soundId = Sounds.EW2_CRANE_LOWER_3169 // I can't find raise sound
                    }
                    // lowering to cart
                    currentPos == 3 && pos == 2 -> {
                        animId = Animations.EW2_CRANE_LOWER_CART_4887
                        soundId = Sounds.EW2_CRANE_LOWER_3169
                    }
                    // raising from cart
                    currentPos == 2 && pos == 3 -> {
                        animId = Animations.EW2_CRANE_RAISE_CART_4888
                        soundId = Sounds.EW2_CRANE_LOWER_3169
                    }
                    // pivot to cart
                    currentPos == 1 && pos == 3 -> {
                        animId = Animations.EW2_CRANE_PIVOT_4892
                        soundId = Sounds.EW2_CRANE_TURN_3170
                    }
                    // pivot to lava
                    currentPos == 3 && pos == 1 -> {
                        animId = Animations.EW2_CRANE_PIVOT_4892
                        soundId = Sounds.EW2_CRANE_TURN_3170
                    }
                }
            }

            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    // animate the scenery
                    0 -> {
                        if (animId != -1) {
                            val anim = Animation(animId)
                            animateScenery(player, currentObject, animId)
                            playAudio(player, soundId)
                            return@queueScript delayScript(player, anim.duration)
                        } else return@queueScript delayScript(player, 1)
                    }

                    // update the scenery
                    1 -> {
                        replaceScenery(currentObject, newId, -1)
                        syncCart(player)

                        // advance quest stage
                        if (newId == Scenery.OLD_CRANE_18634) {
                            if (!getAttribute(player, ATTR_DIPPED, false) &&
                                getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6
                            ) {
                                setAttribute(player, ATTR_DIPPED, true)
                            }
                        }
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            syncCart(player)
        }

        // update the jig cart based on varbit status
        fun syncCart(player: Player) {
            val cartState = getVarbit(player, EW2_JIG_CART)
            val cartPosIndex = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east
            val targetId = jigCartStates[cartState]
            val targetLocation = toInstance(player, jigCartLocations[cartPosIndex])
            val tankState = getVarbit(player, EW2_TANK_RACK) // if tank state is 3, 4, 6, 7 the cart is inside the tank and should be removed.

            // find cart
            val trackCenter = toInstance(player, Location(1953, 5155, 2))
            val existingCart = findLocalNPCs(trackCenter, 15).find { it.id in jigCartStates }
            if (existingCart != null) {

                // update appearance
                if (existingCart.id != targetId) {
                    existingCart.transform(targetId) // needs to be permanent so can't use the contentAPI transform
                }

                // failsafe to reset the cart where it's supposed to be. right now cart just teleports so this shouldn't trigger
                if (!existingCart.walkingQueue.isMoving && existingCart.location != targetLocation) {
                    teleport(existingCart, targetLocation)
                }

                // if cart is inside water tank, clear it
                if (tankState == 3 || tankState == 4 || tankState == 6 || tankState == 7) {
                    existingCart.clear()
                }

            // if there is no cart, spawn a new one (like if player is just entering)
            } else {
                // create the cart
                val newCart = NPC.create(targetId, targetLocation)
                newCart.init()

                // if cart is inside water tank, clear it
                if (tankState == 3 || tankState == 4 || tankState == 6 || tankState == 7) {
                    newCart.clear()
                }
            }
        }

        // values for the cogs
        val cogRange = (Scenery.COG_18667..Scenery.COG_18684).toList().toIntArray()   // 18 different cogs. I'm guessing that is enough to give each cog a static/fwd/rev state (probably).
        val smallCogs = intArrayOf(Scenery.COG_18673, Scenery.COG_18667, Scenery.COG_18670) // pin 1, pin 2, pin 3
        val medCogs = intArrayOf(Scenery.COG_18674, Scenery.COG_18668, Scenery.COG_18671)   // pin 1, pin 2, pin 3
        val largeCogs = intArrayOf(Scenery.COG_18675, Scenery.COG_18669, Scenery.COG_18672) // pin 1, pin 2, pin 3

        // spins the cogs
        fun syncCogs(player: Player) {
            val lever = getVarbit(player, EW2_FAN_LEVER)
            val cogs1 = getScenery(toInstance(player, Location(1959, 5157, 2)))
            val cogs2 = getScenery(toInstance(player, Location(1959, 5156, 2)))

            if (cogs1 != null && cogs2 != null) {
                if (lever == 1) {
                    // spin cogs
                    animateScenery(cogs1, Animations.EW2_COG_FWD_4906)
                    animateScenery(cogs2, Animations.EW2_COG_FWD_4906)
                } else {
                    // stop cogs
                    animateScenery(cogs1, -1)
                    animateScenery(cogs2, -1)
                }
            }
        }

        // visualizes the steam press. right now all it does is spawn the hammer part.
        fun syncSteamPress(player: Player) {
            val pressLoc = toInstance(player, Location(1946, 5155, 2))
            addScenery(core.game.node.scenery.Scenery(steamPressStates[2], pressLoc, 0))
        }

        // syncs the water tank state. this is dependent on the valve states and the door state. also changes cart state (cooling bar)
        fun syncTank(player: Player) {
            val waterIn = getVarbit(player, EW2_WEST_VALVE) // 0 if valve closed, 1 if open
            val waterOut = getVarbit(player, EW2_EAST_VALVE) // 0 if valve closed, 1 if open
            val cart = getVarbit(player, EW2_TANK_RACK) // whether the cart is in the tank or not
            val tankLoc = toInstance(player, location(1951, 5164, 2))
            val currentObject = getScenery(tankLoc)

            // both valves closed, nothing happens
            if (waterIn == 0 && waterOut == 0) return

            // drain is open, empty the tank
            if (waterIn == 0 && waterOut == 1) {
                // tank is full: play drain anim, then remove
                if (currentObject != null && currentObject.id == Scenery.WATER_TANK_18661) {
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animateScenery(player, currentObject, Animations.EW2_TANK_DRAIN_4903)
                                playAudio(player, Sounds.EW2_WATER_RACK_3184)
                                return@queueScript delayScript(player, Animation(Animations.EW2_TANK_DRAIN_4903).duration)
                            }
                            1 -> {
                                removeScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18661, tankLoc))
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }

                // tank is damp: just remove the scenery
                if (currentObject != null && currentObject.id == Scenery.WATER_TANK_18660) {
                    removeScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18660, tankLoc))
                    playAudio(player, Sounds.EW2_WATER_RACK_3184)
                }
            }

            // inlet is open, fill tank
            if (waterIn == 1 && waterOut == 0) {
                if (currentObject?.id == Scenery.WATER_TANK_18661) return // already full

                queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                    // add damp tank so we have something to animate
                    var animTarget = currentObject
                    if (animTarget == null || animTarget.id != Scenery.WATER_TANK_18660) {
                        animTarget = core.game.node.scenery.Scenery(Scenery.WATER_TANK_18660, tankLoc)
                        addScenery(animTarget)
                    }

                    when (stage) {
                        0 -> {
                            animateScenery(player, animTarget, Animations.EW2_TANK_FILL_4902)
                            playAudio(player, Sounds.EW2_WATER_FLOW_3183)
                            return@queueScript delayScript(player, Animation(Animations.EW2_TANK_FILL_4902).duration)
                        }

                        1 -> {
                            // swap damp for full tank
                            val existing = getScenery(tankLoc)
                            if (existing != null) removeScenery(core.game.node.scenery.Scenery(existing.id, tankLoc))
                            addScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18661, tankLoc))

                            // cooling the hot bar
                            if (cart == 3) {
                                setVarbit(player, EW2_TANK_RACK, 6, true)
                                setVarbit(player, EW2_JIG_CART, 4, true)
                                syncTankDoor(player)
                                playAudio(player, Sounds.EW2_COOL_BAR_3168)

                                if (!getAttribute(player, ATTR_WATERED, false)
                                    && getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6
                                ) {
                                    setAttribute(player, ATTR_WATERED, true)
                                }
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }

            // inlet and drain are open, tank is damp
            if (waterIn == 1 && waterOut == 1) {
                // tank is full: play drain anim, then swap to damp
                if (currentObject?.id == Scenery.WATER_TANK_18661) {
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animateScenery(player, currentObject, Animations.EW2_TANK_DRAIN_4903)
                                playAudio(player, Sounds.EW2_WATER_FLOW_3183)
                                return@queueScript delayScript(player, Animation(Animations.EW2_TANK_DRAIN_4903).duration)
                            }

                            1 -> {
                                removeScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18661, tankLoc))
                                addScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18660, tankLoc))
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    // tank is empty: add the damp scenery
                    addScenery(core.game.node.scenery.Scenery(Scenery.WATER_TANK_18660, tankLoc))
                    playAudio(player, Sounds.EW2_WATER_FLOW_3183)
                }
            }
        }

        // syncs the tank door state. this is dependent on the corkscrew lever and cart state
        fun syncTankDoor(player: Player) {
            val state = getVarbit(player, EW2_TANK_RACK)
            val doorLoc = toInstance(player, Location(1953, 5162, 2))
            val currentObject = getScenery(doorLoc) ?: return
            val targetId = tankDoorStates[state]

            if (currentObject.id == targetId) return

            var animId = -1
            var soundId = -1

            when {
                // door opening/closing
                (currentObject.id == Scenery.DOOR_18651 && targetId == Scenery.DOOR_18652)
                        || (currentObject.id == Scenery.DOOR_18654 && targetId == Scenery.DOOR_18655)
                        || (currentObject.id == Scenery.DOOR_18657 && targetId == Scenery.DOOR_18658) -> {
                    animId = Animations.EW2_DOOR_OPEN_4871
                    soundId = Sounds.EW2_DIAL_SPIN_2326
                }
                (currentObject.id == Scenery.DOOR_18652 && targetId == Scenery.DOOR_18651)
                        || (currentObject.id == Scenery.DOOR_18655 && targetId == Scenery.DOOR_18654)
                        || (currentObject.id == Scenery.DOOR_18658 && targetId == Scenery.DOOR_18657) -> {
                    animId = Animations.EW2_DOOR_CLOSE_4872
                    soundId = Sounds.EW2_DIAL_SPIN_2326
                }

                // rack extending
                (currentObject.id == Scenery.DOOR_18652 && targetId == Scenery.DOOR_18653)                 // extend empty
                        || (currentObject.id == Scenery.DOOR_18655 && targetId == Scenery.DOOR_18656)      // extend holding hot
                        || (currentObject.id == Scenery.DOOR_18658 && targetId == Scenery.DOOR_18659) -> { // extend holding cooled
                    animId = Animations.EW2_RACK_EXTEND_4873
                    soundId = Sounds.EW2_SCREW_TURN_3182
                }

                // rack retracting
                (currentObject.id == Scenery.DOOR_18653 && targetId == Scenery.DOOR_18652)                 // retract empty
                        || (currentObject.id == Scenery.DOOR_18653 && targetId == Scenery.DOOR_18655)      // retract grabbing hot cart
                        || (currentObject.id == Scenery.DOOR_18656 && targetId == Scenery.DOOR_18652)      // retract leaving hot cart
                        || (currentObject.id == Scenery.DOOR_18659 && targetId == Scenery.DOOR_18652) -> { // retract leaving cooled cart
                    animId = Animations.EW2_RACK_RETRACT_4874
                    soundId = Sounds.EW2_SCREW_TURN_3182
                }
            }

            // if the cart is being dropped off
            val isDroppingCart = (currentObject.id == Scenery.DOOR_18656 || currentObject.id == Scenery.DOOR_18659) && targetId == Scenery.DOOR_18652

            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        // spawn cart visually immediately so the empty rack retracts away from it
                        if (isDroppingCart) syncCart(player)

                        if (animId != -1) {
                            animateScenery(player, currentObject, animId)
                            if (soundId != -1) playAudio(player, soundId)
                            return@queueScript delayScript(player, Animation(animId).duration)
                        }
                        return@queueScript delayScript(player, 1)
                    }

                    1 -> {
                        // replaceScenery doesn't work here. Why? idfk.
                        removeScenery(core.game.node.scenery.Scenery(currentObject.id, doorLoc, 1))
                        addScenery(core.game.node.scenery.Scenery(targetId, doorLoc, 1))
                        // sync the cart after the door closes
                        if (!isDroppingCart) syncCart(player)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        // updates fan state. fun fact, the fan state varbit isn't saved even if you say to save it. gotta make sure it's set on entry.
        fun syncFan(player: Player) {
            val fanMachine = getVarbit(player, EW2_FAN_LEVER)
            setVarbit(player, EW2_FAN_STATE, fanMachine, true)
        }

        // helper function for the cog crates
        private fun searchCogCrate(player: Player, cogItem: Int, cogName: String) {
            if (!inInventory(player, cogItem)) {
                addItemOrDrop(player, cogItem)
                sendMessage(player, "You find a $cogName cog.")
            } else {
                sendMessage(player, "It's empty.")
            }
        }

        // helper function for cog placement
        private fun placeCog(player: Player, item: Item, varbit: Int) {
            face(player, player.location.transform(1, 0, 0))
            queueScript(player, 1) {
                val (cogVal, cogName) = when (item.id) {
                    Items.SMALL_COG_9726 -> 1 to "small"
                    Items.MEDIUM_COG_9725 -> 2 to "medium-sized"
                    Items.LARGE_COG_9724 -> 3 to "big"
                    else -> return@queueScript stopExecuting(player)
                }
                if (removeItem(player, item)) {
                    setVarbit(player, varbit, cogVal, true)
                    sendItemDialogue(player, item, "You put the $cogName cog onto the shaft.")
                    animate(player, Animations.HUMAN_PLACE_ITEM_537)
                    playAudio(player, Sounds.EW2_PLACE_COG_3178)
                    syncCogs(player)
                }
                return@queueScript stopExecuting(player)
            }
        }

        // helper function for the water valves
        private fun handleValve(player: Player, valveVarbit: Int) {
            val valveState = getVarbit(player, valveVarbit)
            val door = getVarbit(player, EW2_TANK_DOOR) // 0 if door closed, 1 if open

            if (door == 1) {
                sendMessage(player, "The valve won't turn, maybe the door needs to be closed first.")
                return
            }

            // only the west (fill) valve requires the pipe to be fixed first
            if (valveVarbit == EW2_WEST_VALVE && getVarbit(player, EW2_PIPE) == 0) {
                sendMessage(player, "You turn the valve but nothing happens, maybe the pipe needs to be repaired first.")
                return
            }

            lockInteractions(player, 4)
            animate(player, Animations.HUMAN_TURN_LARGE_VALVE_4861)

            val nextState = valveState xor 1
            setVarbit(player, valveVarbit, nextState, true)
            queueScript(player, 3, QueueStrength.SOFT) {
                syncTank(player)
                sendDialogue(player, if (nextState == 1) "You open the valve." else "You close the valve.")
                playAudio(player, Sounds.EW2_WATER_VALVE_3185)
                return@queueScript true
            }
        }

        // helper to convert static location coordinates to the dynamic instance location coordinates
        fun toInstance(player: Player, staticLoc: Location): Location {
            val session = player.getAttribute<EW2Session?>("ew2-session", null) ?: return staticLoc
            val offsetX = staticLoc.x - 1920
            val offsetY = staticLoc.y - 5120
            return session.base.transform(offsetX, offsetY, staticLoc.z)
        }
    }

    override fun defineListeners() {

        /** ------------------------ *
         *  SECTION 0 - START
         * ------------------------- */

        // bookcase at digsite where you start quest
        on(Scenery.BOOKCASE_17382, IntType.SCENERY, "search") { player, _ ->
            if (isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_I)
                && !hasAnItem(player, Items.BEATEN_BOOK_9717).exists()
                && hasLevelStat(player, Skills.MAGIC, 20)
                && hasLevelStat(player, Skills.SMITHING, 30)
            ) {
                sendItemDialogue(player, Items.BEATEN_BOOK_9717, "You find an old-looking book.")
                addItemOrDrop(player, Items.BEATEN_BOOK_9717)

                // advance quest stage
                if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) == 0) {
                    setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 1)
                    // the crane starts in this position per source
                    setVarbit(player, EW2_CRANE_POSN, 3, true)
                }
            } else {
                sendMessage(player, "You search the books...")
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        // the scroll that drops with the beaten book
        on(Items.SCROLL_9721, IntType.ITEM, "read") { player, _ ->
            openInterface(player, 222)
            setInterfaceText(player, "", 222, 1)
            setInterfaceText(player, "Down", 222, 2)
            setInterfaceText(player, "2 N", 222, 3)
            setInterfaceText(player, "2 E", 222, 4)
            setInterfaceText(player, "2 N", 222, 5)
            setInterfaceText(player, "2 E", 222, 6)
            setInterfaceText(player, "10 N", 222, 7)
            setInterfaceText(player, "4 W", 222, 8)
            setInterfaceText(player, "Pipe 3.", 222, 9)

            // advance quest stage
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) == 2) {
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 3)
            }

            // set the varbit that unlocks the machinery key (below)
            if (getVarbit(player, EW2_MACHINERY) == 0)
                setVarbit(player, EW2_MACHINERY, 1, true)
            return@on true
        }

        // machinery where you find the key.
        on(Scenery.MACHINERY_18594, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, Items.KEY_9722)) {
                sendItemDialogue(player, Items.KEY_9722, "You find a key hidden in the third pipe.")
                addItemOrDrop(player, Items.KEY_9722)
            } else {
                sendDialogue(player, "You search the machinery but find nothing.")
            }

            // advance quest stage
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) == 3) {
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 4)
            }
            return@on true
        }

        // unlocking the hatch
        onUseWith(IntType.SCENERY, Items.KEY_9722, Scenery.HATCH_18595) { player, _, _ ->
            sendItemDialogue(player, Items.KEY_9722, "The key fits the lock in the hatch perfectly.")
            setVarbit(player, EW2_HATCH, 1, true)
            return@onUseWith true
        }

        // unlocking the hatch
        on(Scenery.HATCH_18595, IntType.SCENERY, "open") { player, _ ->
            if (inInventory(player, Items.KEY_9722)) {
                sendItemDialogue(player, Items.KEY_9722, "The key fits the lock in the hatch perfectly.")
                setVarbit(player, EW2_HATCH, 1, true)
            } else sendMessage(player, "You're unable to open the locked hatch.")

            return@on true
        }

        // unlocked center hatch down to EW2
        on(Scenery.HATCH_18596, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "You climb down the stairs.")

            // start the instanced workshop session
            EW2Session(player).start(Location(1955, 5155, 2))

            // advance quest stage
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) == 4) {
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 5)
            }
            return@on true
        }

        // stairs back up to EW1
        on(Scenery.STAIRS_18598, IntType.SCENERY, "climb-up") { player, _ ->
            // non-instanced
            teleport(player, Location(2720, 9892, 0))
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        // stairs to EW2 lower level
        on(Scenery.STAIRWELL_18597, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, toInstance(player, Location(1948, 5158, 0)))
            sendMessage(player, "You climb down the stairs.")
            return@on true
        }

        // stairs from EW2 lower level back up to EW2 main level
        on(Scenery.STAIRS_18599, IntType.SCENERY, "climb-up") { player, _ ->
            sendMessage(player, "You climb up the stairs.")
            teleport(player, toInstance(player, Location(1948, 5157, 2)))
            return@on true
        }

        // body doors (locked)
        on(intArrayOf(Scenery.DOOR_18702, Scenery.DOOR_18703), IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door is locked.")
            return@on true
        }

        // stairs in the main level of the workshop
        on(intArrayOf(Scenery.DOOR_18702, Scenery.STAIRS_18610, Scenery.STAIRS_18611), IntType.SCENERY, "climb-up", "climb-down") { player, _ ->
            // locations
            val northTop = toInstance(player, Location(1959, 5159, 3))
            val northBottom = toInstance(player, Location(1957, 5159, 2))
            val southTop = toInstance(player, Location(1948, 5149, 3))
            val southBottom = toInstance(player, Location(1950, 5149, 2))

            // handler options
            val climb = Animation(Animations.HUMAN_CLIMB_STAIRS_828)
            val up = "You climb up the stairs."
            val down = "You climb down the stairs."

            // get destination
            when(player.location) {
                northBottom -> ClimbActionHandler.climb(player, climb, northTop, up)
                southBottom -> ClimbActionHandler.climb(player, climb, southTop, up)
                northTop -> ClimbActionHandler.climb(player, climb, northBottom, down)
                southTop -> ClimbActionHandler.climb(player, climb, southBottom, down)
            }

            return@on true
        }

        // schematic crate
        on(Scenery.SCHEMATIC_CRATE_18711, IntType.SCENERY, "take-from") { player, _ ->
            sendDialogueOptions(player, "There are two schematics here...", "Crane schematic", "Lever schematic")
            addDialogueAction(player) { _, buttonId ->
                when (buttonId) {
                    2 -> {
                        if (!inInventory(player, Items.CRANE_SCHEMATIC_9718)) {
                            addItemOrDrop(player, Items.CRANE_SCHEMATIC_9718)
                            sendItemDialogue(player, Items.CRANE_SCHEMATIC_9718, "You take a copy of the schematic drawing.")
                        } else sendDialogue(player, "You already have this schematic.")
                    }
                    3 -> {
                        if (!inInventory(player, Items.LEVER_SCHEMATIC_9719)) {
                            addItemOrDrop(player, Items.LEVER_SCHEMATIC_9719)
                            sendItemDialogue(player, Items.LEVER_SCHEMATIC_9719, "You take a copy of the schematic drawing.")
                        } else sendDialogue(player, "You already have this schematic.")
                    }
                }
            }
            return@on true
        }

        // lever that moves the cart between stations
        on(Scenery.LEVER_18620, IntType.SCENERY, "pull") { player, _ ->
            val currentPos = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east
            val cranePos = getVarbit(player, EW2_CRANE_POSN) // 2 if crane is down on north side
            val doorPos = getVarbit(player, EW2_TANK_DOOR) // 1 if door is open
            val tankState = getVarbit(player, EW2_TANK_RACK)
            val fanState = getVarbit(player, EW2_FAN_LEVER) // 1 if the fan is on


            // find the player's cart
            val existingCart = findLocalNPCs(player, jigCartStates, 32).firstOrNull()

            // no cart (should not happen)
            if (existingCart == null) {
                sendMessage(player, "CART MISSING. RE-ENTER THE WORKSHOP TO RESET IT, OR REPORT THIS.")
                return@on true
            }

            // crane must be up
            if (currentPos == 0 && cranePos == 2) {
                sendMessage(player, "You cannot do that while the claw is down.")
                return@on true
            }
            
            // door must be closed
            if (currentPos == 2 && doorPos == 1) {
                sendMessage(player, "You cannot do that while the door is open.")
                return@on true
            }
            
            // fan must be off
            if (currentPos == 3 && fanState == 1) {
                sendMessage(player, "You cannot do that while the fan is on.")
                return@on true
            }
            
            // cart must be currently stopped
            if (existingCart.walkingQueue.isMoving) {
                return@on true
            }
            
            // cart must not be in the water tank
            if (tankState == 3 || tankState == 4 || tankState == 6 || tankState == 7) {
                sendMessage(player, "You cannot do that while the cart is inside the tank.")
                return@on true
            }

            // set path
            val path = when (currentPos) {
                // south: south (crane) to west (hammer)
                0 -> arrayOf(Location(1954, 5147, 2), Location(1946, 5147, 2), Location(1946, 5155, 2))

                // west: west (hammer) to north (water)
                1 -> arrayOf(Location(1946, 5155, 2), Location(1946, 5162, 2), Location(1953, 5162, 2))

                // north: north (water) to east (fan)
                2 -> arrayOf(Location(1953, 5162, 2), Location(1961, 5162, 2), Location(1961, 5155, 2))

                // east: east (fan) to south (crane)
                3 -> arrayOf(Location(1961, 5155, 2), Location(1961, 5147, 2), Location(1954, 5147, 2))

                // else nothing
                else -> arrayOf()
            }.map { toInstance(player, it) }.toTypedArray()

            if (path.isNotEmpty()) {
                animate(player, Animations.HUMAN_PULL_LEVER_4909)

                // set the next varbit state
                val nextPos = (currentPos + 1) % 4
                setVarbit(player, EW2_CART_LEVER, nextPos, true)

                // TODO: there are weird bugs with walkingQueue. cart sometimes veers off randomly to one of the other destinations. just teleporting for now.
                // move
                /*
                existingCart.walkingQueue.reset()
                for (step in path) {
                    existingCart.walkingQueue.addPath(step.x, step.y)
                }
                */
                teleport(existingCart, path[2])
                playAudio(player, Sounds.EW2_CART_LOOP_3167)
            }

            return@on true
        }

        // take item from the jig cart
        on(jigCartStates, IntType.NPC, "take-from") { player, node ->
            val cartPos = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east

            // don't allow interaction if cart is not at south station
            if (cartPos != 0) {
                sendMessage(player, "I can't reach that.")
                return@on true
            }

            when(node.id) {
                NPCs.JIG_CART_4914 -> {
                    addItemOrDrop(player, Items.ELEMENTAL_METAL_2893)
                    sendItemDialogue(player, Items.ELEMENTAL_METAL_2893, "You take the elemental metal.")
                    animate(player, Animations.HUMAN_PLACE_ITEM_537)
                    setVarbit(player, EW2_JIG_CART, 0, true)
                }
                NPCs.JIG_CART_4915 -> sendMessage(player, "The bar is too hot to touch.")
                NPCs.JIG_CART_4916 -> sendMessage(player, "The bar is too hot to touch.")
                NPCs.JIG_CART_4917 -> sendMessage(player, "You should finish the process before taking the bar.")
                NPCs.JIG_CART_4918 -> {
                    addItemOrDrop(player, Items.PRIMED_BAR_9727)
                    sendItemDialogue(player, Items.PRIMED_BAR_9727, "You take the primed bar.")
                    setVarbit(player, EW2_JIG_CART, 0, true)

                    // advance quest stage
                    if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6) {
                        setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 7)
                    }
                }
            }
            syncCart(player)
            return@on true
        }

        // put bar on the jig cart
        onUseWith(IntType.NPC, intArrayOf(Items.ELEMENTAL_METAL_2893), NPCs.JIG_CART_4913) { player, used, _ ->
            val cranePos = getVarbit(player, EW2_CRANE_POSN)
            val cartPos = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east

            // don't allow interaction if cart is not at south station
            if (cartPos != 0) {
                sendMessage(player, "I can't reach that.")
                return@onUseWith true
            }

            // if crane is south down, it's blocking the cart.
            if (cranePos == 2) {
                sendMessage(player, "You cannot place that while the claw is down.")
                return@onUseWith true
            }

            setVarbit(player, EW2_JIG_CART, 1, true)
            playAudio(player, Sounds.EW2_PLACE_BAR_3177)
            syncCart(player)
            removeItem(player, used)

            // advance quest stage
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 5) {
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 6)
            }

            // attribute for quest log
            if (!getAttribute(player, ATTR_JIGGED, false) && getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6) {
                setAttribute(player, ATTR_JIGGED, true)
            }
            return@onUseWith true
        }

        /** ------------------------ *
         *  SECTION 1 - THE CRANE
         * ------------------------- */

        // requires the elemental claw to be smithed and attached to the crane

        // crane schematic
        on(Items.CRANE_SCHEMATIC_9718, IntType.ITEM, "read") { player, _ ->
            sendDialogue(player, "The schematic drawing details the construction and maintenance of a mechanical arm. One of the sections you notice is an explanation of how to make an elemental claw.")
            return@on true
        }

        // lever schematic (what lever is this even for?)
        on(Items.LEVER_SCHEMATIC_9719, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.EW2_LEVER_SCHEM_466)
            return@on true
        }

        // fix crane
        onUseWith(IntType.SCENERY, Items.CRANE_CLAW_9720, Scenery.OLD_CRANE_18636) { player, used, _ ->
            // check if already repaired
            if (getVarbit(player, EW2_CRANE_STATE) != 0) {
                sendMessage(player, "The crane is already repaired.")
                return@onUseWith true
            }
            
            // repair crane
            if (removeItem(player, used)) {
                sendItemDialogue(player, used, "You replace the broken claw.")
                setVarbit(player, EW2_CRANE_STATE, 1, true)
                playAudio(player, Sounds.EW2_PLACE_PIPE_3179)

                // update the crane state
                val craneLoc = toInstance(player, Location(1952, 5143, 2))
                val currentObject = getScenery(craneLoc)
                if (currentObject != null) {
                    replaceScenery(currentObject, Scenery.OLD_CRANE_18624, -1)
                }
            }
            return@onUseWith true
        }

        // west lever by crane. moves crane up and down.
        on(Scenery.AN_OLD_LEVER_18622, IntType.SCENERY, "pull") { player, _ ->
            val currentPos = getVarbit(player, EW2_CRANE_POSN)
            val nextPos = currentPos xor 1
            val craneMat = getVarbit(player, EW2_CRANE_STATE) // Crane State. 0 = broken, 1 = fixed/empty, 2 = ele bar, 3 = hot bar.
            val cartState = getVarbit(player, EW2_JIG_CART) // Jig Cart state. 0 = Empty, 1 = ele bar, 2 = hot bar, 3 = smashed hot, 4 = smashed cooled, 5 = primed
            val cartPos = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east

            // facing north: pick up and drop off bar. don't interact with the cart if the cart isn't here.
            if (currentPos == 3 && nextPos == 2) {
                if (craneMat == 1 && (cartState == 1 || cartState == 2) && cartPos == 0) {
                    // pick up
                    setVarbit(player, EW2_CRANE_STATE, cartState + 1, true)
                    setVarbit(player, EW2_JIG_CART, 0, true)
                } else if ((craneMat == 2 || craneMat == 3) && cartState == 0 && cartPos == 0) {
                    // drop off
                    setVarbit(player, EW2_JIG_CART, craneMat - 1, true)
                    setVarbit(player, EW2_CRANE_STATE, 1, true)
                }
            }

            // facing south: heat bar
            if (currentPos == 1 && nextPos == 0) {
                if (craneMat == 2) {
                    // if holding elemental bar, transform into hot bar
                    setVarbit(player, EW2_CRANE_STATE, 3, true)
                    playAudio(player, Sounds.EW2_CRANE_LOWER_3169)
                }
            }
            setVarbit(player, EW2_CRANE_POSN, nextPos, true)
            lockInteractions(player, 5)
            animate(player, Animations.HUMAN_PULL_LEVER_4909)
            syncCrane(player) // this also syncs the cart inside the crane function
            return@on true
        }

        // east lever by crane. rotates crane north and south
        on(Scenery.AN_OLD_LEVER_18621, IntType.SCENERY, "pull") { player, _ ->
            val currentPos = getVarbit(player,EW2_CRANE_POSN)

            // crane must be up to rotate
            if (currentPos == 0 || currentPos == 2) {
                sendMessage(player, "The arm cannot be rotated while the claw is down.")
                return@on true
            }

            val nextPos = currentPos xor 2

            setVarbit(player, EW2_CRANE_POSN, nextPos, true)
            lockInteractions(player, 5)
            animate(player, Animations.HUMAN_PULL_LEVER_4909)
            syncCrane(player)
            return@on true
        }

        /** ------------------------ *
         *  SECTION 2 - THE ANVIL
         * ------------------------- */

        // requires the junction box interface to be fixed

        // opens the junction box
        on(Scenery.JUNCTION_BOX_18641, IntType.SCENERY, "open") { player, _ ->
            openInterface(player, Components.JUNCTION_BOX_262)
            return@on true
        }

        // west lever, checks junction box status
        on(Scenery.AN_OLD_LEVER_18640, IntType.SCENERY, "pull") { player, _ ->

            if (JunctionBoxInterface.isSolutionCorrect(player)) {
                val cartState = getVarbit(player, EW2_JIG_CART) // Jig Cart state. 0 = Empty, 1 = ele bar, 2 = hot bar, 3 = smashed hot, 4 = smashed cooled, 5 = primed
                val cartPos = getVarbit(player, EW2_CART_LEVER) // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east

                // check if cart is here
                if (cartPos == 1) {
                    when (cartState) {
                        0 -> { // empty. nothing happens.
                            // "hardy" is the real word used. I thought it was a typo for 'hammer' but it's not. https://en.wikipedia.org/wiki/Hardy_tool
                            // also note during one of the source videos it's authentic that the press state shows a purple ele bar for a moment. weird. https://youtu.be/hv2rCofydXo?si=jDAmUSvAvUQAY2qG&t=184
                            sendDialogue(player, "You pull the lever and the press responds. The hardy comes down, stopping inches from the jig, then returns to its original position.")
                            playAudio(player, Sounds.EW2_PRESS_BAR_3180)
                            animate(player, Animations.HUMAN_PULL_LEVER_4909)
                        }
                        
                        1 -> { // ele bar. nothing happens.
                            sendDialogue(player, "You pull the lever and the press responds. The hardy comes down, hitting the elemental bar. The bar is not hot enough to flatten.")
                            playAudio(player, Sounds.EW2_PRESS_BAR_3180)
                            animate(player, Animations.HUMAN_PULL_LEVER_4909)
                        }
                        
                        2 -> { // hot ele bar. changes state to hot smashed bar.
                            setVarbit(player, EW2_JIG_CART, 3, true)
                            sendDialogue(player, "You pull the lever and the press responds. The hardy comes down, hitting the elemental bar. The bar has been flattened.")
                            playAudio(player, Sounds.EW2_PRESS_BAR_3180)
                            animate(player, Animations.HUMAN_PULL_LEVER_4909)
                            syncCart(player)
                            // attribute for quest log
                            if (!getAttribute(player, ATTR_FLATTENED, false) && getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6) {
                                setAttribute(player, ATTR_FLATTENED, true)
                            }
                        }
                        
                        else -> { // smashed hot, smashed cooled, or primed. nothing happens.
                            sendDialogue(player, "You pull the lever and the press responds. The hardy comes down, hitting the elemental bar. The bar is already flattened.")
                            playAudio(player, Sounds.EW2_PRESS_BAR_3180)
                            animate(player, Animations.HUMAN_PULL_LEVER_4909)
                        }
                    }
                } else {
                    // cart in wrong position
                    sendDialogue(player, "You pull the lever and the press operates, but the jig cart is not underneath it.")
                }
            } else {
                // The junction box is not configured correctly
                sendDialogue(player, "You pull the lever but nothing happens. Maybe the pipes are connected the wrong way round.")
                animate(player, Animations.HUMAN_PULL_LEVER_4909)
            }
            return@on true
        }

        /** ------------------------ *
         *  SECTION 3 - WATER
         * ------------------------- */

        // requires the pipe to be replaced

        // pipe crate
        on(Scenery.CRATE_18619, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, Items.PIPE_9723)) {
                addItemOrDrop(player, Items.PIPE_9723)
                sendMessage(player, "You find a pipe.")
            } else sendMessage(player, "It's empty.")
            return@on true
        }

        // fix the pipe
        onUseWith(IntType.SCENERY, Items.PIPE_9723, Scenery.PIPING_18650) { player, item, _ ->
            if (removeItem(player, item)) {
                animate(player, Animations.HUMAN_PLACE_ITEM_537)
                sendItemDialogue(player, item, "You replace the section of broken pipe.")
                playAudio(player, Sounds.EW2_PLACE_PIPE_3433)
                setVarbit(player, EW2_PIPE, 1, true)
            }
            return@onUseWith true
        }

        // north lever that opens and closes the door
        on(Scenery.AN_OLD_LEVER_18648, IntType.SCENERY, "pull") { player, _ ->
            val doorOpen = getVarbit(player, EW2_TANK_DOOR)
            val tankState = getVarbit(player, EW2_TANK_RACK)
            val tankScen = getScenery(location(1951, 5164, 2))

            animate(player, Animations.HUMAN_PULL_LEVER_4909)

            // if tank has water, don't open.
            if (tankScen != null) {
                sendDialogue(player, "You pull the lever but nothing happens. Maybe the tank needs to be empty first.")
                return@on true
            }

            // check the rack is not out
            if (tankState == 2 || tankState == 5 || tankState == 8) {
                sendDialogue(player, "You pull the lever but nothing happens. Maybe the rack needs retracting first.")
                return@on true
            }

            // update door status open/close
            if (doorOpen == 0) {
                setVarbit(player, EW2_TANK_DOOR, 1, true)
            } else {
                setVarbit(player, EW2_TANK_DOOR, 0, true)
            }

            // update door state
            val nextTankState = when (tankState) {
                1 -> 0   // Opened -> Closed
                0 -> 1   // Closed -> Opened
                4 -> 3   // Opened/Hot -> Closed/Hot
                3 -> 4   // Closed/Hot -> Opened/Hot
                7 -> 6   // Opened/Cooled -> Closed/Cooled
                6 -> 7   // Closed/Cooled -> Opened/Cooled
                else -> tankState
            }

            setVarbit(player, EW2_TANK_RACK, nextTankState, true)
            syncTankDoor(player)
            return@on true
        }

        // corkscrew lever that grabs and ungrabs the cart as long as the door is open
        on(Scenery.CORKSCREW_LEVER_18649, IntType.SCENERY, "turn") { player, _ ->
            val cartPos = getVarbit(player, EW2_CART_LEVER)
            val doorStatus = getVarbit(player, EW2_TANK_DOOR) // 0 = closed, 1 = open
            val cartState = getVarbit(player, EW2_JIG_CART)
            val tankState = getVarbit(player, EW2_TANK_RACK)

            // cart must be at north station
            if (cartPos != 2) {
                sendMessage(player, "The jig cart is not in position to be grabbed.")
                return@on true
            }

            // door must be open (this also requires tank to be empty)
            if (doorStatus == 0) {
                sendMessage(player, "The tank door must be open to use the corkscrew.")
                return@on true
            }

            playAudio(player, Sounds.EW2_SCREW_TURN_3182)
            animate(player, Animations.HUMAN_TURN_CORKSCREW_LEVER_4905)

            // either pull cart in or push it out. transfer between all the door open states
            queueScript(player, 1, QueueStrength.SOFT) {
                when (tankState) {

                    // 1 door opened, grabber inside. no cart update
                    1 -> setVarbit(player, EW2_TANK_RACK, 2, true) // 2 door opened, grabber out

                    2 -> { // 2 door opened, grabber out
                        // only grab the hot bar. otherwise, put the grabber back in
                        if (cartState == 3) {
                            setVarbit(player, EW2_TANK_RACK, 4, true) // 4 door opened, hot bar inside
                            // remove hot bar cart
                        } else {
                            setVarbit(player, EW2_TANK_RACK, 1, true) // 1 door opened, grabber inside
                        }
                    }

                    // 4 door opened, hot bar inside
                    4 -> setVarbit(player, EW2_TANK_RACK, 5, true) // 5 door opened, hot bar out

                    // 5 door opened, hot bar out. add hot bar cart
                    5 -> setVarbit(player, EW2_TANK_RACK, 1, true) // 1 door opened, grabber inside

                    // 7 door opened, cooled bar inside
                    7 -> setVarbit(player, EW2_TANK_RACK, 8, true) // 8 door opened, cooled bar out

                    // 8 door opened, cooled bar out. add cool bar cart
                    8 -> setVarbit(player, EW2_TANK_RACK, 1, true) // 1 door opened, grabber inside
                }
                syncTankDoor(player) // also syncs the cart inside there
                return@queueScript true
            }
            return@on true
        }

        // west water valve operates the water inflow. requires pipe to be fixed.
        on(Scenery.WATER_VALVE_18646, IntType.SCENERY, "turn") { player, _ ->
            handleValve(player, EW2_WEST_VALVE)
            return@on true
        }

        // east water valve operates the drain
        on(Scenery.WATER_VALVE_18647, IntType.SCENERY, "turn") { player, _ ->
            handleValve(player, EW2_EAST_VALVE)
            return@on true
        }

        /** ------------------------ *
         *  SECTION 4 - FAN
         * ------------------------- */

        // requires the cogs to be placed

        // this crate authentically has its own special search message!
        on(intArrayOf(Scenery.CRATE_18507, Scenery.CRATES_18508), IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You don't find anything interesting.")
            return@on true
        }

        // small cog crate
        on(Scenery.CRATE_18618, IntType.SCENERY, "search") { player, _ ->
            searchCogCrate(player, Items.SMALL_COG_9726, "small")
            return@on true
        }

        // medium cog crate
        on(Scenery.CRATES_18617, IntType.SCENERY, "search") { player, _ ->
            searchCogCrate(player, Items.MEDIUM_COG_9725, "medium-sized")
            return@on true
        }

        // large cog crate
        on(Scenery.CRATE_18616, IntType.SCENERY, "search") { player, _ ->
            searchCogCrate(player, Items.LARGE_COG_9724, "big")
            return@on true
        }

        // taking the cogs from the pins
        on(cogRange, IntType.SCENERY, "take") { player, node ->
            face(player, player.location.transform(1, 0, 0))
            queueScript(player, 1) {
                // determine what to take
                val cogToGive = when {
                    smallCogs.contains(node.id) -> Items.SMALL_COG_9726
                    medCogs.contains(node.id) -> Items.MEDIUM_COG_9725
                    largeCogs.contains(node.id) -> Items.LARGE_COG_9724
                    else -> -1
                }

                if (cogToGive != -1) {
                    // determine which varbit to reset
                    // Pin 1 uses: 18673, 18674, 18675
                    // Pin 2 uses: 18667, 18668, 18669
                    // Pin 3 uses: 18670, 18671, 18672
                    when (node.id) {
                        Scenery.COG_18673, Scenery.COG_18674, Scenery.COG_18675 -> setVarbit(player, EW2_COG_PIN1, 0, true)
                        Scenery.COG_18667, Scenery.COG_18668, Scenery.COG_18669 -> setVarbit(player, EW2_COG_PIN2, 0, true)
                        Scenery.COG_18670, Scenery.COG_18671, Scenery.COG_18672 -> setVarbit(player, EW2_COG_PIN3, 0, true)
                    }

                    addItemOrDrop(player, cogToGive)
                    syncCogs(player)
                    animate(player, Animations.HUMAN_PLACE_ITEM_537)
                    sendMessage(player, "You remove the cog from the shaft.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // use cog with pin 1
        // pin 1: 2655: val 0 is scenery 18665 (pin). val 1 is scenery 18673 (small cog). val 2 is scenery 18674 (med cog). val 3 is scenery 18675 (large cog).
        onUseWith(IntType.SCENERY, intArrayOf(Items.SMALL_COG_9726, Items.MEDIUM_COG_9725, Items.LARGE_COG_9724), Scenery.PIN_18665) { player, item, _ ->
            placeCog(player, item as Item, EW2_COG_PIN1)
            return@onUseWith true
        }

        // use cog with pin 2 // This listener brought to you by Player Name and his famous work with Arios RegionChunk fixing. Without that work it wouldn't have been possible to interact with this pin.
        // pin 2: 2656: val 0 is scenery 18664 (pin). val 1 is scenery 18667 (small cog). val 2 is scenery 18668 (med cog). val 3 is scenery 18669 (large cog)
        onUseWith(IntType.SCENERY, intArrayOf(Items.SMALL_COG_9726, Items.MEDIUM_COG_9725, Items.LARGE_COG_9724), Scenery.PIN_18664) { player, item, _ ->
            placeCog(player, item as Item, EW2_COG_PIN2)
            return@onUseWith true
        }

        // use cog with pin 3
        // pin 3: 2657: val 0 is scenery 18666 (pin). val 1 is scenery 18670 (small cog). val 2 is scenery 18671 (med cog). val 3 is scenery 18672 (large cog)
        onUseWith(IntType.SCENERY, intArrayOf(Items.SMALL_COG_9726, Items.MEDIUM_COG_9725, Items.LARGE_COG_9724), Scenery.PIN_18666) { player, item, _ ->
            placeCog(player, item as Item, EW2_COG_PIN3)
            return@onUseWith true
        }

        // fan lever.
        on(Scenery.AN_OLD_LEVER_18663, IntType.SCENERY, "pull") { player, _ ->
            animate(player, Animations.HUMAN_PULL_LEVER_4909)

            val allCogs = getVarbit(player, EW2_ALL_COGS)
            val lever = getVarbit(player, EW2_FAN_LEVER)
            val cartpos = getVarbit(player, EW2_CART_LEVER) // needs to be 3 to be at this station
            val cartstate = getVarbit(player, EW2_JIG_CART) // needs to be state 4: smashed cooled

            // check if the cogs are right.
            // pin 1 (2655) should have the small cog.
            // pin 2 (2656) should have the med cog.
            // pin 3 (2656) should have the large cog.
            if (allCogs == 57) {
                if (lever == 0) {
                    // rotate camera to view fan
                    val cam = PlayerCamera(player)
                    cam.setPosition(38, 28, 350)

                    // turn on
                    playAudio(player, Sounds.EW2_MACHINE_RUMBLE_3175)
                    playAudio(player, Sounds.EW2_FANBLADE_3174)
                    setVarbit(player, EW2_FAN_LEVER, 1, true)
                    syncFan(player)
                    syncCogs(player)
                    openDialogue(player, object : DialogueLabeller() {
                        override fun addConversation() {
                            line("The machine starts rumbling.")
                            line("The fan starts to blow.")
                        }
                    })

                    // check cart and cool if necessary. otherwise nothing should happen.
                    if (cartpos == 3 && cartstate == 4) {
                        setVarbit(player, EW2_JIG_CART, 5, true)
                        syncCart(player)
                        if (!getAttribute(player, ATTR_WINDED, false) && getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 6) {
                            setAttribute(player, ATTR_WINDED, true)
                        }
                    }
                } else { // lever == 1, turn off
                    sendDialogue(player, "The machine grinds to a standstill.")
                    setVarbit(player, EW2_FAN_LEVER, 0, true)
                    syncFan(player)
                    syncCogs(player)
                }
            } else { // allCogs != 57, cogs are not correct
                // TODO: Authentically there is an extra check where the fan can start backwards, but I'm simplifying it for my sanity. this would require reverse cog anims, too
                sendDialogue(player, "The machine starts up, but the fan does not blow.")
                playAudio(player, Sounds.EW2_MACHINE_RUMBLE_3175)
            }
            return@on true
        }

        /** ------------------------ *
         *  SECTION 5 - MIND IMBUE
         * ------------------------- */

        // use bar with extractor gun
        onUseWith(IntType.SCENERY, Items.PRIMED_BAR_9727, Scenery.EXTRACTOR_GUN_18693) { player, used, _ ->
            if (removeItem(player, used as Item)) {
                setVarbit(player, EW2_EXTRACTOR_GUN, 1, true)
                sendItemDialogue(player, Items.PRIMED_BAR_9727, "You place the primed bar under the device.")
                if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 8) {
                    setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 9)
                }
            }
            return@onUseWith true
        }

        // take primed bar from the extractor gun
        on(intArrayOf(Scenery.EXTRACTOR_GUN_18694), IntType.SCENERY, "take-from") { player, _ ->
            addItemOrDrop(player, Items.PRIMED_BAR_9727)
            setVarbit(player, EW2_EXTRACTOR_GUN, 0, true)
            sendItemDialogue(player, Items.PRIMED_BAR_9727, "You take the primed bar.")
            return@on true
        }

        // take mind bar from the extractor gun
        on(intArrayOf(Scenery.EXTRACTOR_GUN_18695), IntType.SCENERY, "take-from") { player, _ ->
            addItemOrDrop(player, Items.ELEMENTAL_MIND_BAR_9728)
            setVarbit(player, EW2_EXTRACTOR_GUN, 0, true)
            sendItemDialogue(player, Items.ELEMENTAL_MIND_BAR_9728, "You take the elemental mind bar.")
            return@on true
        }

        // sit in the extractor chair
        on(Scenery.EXTRACTOR_HAT_18690, IntType.SCENERY, "operate") { player, _ ->
            // check magic level. if you use the extractor too much, it can drain you too far.
            if (getDynLevel(player, Skills.MAGIC) < 20) {
                sendMessage(player, "You must have a Magic level of 20 and above to use the device.")
                return@on true
            }

            // start the cutscene
            if (getVarbit(player, EW2_EXTRACTOR_GUN) == 1) {
                // if bar is in place, charge it and drain magic
                EW2Cutscene(player, true).start()
            } else {
                // if bar is not in place (or if it's already a primed bar), hit player
                EW2Cutscene(player, false).start()
            }
            return@on true
        }
    }

    // fixes to get right interact locations
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.OLD_CRANE_18636), "use") { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1954, 5148, 2))
        }

        setDest(IntType.NPC, intArrayOf(NPCs.JIG_CART_4913), "use") { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1954, 5148, 2))
        }

        setDest(
            IntType.NPC, intArrayOf(
                NPCs.JIG_CART_4914, NPCs.JIG_CART_4915,
                NPCs.JIG_CART_4916, NPCs.JIG_CART_4917,
                NPCs.JIG_CART_4918
            ), "take-from"
        ) { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1954, 5148, 2))
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.PIPING_18650), "use") { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1953, 5167, 3))
        }

        // the double stacked pins and the cogs on them
        setDest(
            IntType.SCENERY, intArrayOf(
                Scenery.PIN_18664, Scenery.PIN_18665,
                Scenery.COG_18673, Scenery.COG_18674, Scenery.COG_18675,
                Scenery.COG_18667, Scenery.COG_18668, Scenery.COG_18669
            ), "use", "take"
        ) { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1958, 5157, 2))
        }

        // the single pin and its cogs
        setDest(
            IntType.SCENERY, intArrayOf(
                Scenery.PIN_18666,
                Scenery.COG_18670, Scenery.COG_18671, Scenery.COG_18672,
            ), "use", "take"
        ) { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1958, 5156, 2))
        }

        // the extractor gun
        setDest(
            IntType.SCENERY, intArrayOf(
            Scenery.EXTRACTOR_GUN_18693, Scenery.EXTRACTOR_GUN_18694,
            Scenery.EXTRACTOR_GUN_18695, Scenery.EXTRACTOR_GUN_18696
            ), "use", "take-from"
        ) { entity, _ ->
            return@setDest toInstance(entity as Player, Location(1961, 5148, 0))
        }
    }
}