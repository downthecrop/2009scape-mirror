package content.region.fremennik.lighthouse.quest.horror.handlers

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import content.region.fremennik.lighthouse.quest.horror.handlers.bookcase.BookcaseDialogue
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.repository.Repository.getPlayerByName
import core.game.world.update.flag.context.Animation
import org.rs09.consts.*

class HorrorFromTheDeepListeners : InteractionListener {
    private val brokenBridge = intArrayOf(Scenery.BROKEN_BRIDGE_4615, Scenery.BROKEN_BRIDGE_4616)
    private val strangeWalls = intArrayOf(Scenery.STRANGE_WALL_4544, Scenery.STRANGE_WALL_4543)
    private val strangeDoors = intArrayOf(Scenery.STRANGE_WALL_4545, Scenery.STRANGE_WALL_4546)
    private val requiredItems = intArrayOf(
        Items.AIR_RUNE_556,
        Items.FIRE_RUNE_554,
        Items.EARTH_RUNE_557,
        Items.WATER_RUNE_555,

        Items.BRONZE_ARROW_882,
        Items.BRONZE_ARROWP_883,
        Items.BRONZE_ARROWP_PLUS_5616,
        Items.BRONZE_ARROWP_PLUS_PLUS_5622,
        Items.IRON_ARROW_884,
        Items.IRON_ARROWP_885,
        Items.IRON_ARROWP_PLUS_5617,
        Items.IRON_ARROWP_PLUS_PLUS_5623,
        Items.STEEL_ARROW_886,
        Items.STEEL_ARROWP_887,
        Items.STEEL_ARROWP_PLUS_5618,
        Items.STEEL_ARROWP_PLUS_PLUS_5624,
        Items.MITHRIL_ARROW_888,
        Items.MITHRIL_ARROWP_889,
        Items.MITHRIL_ARROWP_PLUS_5619,
        Items.MITHRIL_ARROWP_PLUS_PLUS_5625,
        Items.ADAMANT_ARROW_890,
        Items.ADAMANT_ARROWP_891,
        Items.ADAMANT_ARROWP_PLUS_5620,
        Items.ADAMANT_ARROWP_PLUS_PLUS_5626,
        Items.RUNE_ARROW_892,
        Items.RUNE_ARROWP_893,
        Items.RUNE_ARROWP_PLUS_5621,
        Items.RUNE_ARROWP_PLUS_PLUS_5627,
        Items.DRAGON_ARROW_11212,
        Items.DRAGON_ARROWP_11227,
        Items.DRAGON_ARROWP_PLUS_11228,
        Items.DRAGON_ARROWP_PLUS_PLUS_11229,

        Items.BRONZE_SWORD_1277,
        Items.IRON_SWORD_1279,
        Items.STEEL_SWORD_1281,
        Items.MITHRIL_SWORD_1285,
        Items.ADAMANT_SWORD_1287,
        Items.RUNE_SWORD_1289
    )

    override fun defineListeners() {

        // bookcase inside the lighthouse
        on(Scenery.BOOKCASE_4617, IntType.SCENERY, "search") { player, _ ->
            if (!isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                // pay no attention to the passed jossik, he is not used. If I cast the bookcase as an NPC and pass it, DialogueLabeller throws a fit
                openDialogue(player, BookcaseDialogue(), NPC(NPCs.JOSSIK_1334))
            } else {
                sendMessage(player, "You search the bookcase but find nothing of interest.")
            }
            return@on true
        }

        // the ladder down to the dagganoth lair. accessible after you've fixed the light, or after the quest
        on(Scenery.IRON_LADDER_4383, IntType.SCENERY, "climb") { player, _ ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val teleportLocation = when {
                // dag dungeon
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> Location(2519, 9994, 1)

                // boss fight area
                questStage >= 4 -> Location(2519, 4618, 1)

                // no access
                else -> null
            }

            if (teleportLocation != null) {
                animate(player, 827)
                queueScript(player, 1, QueueStrength.SOFT) {
                    teleport(player, teleportLocation)
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendPlayerDialogue(player, "I have no reason to go down there.", FacialExpression.HALF_THINKING)
            }
            return@on true
        }

        /*
         * Handles ladders up to lighthouse.
         */

        // the ladder up to the lighthouse
        on(Scenery.IRON_LADDER_4412, IntType.SCENERY, "climb") { player, _ ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val teleportLocation = when {
                // return to non-instanced lighthouse
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> Location(2510, 3644, 0)

                // return to instanced lighthouse (you can only get down here at stage 50 or greater)
                questStage <= 5 -> Location(2446, 4604, 0)

                else -> null
            }

            if (teleportLocation != null) {
                animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
                queueScript(player, 1, QueueStrength.SOFT) {
                    teleport(player, teleportLocation)
                    return@queueScript stopExecuting(player)
                }
            } else {
                // this should never trigger
                sendPlayerDialogue(player, "I have no reason to go up there.", FacialExpression.HALF_THINKING)
            }
            return@on true
        }

        // handles the secret shortcut ladder up from DKS to the Lighthouse
        on(Scenery.LADDER_10194, IntType.SCENERY, "climb-up") { player, _ ->
            // TODO: Swimming cutscene/forcemove from mouth of cave to shore
            // Horror from the Deep must be completed in order to access this
            val teleportLocation = when {

                // climb up to the Lighthouse dungeon
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> Location(2510, 10019, 0)

                else -> null
            }

            if (teleportLocation != null) {
                animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
                queueScript(player, 1, QueueStrength.SOFT) {
                    teleport(player, teleportLocation)
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You need to have completed Horror from the Deep in order to do this.")
            }
            return@on true
        }

        /*
         * Handles the interaction with lighthouse door.
         * During the quest, the door teleports the player to the second lighthouse.
         */

        on(Scenery.DOORWAY_4577, IntType.SCENERY, "walk-through") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

            // fixed the bridge
            val bridge = (getVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_E) == 1
                    && getVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_W) == 1)

            // got the key
            val key = getVarbit(player, HorrorFromTheDeep.HFTD_LIGHTHOUSE_KEY) == 1

            when {
                // quest complete
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                // quest in progress, after bridge fixed and key obtained
                questStage >= 2 -> {
                    val insideLighthouse = inBorders(player, 2508, 3634, 2510, 3635)
                    val teleportLocation = if (insideLighthouse) {
                        location(2445, 4596, 0)
                    } else {
                        location(2509, 3635, 0)
                    }

                    // set varbit that says you entered
                    if (getVarbit(player, HorrorFromTheDeep.HFTD_LIGHTHOUSE_DOOR) == 0) {
                        setVarbit(player, HorrorFromTheDeep.HFTD_LIGHTHOUSE_DOOR, 1, true)
                    }

                    lock(player, 5)
                    queueScript(player, 2) { count ->
                        when(count) {
                            0 -> {
                                sendMessage(player, "You unlock the Lighthouse front door.")
                                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                                return@queueScript delayScript(player, 2)
                            }

                            1 -> {
                                teleport(player, teleportLocation)
                                return@queueScript stopExecuting(player)
                            }
                        }
                        return@queueScript stopExecuting(player)
                    }
                }

                // fix the bridge and get the key first
                questStage == 1 -> {
                    if (bridge && key) {
                        sendDialogue(player, "I should talk to Larrissa first.")
                    } else {
                        sendNPCDialogue(player, NPCs.LARRISSA_1336, "Please adventurer... We are both curious as to what has happened in that lighthouse, but you need to fix the bridge for me!")
                    }
                }

                // quest not started
                else -> {
                    sendDialogue(player, "You can't see any way to open the door.")
                }
            }
            return@on true
        }

        // use swamp tar to refill the lighthouse light
        onUseWith(IntType.SCENERY, Items.SWAMP_TAR_1939, Scenery.LIGHTING_MECHANISM_4588) { player, _, mechanism ->
            if (getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_TAR) == 0
                && removeItem(player, Item(Items.SWAMP_TAR_1939, 1))
            ) {
                sendMessage(player, "You use the swamp tar to make the torch flammable again.")
                setVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_TAR, 1, true)
                if (checkLight(player)) {
                    replaceScenery(mechanism.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 200)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // use a tinderbox to light the filled lighthouse light
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.LIGHTING_MECHANISM_4588) { player, _, mechanism ->
            if (getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LIT) == 0
                && getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_TAR) == 1
            ) {
                sendMessage(player, "You light the torch with your tinderbox.")
                setVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LIT, 1, true)
                if (checkLight(player)) {
                    replaceScenery(mechanism.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 200)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // use molten glass to fix the lighthouse lens
        onUseWith(IntType.SCENERY, Items.MOLTEN_GLASS_1775, Scenery.LIGHTING_MECHANISM_4588) { player, item, mechanism ->
            if (getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LENS) == 0
                && removeItem(player, item.asItem())
            ) {
                setVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LENS, 1, true)
                sendMessage(player, "You use the molten glass to repair the lens.")
                if (checkLight(player)) {
                    replaceScenery(mechanism.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 200)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // study the strange wall
        on(strangeWalls, IntType.SCENERY, "study") { player, _ ->
            when (player.location.y) {
                4626 -> {
                    if (getQuestStage(player, Quests.HORROR_FROM_THE_DEEP) >= 5) {
                        openInterface(player, Components.HORROR_METALDOOR_142)
                    } else {
                        openInterface(player, Components.HORROR_METALDOOR_142)
                        setVarbit(player, HorrorFromTheDeep.HFTD_DOOR, 1, true)
                    }
                }

                10002 -> openInterface(player, Components.HORROR_METALDOOR_142)
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        // use items on the strange wall
        onUseWith(IntType.SCENERY, requiredItems, *strangeWalls) { player, used, _ ->
            openDialogue(player, StrangeWallDialogue(items = used.id))
            return@onUseWith true
        }

        // open the strange wall
        on(strangeDoors, IntType.SCENERY, "open") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            if (questStage >= 5) {
                // doors are one-way: https://www.youtube.com/watch?v=ZdmHrnYjbd4&t=325s
                when (player.location.y) {
                    4626, 10002 -> if (node.id == 4546) {
                        sendMessage(player, "This door cannot be opened from this side.")
                        return@on false
                    }
                    4627, 10003 -> if (node.id == 4545) {
                        sendMessage(player, "This door cannot be opened from this side.")
                        return@on false
                    }
                }
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                playAudio(player, Sounds.STRANGEDOOR_OPEN_1626)
                playAudio(player, Sounds.STRANGEDOOR_CLOSE_1625, 2)

                return@on true
            }
            when (player.location.y) {
                4626, 10002 -> sendMessage(player, "You cannot see any way to move this part of the wall....")
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        // fix the bridge with planks
        onUseWith(IntType.SCENERY, Items.PLANK_960, *brokenBridge) { player, _, bridge ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

            val eastBridge = Scenery.BROKEN_BRIDGE_4616
            val eastBridgeFixed = getVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_E) == 1

            val westBridge = Scenery.BROKEN_BRIDGE_4615
            val westBridgeFixed = getVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_W) == 1

            when(questStage) {
                // quest not started
                0 -> sendDialogue(player, "That won't help fix the bridge.")

                // during quest
                1 -> {
                    // check for hammer
                    if (!inInventory(player, Items.HAMMER_2347, 1)) {
                        sendDialogue(player, "You need a hammer to force the nails in with.")
                        return@onUseWith false
                    }

                    // check for nails
                    if (amountInInventory(player, Items.STEEL_NAILS_1539) < 30) {
                        sendDialogue(player, "You need 30 steel nails to attach the plank with.")
                        return@onUseWith false
                    }

                    // select the bridge side we're on
                    when(bridge.id) {
                        // east
                        eastBridge -> {
                            if (eastBridgeFixed) {
                                sendDialogue(player, "You have already fixed this half of the bridge.")
                            } else {
                                // fix the bridge
                                val message =
                                    if (!westBridgeFixed) "You create half a makeshift walkway out of the plank."
                                    else "You have now made a makeshift walkway over the bridge."

                                if (removeItem(player, Items.PLANK_960) && removeItem(player, Item(Items.STEEL_NAILS_1539, 30))) {
                                    lock(player, 1)
                                    animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)

                                    queueScript(player, 3, QueueStrength.SOFT) {
                                        animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                                        setVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_E, 1, true)
                                        sendDialogue(player, message)
                                        return@queueScript stopExecuting(player)
                                    }
                                }
                            }
                        }

                        // west
                        westBridge -> {
                            if (westBridgeFixed) {
                                sendDialogue(player, "You have already fixed this half of the bridge.")
                            } else {
                                // fix the bridge
                                val message =
                                    if (!eastBridgeFixed) "You create half a makeshift walkway out of the plank."
                                    else "You have now made a makeshift walkway over the bridge."

                                if (removeItem(player, Items.PLANK_960) && removeItem(player, Item(Items.STEEL_NAILS_1539, 30))) {
                                    lock(player, 1)
                                    animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)

                                    queueScript(player, 3, QueueStrength.SOFT) {
                                        animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                                        setVarbit(player, HorrorFromTheDeep.HFTD_BRIDGE_W, 1, true)
                                        sendDialogue(player, message)
                                        return@queueScript stopExecuting(player)
                                    }
                                }
                            }
                        }
                    }
                }

                // bridge has been fixed if you're past stage 1
                else -> sendDialogue(player, "You have already fixed this half of the bridge.")
            }
            return@onUseWith true
        }

        // cross the bridge. This movement shit could use improving, but I'm not smart enough to link the anims together at the right speed.
        on(brokenBridge, IntType.SCENERY, "Cross") { player, bridge ->
            val fromWest = bridge.id == Scenery.BROKEN_BRIDGE_4615
            val baseLocationX = if (fromWest) 2595 else 2598
            val locations = (0..3).map { Location(baseLocationX + it * (if (fromWest) 1 else -1), 3608, 0) }

            val animations = if (fromWest) {
                listOf(Animation(753), Animation(756), Animation(757), Animation(759))
            } else {
                listOf(Animation(752), Animation(754), Animation(755), Animation(758))
            }

            val direction = if (fromWest) Direction.EAST else Direction.WEST

            queueScript(player, 0, QueueStrength.SOFT) { counter ->
                if (counter in 0..2) {
                    ForceMovement.run(
                        player,
                        locations[counter],
                        locations[counter + 1],
                        animations[counter],
                        animations.getOrElse(counter + 1) { animations.last() },
                        direction
                    )
                    delayScript(player, 0)
                    return@queueScript false
                }
                stopExecuting(player)
                return@queueScript true
            }

            return@on true
        }
    }
}

private fun checkLight(player: Player) : Boolean {
    if (getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_TAR) == 1
        && getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LIT) == 1
        && getVarbit(player, HorrorFromTheDeep.HFTD_LIGHT_LENS) == 1
    ) {
        setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 4)
        sendMessage(player, "You have managed to repair the lighthouse torch!")
        return true
    } else {
        return false
    }
}

// starts the fight with the small dag
fun startBabyBossFight(player: Player) {
    // mark player as in fight
    setAttribute(player, HorrorFromTheDeep.HFTD_COMBAT, true)

    // create the boss
    val dag = DagonnothBabyNPC(NPCs.DAGANNOTH_1347, Location.create(2512, 4636, 0))

    // mark boss to target the player
    setAttribute(dag, HorrorFromTheDeep.HFTD_TARGET, player.name)

    // init boss and attack target
    dag.init()
    dag.isWalks = true
    dag.isAggressive = true
    dag.attack(player)
    registerHintIcon(player, dag)
}

// starts the fight with the dag mother
fun startBossFight(player: Player) {

    // mark player as in fight
    setAttribute(player, HorrorFromTheDeep.HFTD_COMBAT, true)

    // create the boss
    val dagMom = DagannothMotherNPC(NPCs.DAGANNOTH_MOTHER_1351, location(2520, 4645, 0))

    // mark boss to target the player
    setAttribute(dagMom, HorrorFromTheDeep.HFTD_TARGET, player.name)

    // init boss and attack target
    dagMom.init()
    dagMom.attack(player)
    registerHintIcon(player, dagMom)
    sendMessage(player, "A horror from the ocean depths...")
}
