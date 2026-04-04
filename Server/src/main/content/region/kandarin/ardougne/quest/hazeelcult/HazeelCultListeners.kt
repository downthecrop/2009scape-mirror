package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.carnilleanArc
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.*

class HazeelCultListeners : InteractionListener {

    override fun defineListeners() {
        on(CLIVET_STAIRS, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, location(2586, 3237, 0))
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        on(CAVE_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, location(2570, 9682, 0))
            sendMessage(player, "You enter the cave.")
            return@on true
        }

        on(CLIVET_RAFT, IntType.SCENERY, "board") { player, node ->
            // to use the raft, you must also be at either
            // Carnillean Arc Stage 2 or greater,
            // Mahjarrat Arc Stage 3 with Hazeel's Mark,
            // Mahjarrat Arc Stage 4 or greater
            val questStage = getQuestStage(player, Quests.HAZEEL_CULT)
            val questReq = if(
                (carnilleanArc(player) && questStage >= 2) ||
                (mahjarratArc(player) && inEquipmentOrInventory(player, Items.HAZEELS_MARK_2406) && questStage >= 3) ||
                (mahjarratArc(player) && questStage >= 4)
            ) {true} else false
            val raftUnlock = if(
                getAttribute(player, attrSewer1R, false) &&
                getAttribute(player, attrSewer2R, false) &&
                getAttribute(player, attrSewer3L, false) &&
                getAttribute(player, attrSewer4R, false) &&
                getAttribute(player, attrSewer5R, false) &&
                questReq
                    ) {true} else false


            // check which raft this is.
            val x = node.location.x

            if (x == 2567) { // raft to cultists. only use if you've turned the valves.
                if (!raftUnlock) {
                    sendNPCDialogue(player, CLIVET_NPC, "Hey! I don't remember saying you could use that raft!", FacialExpression.ANNOYED)
                } else {
                    teleport(player, location(2606, 9692, 0))
                    sendDialogue(player, "The raft washes up the sewer, past the islands until it reaches the end of the sewer passage.")
                }
            } else { // raft away from cultists. no check on this one since the valve attributes aren't saved.
                teleport(player, location(2567, 9680, 0))
                sendDialogue(player, "The raft flows back to the cave entrance.")
            }
            return@on true
        }

        /*
         * Valve 1 - This is right across the bridge south of the Carnilleans' house. Turn it right.
         * Valve 2 - This is right behind Carnilleans' house. Turn it right.
         * Valve 3 - This is north of the cave. Turn it left.
         * Valve 4 - This is south of the zoo's penguin cage. Turn it right.
         * Valve 5 - Go southeast of the fourth valve, past the monk. Turn it right.
         *
         * All 4 of the below sources turn the valves in different orders. Text quest guides make no mention of needing a specific order,
         * even though Clivet's bad-side dialogue makes it seem like it. For this implementation, the player needs to set each valve in the
         * correct direction, order of the valves does not matter.
         *
         * Bad side - turns in order: https://www.youtube.com/watch?v=ZYPB823IyRk
         * Good side - turns out of order: https://www.youtube.com/watch?v=OuEvnA6Cyrw
         * Good side - turns out of order: https://www.youtube.com/watch?v=JG7E3uVx4rs
         * Bad side - turns out of order: https://www.youtube.com/watch?v=T8JyyhDEVSg
         *
         * I allow the valve attributes to be set even if quest is unstarted, but getting on the raft checks the quest stage.
         */

        // valves 1, 2, 4, 5 should be turned right
        on(SEWERS, IntType.SCENERY, "turn-right") { player, node ->
            if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true

            val currentSewer = node.id
            when (currentSewer) {
                SEWER_1 -> setAttribute(player, attrSewer1R, true)
                SEWER_2 -> setAttribute(player, attrSewer2R, true)
                SEWER_3 -> removeAttribute(player, attrSewer3L)
                SEWER_4 -> setAttribute(player, attrSewer4R, true)
                SEWER_5 -> setAttribute(player, attrSewer5R, true)
            }

            lock(player, 4)
            animate(player, TURN_VALVE_ANIMATION)
            sendDialogueLines(player, "You turn the large metal valve to the right. Beneath your feet you", "can hear the sudden sound of rushing water from the sewer.")
            return@on true
        }

        // valve 3 should be turned left
        on(SEWERS, IntType.SCENERY, "turn-left") { player, node ->
            if (!player.location.withinDistance(node.asScenery().location, 2)) return@on true

            val currentSewer = node.id
            when (currentSewer) {
                SEWER_1 -> removeAttribute(player, attrSewer1R)
                SEWER_2 -> removeAttribute(player, attrSewer2R)
                SEWER_3 -> setAttribute(player, attrSewer3L, true)
                SEWER_4 -> removeAttribute(player, attrSewer4R)
                SEWER_5 -> removeAttribute(player, attrSewer5R)
            }

            lock(player, 4)
            animate(player, TURN_VALVE_ANIMATION)
            sendDialogueLines(player, "You turn the large metal valve to the left. Beneath your feet you", "can hear the sudden sound of rushing water from the sewer.")
            return@on true
        }

        // this wardrobe contains some poison and an amulet, used to convince ceril that jones is a baddie
        on(WARDROBE, IntType.SCENERY, "search") { player, _ ->
            openDialogue(player, WardrobeRevealDialogueFile())
            return@on true
        }

        // siding with Hazeel and trying to poison the Carnilleans
        onUseWith(IntType.SCENERY, POISON, COOKING_RANGE) { player, _, _ ->
            if (mahjarratArc(player) && removeItem(player, POISON)) {
                sendDialogueLines(player, "You pour the poison into the hot pot.", "The poison dissolves into the soup.")
                setQuestStage(player, Quests.HAZEEL_CULT, 3)
            } else {
                sendNPCDialogue(player, NPCs.CLAUS_THE_CHEF_886, "Oi - I don't want people messing around with my range!")
            }
            return@onUseWith true
        }

        // crate in the basement. they key can only be obtained in the mahjarrat arc, which locks the secret passage, chest, and hazeel scroll to that arc as well.
        on(CRATE, IntType.SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.HAZEEL_CULT) >= 1 &&
                mahjarratArc(player) &&
                !inInventory(player, CHEST_KEY)
            ) {
                sendDialogue(player, "You search the crate. Under various food packages you find an old rusty key.")
                addItemOrDrop(player, CHEST_KEY, 1)
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        // secret wall on the way up to retrieve the hazeel scroll
        on(SECRET_PASSAGE, IntType.SCENERY, "Knock-at") { player, node ->
            if (inInventory(player, CHEST_KEY) && player.location.y == 3274) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "There is something odd about the wall here.")
                sendMessage(player, "You find a secret passageway")
            } else if (player.location.y == 3275) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        // chest with hazeel scroll. unlocks with key, opens, and contains the scroll.
        on(Scenery.CHEST_2856, IntType.SCENERY, "Open") { player, _ ->
            sendMessage(player, "This chest is locked shut.")
            return@on true
        }

        // chest with hazeel scroll. unlocks with key, opens, and contains the scroll.
        onUseWith(IntType.SCENERY, CHEST_KEY, Scenery.CHEST_2856) { player, _, _ ->
            sendMessage(player, "You unlock the chest.")
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            addScenery(Scenery.CHEST_2857,CHEST_LOCATION, 2, 10)
            removeScenery(core.game.node.scenery.Scenery(Scenery.CHEST_2856, Location(2565, 3272, 2)))
            return@onUseWith true
        }

        // chest with hazeel scroll. unlocks with key, opens, and contains the scroll.
        on(Scenery.CHEST_2857, IntType.SCENERY, "Close") { _, _ ->
            addScenery(Scenery.CHEST_2856, CHEST_LOCATION, 2, 10)
            removeScenery(core.game.node.scenery.Scenery(Scenery.CHEST_2857, CHEST_LOCATION))
            return@on true
        }

        // chest with hazeel scroll. unlocks with key, opens, and contains the scroll.
        on(Scenery.CHEST_2857, IntType.SCENERY, "Search") { player, _ ->
            val hasScroll = hasAnItem(player, HAZEEL_SCROLL).container != null
            if (hasScroll) {
                sendDialogue(player, "You already have the scroll from this chest.")
                return@on true
            }
            addItemOrDrop(player, HAZEEL_SCROLL, 1)
            sendItemDialogue(player, HAZEEL_SCROLL, "Inside the chest you find the Scroll of Hazeel.")
            setQuestStage(player, Quests.HAZEEL_CULT, 5)
            return@on true
        }
    }

    // interact locations for the sewer valves
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(SEWER_1), "turn-right", "turn-left") { _, _ ->
            return@setDest Location.create(2563, 3246, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_2), "turn-right", "turn-left") { _, _ ->
            return@setDest Location.create(2571, 3263, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_3), "turn-right", "turn-left") { _, _ ->
            return@setDest Location.create(2586, 3244, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_4), "turn-right", "turn-left") { _, _ ->
            return@setDest Location.create(2597, 3262, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(SEWER_5), "turn-right", "turn-left") { _, _ ->
            return@setDest Location.create(2610, 3242, 0)
        }

    }

    companion object {
        const val HAZEEL_CULT_VARP = 223

        const val attrSewer1R = "quest:hazeelcult-sewer1"
        const val attrSewer2R = "quest:hazeelcult-sewer2"
        const val attrSewer3L = "quest:hazeelcult-sewer3"
        const val attrSewer4R = "quest:hazeelcult-sewer4"
        const val attrSewer5R = "quest:hazeelcult-sewer5"

        const val attrHazeel = "/save:quest:hazeelcult-hazeel"          // true when you agree to help Clivet
        const val attrCarnillean = "/save:quest:hazeelcult-carnillean"  // true when you DON'T agree to help Clivet

        const val CLIVET_NPC = NPCs.CLIVET_893
        const val CLIVET_RAFT = Scenery.RAFT_2849

        const val CAVE_ENTRANCE = Scenery.CAVE_ENTRANCE_2852
        const val CLIVET_STAIRS = Scenery.STAIRS_2853

        const val HAZEEL_SCROLL = Items.HAZEEL_SCROLL_2403

        const val SEWER_1 = Scenery.SEWER_VALVE_2844 // Valve 1 - This is right across the bridge south of the Carnilleans' house. Turn it right.
        const val SEWER_2 = Scenery.SEWER_VALVE_2845 // Valve 2 - This is right behind Carnilleans' house. Turn it right.
        const val SEWER_3 = Scenery.SEWER_VALVE_2846 // Valve 3 - This is north of the cave. Turn it left.
        const val SEWER_4 = Scenery.SEWER_VALVE_2847 // Valve 4 - This is south of the zoo's penguin cage. Turn it right.
        const val SEWER_5 = Scenery.SEWER_VALVE_2848 // Valve 5 - Go southeast of the fourth valve, past the monk. Turn it right.

        val SEWERS = intArrayOf(SEWER_1, SEWER_2, SEWER_3, SEWER_4, SEWER_5)

        const val WARDROBE = Scenery.WARDROBE_2851

        const val TURN_VALVE_ANIMATION = 4861

        const val CRATE = Scenery.CRATE_34585
        const val CHEST_KEY = Items.CHEST_KEY_709
        const val POISON = Items.POISON_273
        const val COOKING_RANGE = Scenery.COOKING_RANGE_2859

        const val SECRET_PASSAGE = Scenery.WALL_26940

        val CHEST_LOCATION = Location(2565, 3272, 2)
    }
}
