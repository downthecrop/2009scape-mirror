package content.region.kandarin.quest.waterfall

import content.data.Quests
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueLabeller
import core.game.global.action.DoorActionHandler
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.ticksToCycles
import org.rs09.consts.Animations
import org.rs09.consts.Graphics as GFX
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery as SceneryObj

/**
 * Listeners for the Waterfall Quest.
 * In Loving Memory of WaterfallPlugin.java 2015/02/28 - 2026/07/27, about which was said:
 * "This is one of the most disgusting files I have ever seen." - Ceikry
 * @author Bishop
 */

class WaterfallListeners : InteractionListener {

    companion object {
        // Attributes
        private const val ATTRIBUTE_RUNES   = "waterfall:runes-placed"
        // Locations
        private val locIslandAfterRaft      = Location(2512, 3481)
        private val locIslandBeforeRock     = Location(2512, 3476)
        private val locSwimmingGoal         = Location(2512, 3471)
        private val locInFrontOfRock        = Location(2512, 3470)
        private val locRock                 = Location(2512, 3468)
        private val locWaterfallLedge       = Location(2511, 3463)
        private val locDownstream           = Location(2527, 3413)
        private val locTopOfStairs          = Location(2518, 3431, 1)
        private val locInsideTomb           = Location(2555, 9844)
        private val locInWaterfallCave      = Location(2575, 9861)
        private val locWaterfallDoorSouth   = Location(2568, 9893)
        private val locWaterfallDoorNorth   = Location(2566, 9901)
        private val locWaterfallDoorChamber = Location(2604, 9900)
        private val locBeforeFloodedStatue  = Location(2539, 9914)
        private val locBeforeFinalStatue    = Location(2603, 9914)
        // Misc data
        private val ropeYSpan               = intArrayOf(3469, 3470, 3471, 3472, 3473, 3474, 3475)
        private val pedestalLocations       = arrayOf(
            Location(2562, 9914), Location(2562, 9912), Location(2562, 9910),
            Location(2569, 9914), Location(2569, 9912), Location(2569, 9910),
        )
        private val puzzleRunes             = intArrayOf(
            Items.WATER_RUNE_555, Items.AIR_RUNE_556, Items.EARTH_RUNE_557,
        )
        private val anyRunes               = intArrayOf(
            Items.FIRE_RUNE_554,   Items.WATER_RUNE_555, Items.AIR_RUNE_556,    Items.EARTH_RUNE_557,   Items.MIND_RUNE_558,
            Items.BODY_RUNE_559,   Items.DEATH_RUNE_560, Items.NATURE_RUNE_561, Items.CHAOS_RUNE_562,   Items.LAW_RUNE_563,
            Items.COSMIC_RUNE_564, Items.BLOOD_RUNE_565, Items.SOUL_RUNE_566,   Items.ASTRAL_RUNE_9075, Items.STEAM_RUNE_4694,
            Items.MIST_RUNE_4695,  Items.DUST_RUNE_4696, Items.SMOKE_RUNE_4697, Items.MUD_RUNE_4698,    Items.LAVA_RUNE_4699
        )
    }

    override fun defineListeners() {
        // Speak to Hudon
        on(NPCs.HUDON_305, IntType.NPC, "talk-to") { player, node ->
            sendMessage(player, "Hudon is refusing to leave the waterfall")
            DialogueLabeller.open(player, HudonDialogueFile(), node as NPC)
            return@on true
        }

        // Ride the raft
        on(SceneryObj.LOG_RAFT_1987, IntType.SCENERY, "board") { player, _ ->
            if (getQuestStage(player, Quests.WATERFALL_QUEST) >= 10) {
                lock(player, 8)
                queueScript(player, 2, QueueStrength.STRONG) { stage ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "You board the small raft")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            sendMessage(player, "and push off down stream.")
                            return@queueScript delayScript(player, 2)
                        }
                        2 -> {
                            sendMessage(player, "The raft is pulled down stream by strong currents.")
                            return@queueScript delayScript(player, 2)
                        }
                        3 -> {
                            sendMessage(player, "You crash into a small land mound")
                            teleport(player, locIslandAfterRaft)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendDialogue(player, "You have no reason to board this raft.")
            }
            return@on true
        }

        // Try to swim to the rock at the waterfall, or just swim in general
        on(intArrayOf(SceneryObj.ROCK_1996, SceneryObj.ROCK_1997, SceneryObj.RIVER_10283), IntType.SCENERY, "swim to", "swim") { player, _ ->
            sendMessage(player, "It looks like a long distance, but you swim out into the water.")
            sendGraphics(GFX.WATER_SPLASH_68, player.location)
            AgilityHandler.walk(
                player, -1, player.location, locSwimmingGoal, Animation(Animations.HUMAN_SWIM_164), 0.0, null
            )
            lock(player, 6)
            queueScript(player, 3, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "The current is too strong, you feel yourself being pulled under")
                        return@queueScript delayScript(player, 3)
                    }
                    1 -> {
                        sendMessage(player, "You are washed downstream but feel lucky to be alive.")
                        teleport(player, locDownstream)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Correctly use a rope on the rock at the waterfall
        onUseWith(IntType.SCENERY, Items.ROPE_954, SceneryObj.ROCK_1996, SceneryObj.ROCK_1997) { player, _, _ ->
            animate(player, Animations.HUMAN_TOSS_ROPE_774)
            sendGraphics(GFX.ROPE_EXTENDS_67, Location(locRock.x, ropeYSpan[3]))
            lock(player, 9)
            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        replaceScenery(getScenery(locRock)!!, SceneryObj.ROCK_1997, -1)
                        for (y in ropeYSpan[0]..ropeYSpan[6]) {
                            if (getScenery(Location(locRock.x, y))?.id != SceneryObj.ROPE_1998) {
                                addScenery(Scenery(SceneryObj.ROPE_1998, Location(locRock.x, y), 10, 0))
                            }
                        }
                        face(player, locRock)
                        renderAnimation(player, Animations.HUMAN_WADE_WITH_ROPE_R_273)
                        AgilityHandler.walk(
                            player, -1, player.location, locInFrontOfRock, null, 0.0, null
                        )
                        return@queueScript keepRunning(player)
                    }
                    in 1..6 -> {
                        removeScenery(Scenery(SceneryObj.ROPE_1998, Location(locRock.x, ropeYSpan[7 - stage]), 10, 0))
                        if (stage == 1 && getScenery(locRock.x, ropeYSpan[6], 0) == null) {
                            addScenery(SceneryObj.RIVER_10283, Location(locRock.x, ropeYSpan[6]))
                        }
                        return@queueScript keepRunning(player)
                    }
                    7 -> {
                        AgilityHandler.walk(
                            player, -1, player.location, locInFrontOfRock.transform(1, -1, 0), null, 0.0, null
                        )
                        replaceScenery(getScenery(locRock)!!, SceneryObj.ROCK_1996, -1)
                        removeScenery(Scenery(SceneryObj.ROPE_1998, Location(locRock.x, ropeYSpan[0]), 10, 0))
                        return@queueScript keepRunning(player)
                    }
                    8 -> {
                        resetRenderAnimation(player)
                        forceMove(player, player.location, locInFrontOfRock.transform(1, -2, 0), 0, ticksToCycles(1))
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        // Try to climb down the tree at the waterfall barehanded
        on(SceneryObj.DEAD_TREE_2020, IntType.SCENERY, "climb") { player, _ ->
            sendMessage(player, "You slip and tumble over the water "/*sic*/+"fall.")
            sendChat(player, "Ouch!")
            teleport(player, locDownstream)
            face(player, locDownstream.transform(0, 1, 0))
            impact(player, 8, HitsplatType.NORMAL)
            return@on true
        }

        // Correctly use a rope on the tree at the waterfall
        onUseWith(IntType.SCENERY, Items.ROPE_954, SceneryObj.DEAD_TREE_2020) { player, _, _ ->
            sendMessage(player, "You tie the rope to the tree")
            queueScript(player, 1, QueueStrength.STRONG) {
                sendMessage(player, "and let your self down on to the ledge.")
                teleport(player, locWaterfallLedge)
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

        // Get in the barrel on the waterfall's ledge
        on(SceneryObj.BARREL_2022, IntType.SCENERY, "get in") { player, _ ->
            sendMessage(player, "You get in the barrel and start rocking.")
            queueScript(player, 1, QueueStrength.STRONG) {
                sendMessage(player, "The barrel falls off the ledge.")
                teleport(player, locDownstream)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // Climb the stairs at the tourism center
        on(SceneryObj.STAIRCASE_1738, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, locTopOfStairs)
            return@on true
        }

        // Search an incorrect bookcase at the tourism center
        on(intArrayOf(SceneryObj.BOOKCASE_380, SceneryObj.BOOKCASE_381), IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the books...")
            queueScript(player, 2, QueueStrength.NORMAL) {
                sendMessage(player, "You find nothing of interest to you.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // Search the correct bookcase at the tourism center
        on(SceneryObj.BOOKCASE_1989, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.BOOK_ON_BAXTORIAN_292) || getQuestStage(player, Quests.WATERFALL_QUEST) != 20) {
                sendMessage(player, "You search the bookcase and find nothing of interest.")
            } else {
                sendMessage(player, "You search the bookcase...")
                sendMessage(player, "You find a book named 'Book on Baxtorian'")
                addItemOrDrop(player, Items.BOOK_ON_BAXTORIAN_292)
            }
            return@on true
        }

        // Search the crate that contains the key
        on(SceneryObj.CRATE_1990, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.A_KEY_293) || getQuestStage(player, Quests.WATERFALL_QUEST) < 30) {
                sendMessage(player, "You search the crate and find nothing.")
            } else {
                sendMessage(player, "You search the crate...")
                queueScript(player, 2, QueueStrength.WEAK) { // Weak so you can most easily be harangued by enemies
                    sendMessage(player, "and find a large key.")
                    addItemOrDrop(player, Items.A_KEY_293)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Access the door in the gnome cave
        on(SceneryObj.DOOR_1991, IntType.SCENERY, "open") { player, node ->
            if (player.location.y > node.location.y) {
                sendMessage(player, "You open the gate and walk through.")
                DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                return@on true
            } else {
                sendMessage(player, "The gate is locked.")
            }
            val startLabel = if (getQuestStage(player, Quests.WATERFALL_QUEST) >= 30) { "door_on_quest" } else { "door_no_business_here" }
            DialogueLabeller.open(player, GolrieDialogueFile(startLabel), findLocalNPC(player, NPCs.GOLRIE_306)?: return@on true)
            return@on true
        }

        // Use the key on the door in the gnome cave
        onUseWith(IntType.SCENERY, Items.A_KEY_293, SceneryObj.DOOR_1991) { player, _, with ->
            sendMessage(player, "The key fits the gate.")
            lock(player, 3)
            queueScript(player, 3, QueueStrength.STRONG) {
                sendMessage(player, "You open the gate and walk through.")
                DoorActionHandler.handleAutowalkDoor(player, with as Scenery)
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

        // Read Glarial's tombstone
        on(SceneryObj.GLARIAL_S_TOMBSTONE_1992, IntType.SCENERY, "read") { player, _ ->
            lock(player, 16)
            queueScript(player, 1, QueueStrength.NORMAL) { stage ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "The grave is covered in elven script.")
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        sendMessage(player, "Some of the writing is in common tongue, it reads:")
                        return@queueScript delayScript(player, 3)
                    }
                    2 -> {
                        sendMessage(player, "Here lies Glarial, wife of Baxtorian,")
                        return@queueScript delayScript(player, 3)
                    }
                    3 -> {
                        sendMessage(player, "true friend of nature in life and death.")
                        return@queueScript delayScript(player, 3)
                    }
                    4 -> {
                        sendMessage(player, "May she now rest knowing")
                        return@queueScript delayScript(player, 3)
                    }
                    5 -> {
                        sendMessage(player, "only visitors with peaceful intent can enter.")
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Enter Glarial's tomb with the pebble
        onUseWith(IntType.SCENERY, Items.GLARIALS_PEBBLE_294, SceneryObj.GLARIAL_S_TOMBSTONE_1992) { player, _, _ ->
            // A more strict frisk than Entrana
            if (!ItemDefinition.canEnterEntrana(player) || anyInInventory(player, *anyRunes)) {
                sendMessage(player, "You place the pebble in the gravestone's small indent.")
                sendMessage(player, "It fits perfectly.")
                lock(player, 3)
                queueScript(player, 3, QueueStrength.NORMAL) {
                    sendMessage(player, "But nothing happens.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You place the pebble in the gravestone's small indent.")
                sendMessage(player, "It fits perfectly.")
                lock(player, 10)
                queueScript(player, 3, QueueStrength.NORMAL) { stage ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "You hear a loud creak.")
                            return@queueScript delayScript(player, 3)
                        }
                        1 -> {
                            sendMessage(player, "The stone slab slides back revealing a ladder down.")
                            return@queueScript delayScript(player, 4)
                        }
                        2 -> {
                            sendMessage(player, "You climb down to an underground passage.")
                            player.skills.setPrayerPoints(0.0)
                            teleport(player, locInsideTomb)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }

        // Open the treasure chest in Glarial's tomb
        on(SceneryObj.CLOSED_CHEST_33046, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            replaceScenery(node as Scenery, SceneryObj.OPEN_CHEST_33047, -1)
            return@on true
        }

        // Search the treasure chest in Glarial's tomb
        on(SceneryObj.OPEN_CHEST_33047, IntType.SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.WATERFALL_QUEST) >= 30) {
                if (inEquipmentOrInventory(player, Items.GLARIALS_AMULET_295)) {
                    sendMessage(player, "You search the chest and find nothing.")
                } else {
                    sendMessage(player, "You search the chest and find a small amulet.")
                    addItemOrDrop(player, Items.GLARIALS_AMULET_295)
                }
            }
            return@on true
        }

        // Close the treasure chest in Glarial's tomb
        on(SceneryObj.OPEN_CHEST_33047, IntType.SCENERY, "close") { player, node ->
            animate(player, Animations.HUMAN_CLOSE_CHEST_535)
            replaceScenery(node as Scenery, SceneryObj.CLOSED_CHEST_33046, -1)
            return@on true
        }

        // Search Glarial's Tomb
        on(SceneryObj.GLARIAL_S_TOMB_33066, IntType.SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.WATERFALL_QUEST) >= 30) {
                if (inInventory(player, Items.GLARIALS_URN_296)) {
                    sendMessage(player, "You search the coffin and find nothing.")
                } else {
                    sendMessage(player, "You search the coffin")
                    queueScript(player, 2, QueueStrength.WEAK) { stage -> // Weak so you can most easily be harangued by enemies
                        when (stage) {
                            0 -> {
                                sendMessage(player, "Inside you find an urn full of ashes.")
                                return@queueScript delayScript(player, 2)
                            }
                            1 -> {
                                sendMessage(player, "You take the urn and close the coffin.")
                                addItemOrDrop(player, Items.GLARIALS_URN_296)
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            }
            return@on true
        }

        // Enter the waterfall cave
        on(SceneryObj.LEDGE_37247, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door begins to open.")
            queueScript(player, 2, QueueStrength.STRONG) { stage ->
                when (stage) {
                    0 -> {
                        if (inEquipmentOrInventory(player, Items.GLARIALS_AMULET_295) || isQuestComplete(player, Quests.WATERFALL_QUEST)) {
                            return@queueScript delayScript(player, 2)
                        } else {
                            sendMessage(player, "The cave floods and washes you away!")
                            teleport(player, locDownstream)
                            return@queueScript stopExecuting(player)

                        }
                    }
                    1 -> {
                        sendMessage(player, "You walk through the door.")
                        teleport(player, locInWaterfallCave)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Exit the waterfall cave through the entrance
        on(SceneryObj.DOOR_32711, IntType.SCENERY, "open") { player, _ ->
            teleport(player, locWaterfallLedge)
            return@on true
        }

        // Search the crate in the waterfall cave
        on(SceneryObj.CRATE_1999, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.A_KEY_298, 1) || getQuestStage(player, Quests.WATERFALL_QUEST) < 30) {
                sendMessage(player, "You search the crate and find nothing.")
            } else {
                sendMessage(player, "You search the crate")
                queueScript(player, 2, QueueStrength.WEAK) { // Weak so you can most easily be harangued by enemies
                    sendMessage(player, "and find a large key")
                    addItemOrDrop(player, Items.A_KEY_298)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Interact with the doors directly ahead of the waterfall cave's inner chamber
        on(SceneryObj.DOOR_2002, IntType.SCENERY, "open") { player, node ->
            when (node.location) {
                locWaterfallDoorSouth, locWaterfallDoorNorth -> {
                    if (player.location != locWaterfallDoorNorth.transform(0, 1, 0)) {
                        sendMessage(player, "The door is locked.")
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }
                }
                locWaterfallDoorChamber -> {
                    teleport(player, locWaterfallDoorNorth)
                    removeAttribute(player, ATTRIBUTE_RUNES)
                }
                else -> return@on false
            }
            return@on true
        }

        // Use the key on the doors inside the waterfall cave
        onUseWith(IntType.SCENERY, Items.A_KEY_298, SceneryObj.DOOR_2002) { player, _, with ->
            val locInChamber = locWaterfallDoorChamber.transform(0, 1, 0)
            sendMessage(player, "You open the door and walk through.")
            if (player.location == locInChamber) {
                teleport(player, locWaterfallDoorNorth)
                removeAttribute(player, ATTRIBUTE_RUNES)
                return@onUseWith true
            }
            if (isQuestComplete(player, Quests.WATERFALL_QUEST) && player.location == locWaterfallDoorNorth) {
                teleport(player, locInChamber)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, with as Scenery)
            }
            return@onUseWith true
        }

        // Use runes on the pedestals
        onUseWith(IntType.SCENERY, puzzleRunes, SceneryObj.PILLAR_2004) { player, used, with ->
            val pIndex = pedestalLocations.indexOf(with.location)
            val rIndex = puzzleRunes.indexOf(used.id)
            val activeIndex = (pIndex * puzzleRunes.size) + rIndex
            var activeState = getAttribute(player, ATTRIBUTE_RUNES, 0)
            if (pIndex == -1) return@onUseWith false
            if (activeState and (1 shl activeIndex) == 0) {
                if (removeItem(player, used.id)) {
                    sendMessage(player, "You place the rune on the stand.")
                    lock(player, 1)
                    queueScript(player, 1, QueueStrength.SOFT) {
                        sendMessage(player, "The rune stone disappears in a puff of smoke.")
                        sendGraphics(Graphics(GFX.GIVE_THANKS_86, 100), with.location)
                        return@queueScript stopExecuting(player)
                    }
                    activeState = activeState or (1 shl activeIndex)
                    setAttribute(player, ATTRIBUTE_RUNES, activeState)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // Use the amulet on the statue
        onUseWith(IntType.SCENERY, Items.GLARIALS_AMULET_295, SceneryObj.STATUE_OF_GLARIAL_2006) { player, used, _ ->
            if (isQuestComplete(player, Quests.WATERFALL_QUEST) || player.location == locBeforeFinalStatue) {
                return@onUseWith false
            }
            if (getAttribute(player, ATTRIBUTE_RUNES, 0) != (-1 shl 18).inv()) {
                sendMessage(player, "You place the amulet around the neck of the statue.")
                lock(player, 4)
                queueScript(player, 3, QueueStrength.SOFT) {
                    sendMessage(player, "Rocks fall from the ceiling and hit you in the head.")
                    sendGraphics(GFX.ROCKS_FALL_74, player.location)
                    impact(player, 20, HitsplatType.NORMAL)
                    return@queueScript stopExecuting(player)
                }
                return@onUseWith true
            } else if (removeItem(player, used)) {
                sendMessage(player, "You place the amulet around the neck of the statue.")
                lock(player, 5)
                queueScript(player, 2, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "You hear a loud rumble from beneath...")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            sendMessage(player, "The ground raises up before you!")
                            teleport(player, locBeforeFinalStatue)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
                return@onUseWith true
            }
            return@onUseWith false
        }

        // Try to loot the chalice
        on(SceneryObj.CHALICE_OF_ETERNITY_2014, IntType.SCENERY, "take treasure") { player, _ ->
            if (!isQuestComplete(player, Quests.WATERFALL_QUEST)) {
                sendMessage(player, "You hear the gushing of water.")
                removeAttribute(player, ATTRIBUTE_RUNES)
                lock(player, 18)
                queueScript(player, 2, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "Water floods into the cavern.")
                            animate(player, Animations.HUMAN_DROWN_765)
                            teleport(player, locBeforeFloodedStatue)
                            return@queueScript keepRunning(player)
                        }
                        1 -> {
                            renderAnimation(player, Animations.HUMAN_DROWN_R_163)
                            AgilityHandler.walk(player, -1, player.location, player.location.transform(0, -2, 0), null, 0.0, null)
                            return@queueScript delayScript(player, 2)
                        }
                        2 -> {
                            AgilityHandler.walk(player, -1, player.location, player.location.transform(1, -1, 0), null, 0.0, null)
                            return@queueScript keepRunning(player)
                        }
                        3 -> {
                            renderAnimation(player, Animations.HUMAN_WHIRLPOOL_R_847)
                            return@queueScript delayScript(player, 12)
                        }
                        4 -> {
                            sendMessage(player, "You are washed downstream, but feel lucky to be alive.")
                            resetRenderAnimation(player)
                            teleport(player, locDownstream)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendMessage(player, "The chalice only contains some old ashes.")
            }
            return@on true
        }

        // Use the urn on the chalice
        onUseWith(IntType.SCENERY, Items.GLARIALS_URN_296, SceneryObj.CHALICE_OF_ETERNITY_2014) { player, used, _ ->
            if (isQuestComplete(player, Quests.WATERFALL_QUEST)) {
                sendMessage(player, "The chalice only contains some old ashes.")
                return@onUseWith true
            }
            if (freeSlots(player) < 5) {
                sendMessage(player, "You do not have enough space for the reward, you will need 5 free slots to accept it.")
                return@onUseWith true
            }
            if (removeItem(player, used)) {
                addItemOrDrop(player, Items.GLARIALS_URN_297)
                sendMessage(player, "You carefully pour the ashes into the chalice")
                lock(player, 10)
                queueScript(player, 3, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "as you remove the Treasure of Baxtorian.")
                            return@queueScript delayScript(player, 3)
                        }
                        1 -> {
                            sendMessage(player, "The chalice remains standing.")
                            sendMessage(player, "Inside you find a mithril case")
                            sendMessage(player, "containing 40 seeds,")
                            sendMessage(player, "two diamonds and two gold bars.")
                            return@queueScript delayScript(player, 2)
                        }
                        2 -> {
                            finishQuest(player, Quests.WATERFALL_QUEST)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPCs.HUDON_305) { _, _ ->
            return@setDest locIslandAfterRaft
        }

        setDest(IntType.SCENERY, intArrayOf(SceneryObj.ROCK_1996, SceneryObj.ROCK_1997), "swim to", "use") { _, _ ->
            return@setDest locIslandBeforeRock
        }
    }

}