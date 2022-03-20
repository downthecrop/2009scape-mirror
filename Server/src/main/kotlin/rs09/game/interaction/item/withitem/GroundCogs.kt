package rs09.game.interaction.item.withitem

import api.*
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.content.quest.members.clocktower.ClockTowerObjInterationDialogueFile
import rs09.game.interaction.InteractionListener

class GroundCogs : InteractionListener() {

    override fun defineListeners() {
        on(Items.BLACK_COG_21, GROUNDITEM, "take") { player, groundItem ->
            if (!inInventory(player, Items.BUCKET_OF_WATER_1929, 1) && !inEquipment(player, Items.ICE_GLOVES_1580)){
                sendDialogue(player, "The cog is red hot from the flames. You cannot pick it up.")
            } else if(hasSpaceFor(player, Item(Items.BLACK_COG_21)) && getAttribute(player, "quest:clocktower-blackcogcooled", false)) {
                addItem(player, Items.BLACK_COG_21)
                removeGroundItem(groundItem as GroundItem)
                setAttribute(player, "/save:quest:clocktower-blackcogcooled", false)
                setQuestStage(player, "Clock Tower", 2)
            } else if(hasSpaceFor(player, Item(Items.BLACK_COG_21)) && (inInventory(player, Items.BUCKET_OF_WATER_1929, 1) || inEquipment(player, Items.ICE_GLOVES_1580)) && !getAttribute(player, "quest:clocktower-blackcogcooled", false)) {
                if(!inEquipment(player, Items.ICE_GLOVES_1580) && removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                    sendDialogue(player!!, "You pour water over the cog. It quickly cools down enough to take.")
                    addItem(player!!, Items.BUCKET_1925)
                    addItem(player!!, Items.BLACK_COG_21)
                    setQuestStage(player!!, "Clock Tower", 2)
                    setAttribute(player, "/save:quest:clocktower-blackcogcooled", true)
                    removeGroundItem(groundItem as GroundItem)
                } else if(inEquipment(player, Items.ICE_GLOVES_1580) && !inInventory(player, Items.BUCKET_OF_WATER_1929)) {
                    sendDialogue(player, "You grab the cog with your ice gloves. It quickly cools down enough to take.")
                    setAttribute(player, "/save:quest:clocktower-blackcogcooled", true)
                    removeGroundItem(groundItem as GroundItem)
                    addItem(player, Items.BLACK_COG_21)
                    setQuestStage(player, "Clock Tower", 2)
                }
            }
            return@on true
        }

        onUseWith(SCENERY, Items.BLACK_COG_21, Scenery.CLOCK_SPINDLE_30) { player, _, _ ->
            sendMessage(player, "The cog fits perfectly.")
            removeItem(player, Items.BLACK_COG_21)
            setAttribute(player,"/save:quest:clocktower-blackcogplaced", true)
            player.incrementAttribute("/save:quest:clocktower-cogsplaced")
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.BLUE_COG_22, Scenery.CLOCK_SPINDLE_32) { player, _, _ ->
            sendMessage(player, "The cog fits perfectly.")
            removeItem(player, Items.BLUE_COG_22)
            setAttribute(player,"/save:quest:clocktower-bluecogplaced", true)
            player.incrementAttribute("/save:quest:clocktower-cogsplaced")
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.WHITE_COG_20, Scenery.CLOCK_SPINDLE_31) { player, _, _ ->
            sendMessage(player, "The cog fits perfectly.")
            removeItem(player, Items.WHITE_COG_20)
            setAttribute(player,"/save:quest:clocktower-whitecogplaced", true)
            player.incrementAttribute("/save:quest:clocktower-cogsplaced")
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.RED_COG_23, Scenery.CLOCK_SPINDLE_29) { player, _, _ ->
            sendMessage(player, "The cog fits perfectly.")
            removeItem(player, Items.RED_COG_23)
            setAttribute(player,"/save:quest:clocktower-redcogplaced", true)
            player.incrementAttribute("/save:quest:clocktower-cogsplaced")
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.RAT_POISON_24, Scenery.FOOD_TROUGH_40) { player, _, _ ->
            val loc = location(2579, 9656, 0)

            sendMessage(player, "The rats swarm towards the poisoned food...")
            sendMessage(player, "... and devour it hungrily.")
            sendMessage(player, "You see them smashing against the gates in a panic.")
            sendMessage(player, "They seem to be dying.")
            removeItem(player, Items.RAT_POISON_24)
            setAttribute(player,"/save:quest:clocktower-poisonplaced", true)
            runTask(player, 1) {
                val rattos = findLocalNPCs(player, intArrayOf(NPCs.DUNGEON_RAT_224))
                rattos.forEach{rat ->
                    if(rat.location.withinDistance(loc,20)) {
                        var a = 0
                        var b = 0
                        var c = false

                        for(rat in rattos) {
                            Pathfinder.find(rat.location, location(loc.x + a, loc.y + b, loc.z), true, Pathfinder.SMART).walk(rat)
                            a++
                            if (a == 4) {
                                c = true
                                a = 0
                            }
                            if (c) {
                                b = 1
                            }
                        }
                        runTask(rat, 15) {
                            rat.startDeath(player)
                        }
                    }
                }
            }
            return@onUseWith true
        }

        val levers = intArrayOf(Scenery.LEVER_33, Scenery.LEVER_34)
        on(levers, SCENERY, "pull") { player, node ->
            if (node.asScenery().location.equals(2591, 9661, 0)) {
                setAttribute(player, "clocktower:raiseLowerLever", true)
                //also opens door id 37, location: 2595, 9657, 0
            }

            if (node.asScenery().location.equals(2593, 9661, 0)) {
                setAttribute(player, "clocktower:lowerUpperLever", true)
            }
            return@on true
        }

        on(Scenery.GATE_39, SCENERY, "go-through") { player, node ->
            if (node.asScenery().location.equals(2579, 9656, 0) && player.location.equals(2579, 9656, 0)) {
                openDialogue(player, ClockTowerObjInterationDialogueFile(0))
            }
            return@on true
        }


    }
}