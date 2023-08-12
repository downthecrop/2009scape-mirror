package content.region.kandarin.ardougne.quest.clocktower

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class ClockTowerListener : InteractionListener {

    companion object {
        val DOWN_ANIMATION = Animation(2140)
        val UP_ANIMATION = Animation(2139)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, intArrayOf(Items.WHITE_COG_20, Items.BLACK_COG_21, Items.BLUE_COG_22, Items.RED_COG_23), Scenery.CLOCK_SPINDLE_25) { player, _, _ ->
            sendMessage(player, "The cog doesn't seem to fit.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, intArrayOf(Items.WHITE_COG_20, Items.BLACK_COG_21, Items.BLUE_COG_22, Items.RED_COG_23), Scenery.CLOCK_SPINDLE_26) { player, _, _ ->
            sendMessage(player, "The cog doesn't seem to fit.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, intArrayOf(Items.WHITE_COG_20, Items.BLACK_COG_21, Items.BLUE_COG_22, Items.RED_COG_23), Scenery.CLOCK_SPINDLE_27) { player, _, _ ->
            sendMessage(player, "The cog doesn't seem to fit.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, intArrayOf(Items.WHITE_COG_20, Items.BLACK_COG_21, Items.BLUE_COG_22, Items.RED_COG_23), Scenery.CLOCK_SPINDLE_28) { player, _, _ ->
            sendMessage(player, "The cog doesn't seem to fit.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.WHITE_COG_20, Scenery.CLOCK_SPINDLE_31) { player, _, _ ->
            if (!getAttribute(player, ClockTower.attributeWhiteCog, false)) {
                if (removeItem(player, Items.WHITE_COG_20)) {
                    sendMessage(player, "The cog fits perfectly.")
                    setAttribute(player, ClockTower.attributeWhiteCog, true)
                    if (!isQuestComplete(player, ClockTower.questName)) {
                        setQuestStage(player, ClockTower.questName, getQuestStage(player, ClockTower.questName) + 1)
                    }
                }
            } else {
                sendMessage(player, "The cog has already been placed.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BLACK_COG_21, Scenery.CLOCK_SPINDLE_30) { player, _, _ ->
            if (!getAttribute(player, ClockTower.attributeBlackCog, false)) {
                if (removeItem(player, Items.BLACK_COG_21)) {
                    sendMessage(player, "The cog fits perfectly.")
                    setAttribute(player, ClockTower.attributeBlackCog, true)
                    if (!isQuestComplete(player, ClockTower.questName)) {
                        setQuestStage(player, ClockTower.questName, getQuestStage(player, ClockTower.questName) + 1)
                    }
                }
            } else {
                sendMessage(player, "The cog has already been placed.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BLUE_COG_22, Scenery.CLOCK_SPINDLE_32) { player, _, _ ->
            if (!getAttribute(player, ClockTower.attributeBlueCog, false)) {
                if (removeItem(player, Items.BLUE_COG_22)) {
                    sendMessage(player, "The cog fits perfectly.")
                    setAttribute(player, ClockTower.attributeBlueCog, true)
                    if (!isQuestComplete(player, ClockTower.questName)) {
                        setQuestStage(player, ClockTower.questName, getQuestStage(player, ClockTower.questName) + 1)
                    }
                }
            } else {
                sendMessage(player, "The cog has already been placed.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.RED_COG_23, Scenery.CLOCK_SPINDLE_29) { player, _, _ ->
            if (!getAttribute(player, ClockTower.attributeRedCog, false)) {
                if (removeItem(player, Items.RED_COG_23)) {
                    sendMessage(player, "The cog fits perfectly.")
                    setAttribute(player, ClockTower.attributeRedCog, true)
                    if (!isQuestComplete(player, ClockTower.questName)) {
                        setQuestStage(player, ClockTower.questName, getQuestStage(player, ClockTower.questName) + 1)
                    }
                }
            } else {
                sendMessage(player, "The cog has already been placed.")
            }
            return@onUseWith true
        }

        // The lever to the rats
        val levers = intArrayOf(Scenery.LEVER_33, Scenery.LEVER_34)
        on(levers, IntType.SCENERY, "pull") { player, node ->
            val leverScenery = node.asScenery()
            // This one opens the first and only gate
            if (leverScenery.location.equals(2591, 9661, 0)) {
                if (leverScenery.id == Scenery.LEVER_33) {
                    val metalGate = getScenery(2595, 9657, 0)!!
                    val location = metalGate.location.transform(1, 0, 0)
                    face(player, Location.create(2591, 9660, 0))
                    animate(player, UP_ANIMATION)
                    replaceScenery(metalGate, Scenery.GATE_37, -1, Direction.WEST, location)
                    replaceScenery(leverScenery, Scenery.LEVER_34, -1)
                } else if (leverScenery.id == Scenery.LEVER_34) {
                    val metalGateOpen = getScenery(2596, 9657, 0)!!
                    val location = metalGateOpen.location.transform(-1, 0, 0)
                    face(player, Location.create(2591, 9660, 0))
                    animate(player, DOWN_ANIMATION)
                    replaceScenery(metalGateOpen, Scenery.GATE_37, -1, Direction.SOUTH, location)
                    replaceScenery(leverScenery, Scenery.LEVER_33, -1)
                }
            }
            if (leverScenery.location.equals(2593, 9661, 0)) {
                // This one doesn't do anything https://www.youtube.com/watch?v=2FXiYftAX9A
                // The second gate is fixed and is not a Scenery
                if (leverScenery.id == Scenery.LEVER_34) {
                    face(player, Location.create(2593, 9660, 0))
                    animate(player, DOWN_ANIMATION)
                    replaceScenery(leverScenery, Scenery.LEVER_33, -1)
                } else if (leverScenery.id == Scenery.LEVER_33) {
                    face(player, Location.create(2593, 9660, 0))
                    animate(player, UP_ANIMATION)
                    replaceScenery(leverScenery, Scenery.LEVER_34, -1)
                }
            }
            return@on true
        }

        // The gate to the rats
        on(Scenery.GATE_37, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't seem to open from here...")
            return@on true
        }

        // Poison the rats
        onUseWith(IntType.SCENERY, Items.RAT_POISON_24, Scenery.FOOD_TROUGH_40) { player, _, _ ->
            if (getAttribute(player, ClockTower.attributeRatsPoisoned, false)) {
                sendDialogue(player, "The gate has already been damaged, you see no reason to cause more suffering.")
                return@onUseWith true
            }
            if (removeItem(player, Items.RAT_POISON_24)) {
                val loc = location(2579, 9656, 0) // Location of the gate
                sendMessage(player, "The rats swarm towards the poisoned food...")
                sendMessage(player, "... and devour it hungrily.")
                sendMessage(player, "You see them smashing against the gates in a panic.")
                sendMessage(player, "They seem to be dying.")
                setAttribute(player, ClockTower.attributeRatsPoisoned, true)
                runTask(player, 1) {
                    val rats = findLocalNPCs(player, intArrayOf(NPCs.DUNGEON_RAT_224))
                    rats.forEach { rat ->
                        if (rat.location.withinDistance(loc, 20)) {
                            var xDeath = 0
                            var yDeath = -1
                            for (rat in rats) {

                                Pathfinder.find(rat.location, location(loc.x + xDeath, loc.y + yDeath, loc.z), true, Pathfinder.SMART).walk(rat)
                                xDeath++
                                if (xDeath == 4) {
                                    xDeath = 0
                                    yDeath++
                                }
                            }
                            runTask(rat, 15) {
                                rat.startDeath(null)
                            }
                        }
                    }
                }
            }
            return@onUseWith true
        }

        // Gate to White Cog (after rats die)
        on(Scenery.GATE_39, IntType.SCENERY, "go-through") { player, node ->
            if (getAttribute(player, ClockTower.attributeRatsPoisoned, false)) {
                sendDialogueLines(player, "The death throws of the rats seem to have shaken the door loose of", "its hinges. You pick it up and go through.")
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendPlayerDialogue(player, "It won't open. Maybe the monk knows how to open this door?")
                setAttribute(player, ClockTower.attributeAskKojoAboutRats, true)
            }
            return@on true
        }

        // Wall to Blue Cog
        on(Scenery.WALL_1586, IntType.SCENERY, "push") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        on(Items.WHITE_COG_20, IntType.GROUNDITEM, "take") { player, groundItem ->
            if (inInventory(player, Items.WHITE_COG_20) || inInventory(player, Items.BLACK_COG_21) || inInventory(player, Items.BLUE_COG_22) || inInventory(player, Items.RED_COG_23)) {
                sendDialogue(player, "The cogs are too heavy to carry more than one at a time.")
            } else if (hasSpaceFor(player, Item(Items.WHITE_COG_20))) {
                addItem(player, Items.WHITE_COG_20)
                removeGroundItem(groundItem as GroundItem)
            }
            return@on true
        }

        onUseWith(IntType.GROUNDITEM, Items.BUCKET_OF_WATER_1929, Items.BLACK_COG_21) { player, _, _ ->
            if (removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                addItem(player, Items.BUCKET_1925)
                sendDialogue(player, "You pour water over the cog. It quickly cools down enough to take.")
                setAttribute(player, ClockTower.attributeBlackCogCooled, true)
            }
            return@onUseWith true
        }

        on(Items.BLACK_COG_21, IntType.GROUNDITEM, "take") { player, groundItem ->
            if (inInventory(player, Items.WHITE_COG_20) || inInventory(player, Items.BLACK_COG_21) || inInventory(player, Items.BLUE_COG_22) || inInventory(player, Items.RED_COG_23)) {
                sendDialogue(player, "The cogs are too heavy to carry more than one at a time.")
                return@on true
            }
            if (hasSpaceFor(player, Item(Items.BLACK_COG_21)) && getAttribute(player, ClockTower.attributeBlackCogCooled, false)) {
                addItem(player, Items.BLACK_COG_21)
                removeGroundItem(groundItem as GroundItem)
                return@on true
            }
            if (!inInventory(player, Items.BUCKET_OF_WATER_1929, 1) && !inEquipment(player, Items.ICE_GLOVES_1580)) {
                sendDialogue(player, "The cog is red hot from the flames. You cannot pick it up.")
                return@on true
            }
            if (hasSpaceFor(player, Item(Items.BLACK_COG_21)) && (inInventory(player, Items.BUCKET_OF_WATER_1929, 1) || inEquipment(player, Items.ICE_GLOVES_1580)) && !getAttribute(player, ClockTower.attributeBlackCogCooled, false)) {
                // Picking up the Black Cog requires dousing it in water or using ice gloves
                if (!inEquipment(player, Items.ICE_GLOVES_1580) && removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                    sendDialogue(player, "You pour water over the cog. It quickly cools down enough to take.")
                    addItem(player, Items.BUCKET_1925)
                    addItem(player, Items.BLACK_COG_21)
                    setAttribute(player, ClockTower.attributeBlackCogCooled, true)
                    removeGroundItem(groundItem as GroundItem)
                } else if (inEquipment(player, Items.ICE_GLOVES_1580) && !inInventory(player, Items.BUCKET_OF_WATER_1929)) {
                    sendDialogue(player, "You grab the cog with your ice gloves. It quickly cools down enough to take.")
                    setAttribute(player, ClockTower.attributeBlackCogCooled, true)
                    removeGroundItem(groundItem as GroundItem)
                    addItem(player, Items.BLACK_COG_21)
                }
            }
            return@on true
        }

        on(Items.BLUE_COG_22, IntType.GROUNDITEM, "take") { player, groundItem ->
            if (inInventory(player, Items.WHITE_COG_20) || inInventory(player, Items.BLACK_COG_21) || inInventory(player, Items.BLUE_COG_22) || inInventory(player, Items.RED_COG_23)) {
                sendDialogue(player, "The cogs are too heavy to carry more than one at a time.")
            } else if (hasSpaceFor(player, Item(Items.BLUE_COG_22))) {
                addItem(player, Items.BLUE_COG_22)
                removeGroundItem(groundItem as GroundItem)
            }
            return@on true
        }

        on(Items.RED_COG_23, IntType.GROUNDITEM, "take") { player, groundItem ->
            if (inInventory(player, Items.WHITE_COG_20) || inInventory(player, Items.BLACK_COG_21) || inInventory(player, Items.BLUE_COG_22) || inInventory(player, Items.RED_COG_23)) {
                sendDialogue(player, "The cogs are too heavy to carry more than one at a time.")
            } else if (hasSpaceFor(player, Item(Items.RED_COG_23))) {
                addItem(player, Items.RED_COG_23)
                removeGroundItem(groundItem as GroundItem)
            }
            return@on true
        }

    }
}
