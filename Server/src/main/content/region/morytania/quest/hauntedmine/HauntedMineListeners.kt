package content.region.morytania.quest.hauntedmine

import content.data.Quests
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * This covers listeners for the Abandoned Mine and Haunted Mine quest.
 */

class HauntedMineListeners : InteractionListener {

    companion object {
        const val HUMAN_CRAWL = 844
        const val HUMAN_WADE_IDLE = 777
    }

    override fun defineListeners() {

        /* * * * * * * * * * * * * * * * * * * * * *
         *
         *   ABANDONED MINE (AREA) RELATED LISTENERS
         *
         * * * * * * * * * * * * * * * * * * * * * */

        // stairs that go from the outside to level 2. the doors down there require the crystal mine key to unlock.
        on(Scenery.STAIRS_4919, IntType.SCENERY, "Walk-down") { player, _ ->
            teleport(player, Location.create(2792, 4592, 0))
        }

        // stairs that go from level 2 to the outside. the doors on level 2 require the crystal mine key to unlock.
        on(Scenery.STAIRS_4923, IntType.SCENERY, "Walk-up") { player, _ ->
            teleport(player, Location.create(3453, 3242, 0))
        }

        // minecart to climb over to get to mine entrance
        // Note: the osrs wiki states that the agility req is for the cart, and if you don't have it, you can just run around to the back entrance.
        on(Scenery.MINE_CART_4918, IntType.SCENERY, "Climb-over") { player, node ->
            if (hasLevelStat(player, Skills.AGILITY, 15)) {
                if (player.location == node.location.transform(-1, 0, 0)) {
                    forceMove(
                        player,
                        node.location.transform(-1, 0, 0),
                        node.location.transform(1, 0, 0),
                        0,
                        60,
                        anim = Animations.HUMAN_CLIMB_OVER_MEDIUM_839
                    )
                } else if (player.location == node.location.transform(1, 0, 0)) {
                    forceMove(
                        player,
                        node.location.transform(1, 0, 0),
                        node.location.transform(-1, 0, 0),
                        0,
                        60,
                        anim = Animations.HUMAN_CLIMB_OVER_MEDIUM_839
                    )
                }
            } else sendMessage(player, "You need at least 15 Agility to attempt this.")
            return@on true
        }

        // entrance and exit to Tarn's Lair
        // use ::setvarp 382 0 11 to finish haunted mine and activate entrance. Set to 0 to reset.
        on(intArrayOf(Scenery.ENTRANCE_20527, Scenery.PASSAGEWAY_20814), IntType.SCENERY, "Enter") { player, node ->
            when (node.id) {
                Scenery.ENTRANCE_20527 -> teleport(player, Location.create(3166, 4547, 0))
                Scenery.PASSAGEWAY_20814 -> teleport(player, Location.create(3424, 9660, 0))
            }
            return@on true
        }

        // the three outside entrances
        on(intArrayOf(Scenery.CART_TUNNEL_4913, Scenery.CART_TUNNEL_4914, Scenery.CART_TUNNEL_4915), IntType.SCENERY, "Crawl-down") { player, node ->

            // set quest stage 2
            if (getQuestStage(player, Quests.HAUNTED_MINE) <= 1) setQuestStage(player, Quests.HAUNTED_MINE, 2)

            animate(player, HUMAN_CRAWL)
            queueScript(player, 1) {
                when (node.id) {
                    Scenery.CART_TUNNEL_4913 -> teleport(player, Location.create(3436, 9637, 0))
                    Scenery.CART_TUNNEL_4914 -> teleport(player, Location.create(3405, 9631, 0))
                    Scenery.CART_TUNNEL_4915 -> teleport(player, Location.create(3409, 9623, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 1 exits back to the outside
        on(intArrayOf(Scenery.CART_TUNNEL_4920, Scenery.CART_TUNNEL_4921, Scenery.CART_TUNNEL_20524), IntType.SCENERY, "Crawl-through") { player, node ->
            animate(player, HUMAN_CRAWL)
            queueScript(player, 1) {
                when (node.id) {
                    Scenery.CART_TUNNEL_4920 -> teleport(player, Location.create(3441, 3232, 0))
                    Scenery.CART_TUNNEL_4921 -> teleport(player, Location.create(3429, 3233, 0))
                    Scenery.CART_TUNNEL_20524 -> teleport(player, Location.create(3428, 3225, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 1 to mine level 2
        on(Scenery.LADDER_4965, IntType.SCENERY, "Climb-down") { player, _ ->
            // ladder 4965 is reused in two locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.x) {
                    // north ladder
                    in 3412..3414 -> teleport(player, Location.create(2774, 4577, 0))
                    // south ladder
                    in 3421..3423 -> teleport(player, Location.create(2783, 4569, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 2 to mine level 1
        on(Scenery.LADDER_4966, IntType.SCENERY, "Climb-up") { player, _ ->
            // ladder 4965 is reused in two locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.x) {
                    // north ladder
                    in 2772..2774 -> teleport(player, Location.create(3412, 9633, 0))
                    // south ladder
                    in 2781..2783 -> teleport(player, Location.create(3422, 9624, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // These are the doors from the shortcut to level 2, and also the ones on level 6 that go to the crystals. Both require the crystal mine key.
        on(intArrayOf(Scenery.LARGE_DOOR_4963, Scenery.LARGE_DOOR_4964), IntType.SCENERY, "Open") { player, node ->
            if (inInventory(player, Items.CRYSTAL_MINE_KEY_4077)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else sendMessage(player, "This door is locked.")
            return@on true
        }

        // mine level 2 to 3
        on(Scenery.LADDER_4969, IntType.SCENERY, "Climb-down") { player, _ ->
            // ladder 4969 is reused in two locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.y) {
                    // north ladder
                    in 4598..4600 -> teleport(player, Location.create(2733, 4536, 0))
                    // south ladder
                    in 4566..4568 -> teleport(player, Location.create(2733, 4503, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 3 to 2
        on(Scenery.LADDER_4970, IntType.SCENERY, "Climb-up") { player, _ ->
            // ladder 4970 is reused in two locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.y) {
                    // north ladder
                    in 4534..4536 -> teleport(player, Location.create(2797, 4600, 0))
                    // south ladder
                    in 4502..4504 -> teleport(player, Location.create(2797, 4567, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 3 to 4
        on(Scenery.LADDER_4967, IntType.SCENERY, "Climb-down") { player, _ ->

            // set quest stage 3
            if (getQuestStage(player, Quests.HAUNTED_MINE) <= 2) setQuestStage(player, Quests.HAUNTED_MINE, 3)

            // ladder 4967 is reused in four locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.x) {
                    // north-east ladder
                    in 2731..2733 -> teleport(player, Location.create(2797, 4529, 0))
                    // north-west ladder
                    in 2709..2711 -> teleport(player, Location.create(2775, 4540, 0))
                    // south-east ladder
                    in 2724..2726 -> teleport(player, Location.create(2790, 4486, 0))
                    // south-west ladder
                    in 2695..2697 -> teleport(player, Location.create(2760, 4498, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // mine level 4 to 3
        on(Scenery.LADDER_4968, IntType.SCENERY, "Climb-up") { player, _ ->
            // ladder 4968 is reused in four locations, so we have to see which one the player is interacting with
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 1) {
                when (player.location.x) {
                    // north-east ladder
                    in 2795..2797 -> teleport(player, Location.create(2733, 4529, 0))
                    // north-west ladder
                    in 2773..2775 -> teleport(player, Location.create(2711, 4540, 0))
                    // south-east ladder
                    in 2788..2790 -> teleport(player, Location.create(2726, 4486, 0))
                    // south-west ladder
                    in 2759..2761 -> teleport(player, Location.create(2697, 4497, 0))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // iron pickaxe turns into possessed pickaxe
        on(NPCs.IRON_PICKAXE_1015, IntType.NPC, "Take") { player, node ->

            val npc = node as NPC
            npc.transform(NPCs.POSSESSED_PICKAXE_1536) // I use npc.transform because I don't want a timer to revert.
            npc.attack(player)

            return@on true
        }

        // stair from mine level 5 to 6
        on(Scenery.STAIRS_4971, IntType.SCENERY, "Walk-down") { player, _ ->
            // stair 4971 is reused in two locations, so we have to see which one the player is interacting with
            val loc = player.location.x
            if (inInventory(player, Items.GLOWING_FUNGUS_4075)) {

                // set quest stage 6. you can skip straight here from 4.
                if (getQuestStage(player, Quests.HAUNTED_MINE) <= 4) setQuestStage(player, Quests.HAUNTED_MINE, 6)

                when (loc) {
                    // left stairs to crystals
                    in 2691..2696 -> teleport(player, Location.create(2758, 4453, 0))
                    // right stairs to dayth
                    in 2745..2750 -> teleport(player, Location.create(2811, 4453, 0))
                }
            } else {
                when (loc) {
                    // left stairs to dark cave left side
                    in 2691..2696 -> teleport(player, Location.create(2712, 4593, 0))
                    // right stairs to dark cave right side
                    in 2745..2750 -> teleport(player, Location.create(2730, 4563, 0))
                }

                // set quest stage 5
                if (getQuestStage(player, Quests.HAUNTED_MINE) <= 4) setQuestStage(player, Quests.HAUNTED_MINE, 5)

                sendPlayerDialogue(player, "It's kind of dark down here. I should probably find a light source before going further.", FacialExpression.SCARED)
            }
            return@on true
        }

        // stair from mine level 6 to 5
        on(Scenery.STAIRS_4973, IntType.SCENERY, "Walk-up") { player, _ ->
            // stair 4973 is reused in two locations, so we have to see which one the player is interacting with
            when (player.location.x) {
                // left stairs
                2758 -> teleport(player, Location.create(2691, 4437, 0))
                // right stairs
                2811 -> teleport(player, Location.create(2750, 4437, 0))
            }
            return@on true
        }

        // stair from mine level 6 (darkened) to 5
        on(Scenery.STAIRS_4972, IntType.SCENERY, "Walk-up") { player, _ ->
            // stair 4972 is reused in two locations, so we have to see which one the player is interacting with
            when (player.location.x) {
                // left stairs
                2712 -> teleport(player, Location.create(2691, 4437, 0))
                // right stairs
                2730 -> teleport(player, Location.create(2750, 4437, 0))
            }
            return@on true
        }

        /* * * * * * * * * * * * * * * * * * * * * *
         *
         *   HAUNTED MINE (QUEST) RELATED LISTENERS
         *
         * * * * * * * * * * * * * * * * * * * * * */

        onUseWith(ITEM, Items.SALVE_SHARD_4082, Items.BALL_OF_WOOL_1759) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                addItem(player, Items.SALVE_AMULET_4081)
                sendMessage(player, "You string the salve amulet.")
            }
            return@onUseWith true
        }

        // you should not be able to pickpocket the key after the quest, but that should be taken care of by clearing the KeyMentioned attribute when the quest finishes.
        on(NPCs.ZEALOT_1528, IntType.NPC, "pickpocket") { player, _ ->
            if (getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                && !inInventory(player, Items.ZEALOTS_KEY_4078)
            ) {
                queueScript(player, 0, QueueStrength.SOFT) { counter ->
                    when (counter) {
                        0 -> {
                            animate(player, Animations.HUMAN_PICKPOCKETING_881)
                            return@queueScript delayScript(player, Animation(Animations.HUMAN_PICKPOCKETING_881).duration)
                        }

                        1 -> {
                            addItemOrDrop(player, Items.ZEALOTS_KEY_4078)
                            sendMessage(player, "You pick the zealot's pocket and retrieve a small silvery key.")
                            return@queueScript delayScript(player, 2)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else if (getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                && inInventory(player, Items.ZEALOTS_KEY_4078)
            ) {
                sendMessage(player, "I've already picked his pockets.")
            } else sendMessage(player, "I doubt he's got much of value on him.")
            return@on true
        }

        // mine cart you are supposed to put the fungus into (south) and take the fungus out of (north).
        on(Scenery.MINE_CART_4974, IntType.SCENERY, "Search") { player, _ ->
            // the mine cart is the same scenery in both locations, so we have to see which one the player is interacting with
            when (player.location.y) {
                // Check around a 1-tile radius of the north cart
                in 4536..4538 -> {
                    // take fungus out if it's in there and you did the cart thing correctly.
                    if (getAttribute(player, HauntedMine.ATTR_CART_SENT_SUCCESS, false)) {
                        sendDialogueOptions(player, "There's a glowing fungus I placed in here earlier.", "Take it.", "Leave it.")
                        addDialogueAction(player) { _, buttonId ->
                            when (buttonId) {
                                2 -> {
                                    if (freeSlots(player) >= 1) {
                                        addItem(player, Items.GLOWING_FUNGUS_4075)
                                        removeAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED)
                                        removeAttribute(player, HauntedMine.ATTR_CART_SENT_SUCCESS)
                                    } else sendMessage(player, "You need at least one free inventory space to take this.")
                                }
                            }
                        }
                    } else sendMessage(player, "The cart is empty.")
                }
                // Check around a 1-tile radius of the south cart
                in 4505..4507 -> {
                    // if there is fungus in the cart
                    if (getAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED, false)) {
                        // if you successfully sent a cart full of fungus to the other end
                        if (getAttribute(player, HauntedMine.ATTR_CART_SENT_SUCCESS, false)) {
                            sendMessage(player, "This cart is empty; perhaps I should check the cart at the other end of the tracks.")
                        } else sendMessage(player, "There's already a load of fungus in this cart.")
                    } else sendMessage(player, "The cart is empty.")
                }
            }
            return@on true
        }

        onUseWith(SCENERY, Items.GLOWING_FUNGUS_4075, Scenery.MINE_CART_4974) { player, used, _ ->
            // the mine cart is the same scenery in both locations, so we have to see which one the player is interacting with
            when (player.location.y) {
                // Check around a 1-tile radius of the north cart
                in 4536..4538 -> sendMessage(player, "I just went to all the effort of getting this over here. I'm not sending it back.")
                // Check around a 1-tile radius of the south cart
                in 4505..4507 -> {
                    if (getAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED, false)) {
                        sendMessage(player, "There's already a load of fungus in this cart.")
                    } else if (removeItem(player, used)) {
                        setAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED, true)
                        sendMessage(player, "You place the glowing fungus in the mine cart.")
                    }
                }
            }
            return@onUseWith true
        }

        // points settings
        on(Scenery.POINTS_SETTINGS_4949, IntType.SCENERY, "Check") { player, _ ->
            openOverlay(player, HauntedMine.POINTS_SETTINGS_IFACE)
            return@on true
        }

        // you can't drop the fungus
        on(Items.GLOWING_FUNGUS_4075, IntType.ITEM, "Drop") { player, node ->
            if (removeItem(player, node)) {
                produceGroundItem(player, Items.ASHES_592)
                sendMessage(player, "When you drop the fungus it crumbles mysteriously to dust.")
            }
            return@on true
        }

        // This is a general handler for all the levers (IDs and Varbits defined below)
        leverConfigs.forEach { cfg ->
            on(cfg.from, IntType.SCENERY, "Pull") { player, node ->
                replaceScenery(node as core.game.node.scenery.Scenery, cfg.to, 2)
                val newValue = if (getVarbit(player, cfg.varbit) == 0) 1 else 0
                setVarbit(player, cfg.varbit, newValue)
                sendMessage(player, "You pull the lever. The old points creak into place.")
                return@on true
            }
        }

        // water valve for lift
        on(Scenery.WATER_VALVE_4924, IntType.SCENERY, "Turn") { player, _ ->
            if (getAttribute(player, HauntedMine.ATTR_VALVE_UNLOCKED, false)) {
                if (getVarbit(player, HauntedMine.LIFT_MACHINERY_VARBIT) == 1
                    || getQuestStage(player, Quests.HAUNTED_MINE) >= 4
                ) {
                    sendMessage(player, "The valve is already open.")
                } else {
                    sendMessage(player, "You open the valve. Water begins to flow through the lift mechanism.")
                    setVarbit(player, HauntedMine.LIFT_MACHINERY_VARBIT, 1)

                    // spawn ghost, after a delay it will become visible and follow the player
                    val ghost = MischievousGhostNPC(NPCs.MISCHIEVOUS_GHOST_1551, location(2802, 4516, 0))
                    ghost.init()
                    setAttribute(ghost, HauntedMine.ATTR_HAUNTED_TARGET, player)
                    ghost.isInvisible = true
                    ghost.isWalks = false

                    queueScript(ghost, 14, QueueStrength.NORMAL) {
                        sendChat(ghost, "Ooooo Wooooo Woo")
                        ghost.isInvisible = false
                        ghost.isWalks = true
                        return@queueScript stopExecuting(ghost)
                    }
                }
            } else sendMessage(player, "The valve seems to be locked in position. There is a small keyhole in the side.")
            return@on true
        }

        // unlocking the water valve
        onUseWith(SCENERY, Items.ZEALOTS_KEY_4078, Scenery.WATER_VALVE_4924) { player, _, _ ->
            sendMessage(player, "The key unlocks the valve.")
            setAttribute(player, HauntedMine.ATTR_VALVE_UNLOCKED, true)
            return@onUseWith true
        }

        // lift from level 4 to 5
        on(intArrayOf(Scenery.LIFT_4937, Scenery.LIFT_4938, Scenery.LIFT_4940), IntType.SCENERY, "Go-down") { player, _ ->
            // if lift is active, or if the player has already been down once, descend.
            if (getVarbit(player, HauntedMine.LIFT_MACHINERY_VARBIT) == 1
                || getQuestStage(player, Quests.HAUNTED_MINE) >= 4
            ) {

                lock(player, 9)
                queueScript(player, 1, QueueStrength.SOFT) { counter ->
                    when (counter) {
                        0 -> {
                            // activate lift
                            sendMessage(player, "You get in the lift. Now powered, the lift descends further into the mines...")
                            if (getQuestStage(player, Quests.HAUNTED_MINE) <= 3) setQuestStage(player, Quests.HAUNTED_MINE, 4)
                            return@queueScript delayScript(player, 1)
                        }

                        1 -> {
                            // get dumped down a level in the water
                            teleport(player, Location.create(2725, 4456, 0))
                            animate(player, HUMAN_WADE_IDLE)
                            sendMessage(
                                player,
                                "...plunging you straight into the middle of a chamber flooded with water."
                            )
                            return@queueScript delayScript(player, animationDuration(Animation(HUMAN_WADE_IDLE)))
                        }

                        2 -> {
                            // move out
                            forceMove(
                                player,
                                Location.create(2725, 4456, 0),
                                Location.create(2725, 4452, 0),
                                0,
                                animationCycles(Animations.HUMAN_WADE_WITH_ROPE_776) * 2,
                                anim = Animations.HUMAN_WADE_WITH_ROPE_776
                            )
                            return@queueScript delayScript(player, animationDuration(Animation(Animations.HUMAN_WADE_WITH_ROPE_776)) * 2)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }

            } else sendMessage(player, "The lift is not active.")
            return@on true
        }

        // lift from level 5 to 4
        on(Scenery.LIFT_4942, IntType.SCENERY, "Go-up") { player, _ ->

            lock(player, 7)
            queueScript(player, 0, QueueStrength.SOFT) { counter ->
                when (counter) {
                    0 -> {
                        // move
                        forceMove(
                            player,
                            Location.create(2725, 4452, 0),
                            Location.create(2725, 4456, 0),
                            0,
                            animationCycles(Animations.HUMAN_WADE_WITH_ROPE_776) * 2,
                            anim = Animations.HUMAN_WADE_WITH_ROPE_776
                        )
                        return@queueScript delayScript(player, 5)
                    }

                    1 -> {
                        teleport(player, Location.create(2807, 4493, 0))
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }

        // taking the key starts the boss fight
        on(NPCs.INNOCENT_LOOKING_KEY_1543, IntType.NPC, "Take") { player, _ ->
            when {
                inInventory(player, Items.CRYSTAL_MINE_KEY_4077) -> {
                    sendDialogue(player, "I already have a key like that.")
                }

                getQuestStage(player, Quests.HAUNTED_MINE) >= 7 -> {
                    addItemOrDrop(player, Items.CRYSTAL_MINE_KEY_4077, 1)
                    sendDialogue(player, "You take the key. It doesn't resist anymore.")
                }

                isTreusDaythNearby(player) -> {
                    sendMessage(player, "I should deal with that ghost before trying to take the key again.")
                }

                else -> {
                    TreusDaythCutscene(player).start()
                }
            }
            return@on true
        }

        // crystals!
        on(intArrayOf(Scenery.CRYSTAL_OUTCROP_4926, Scenery.CRYSTAL_OUTCROP_4927, Scenery.CRYSTAL_OUTCROP_4928), IntType.SCENERY, "Cut") { player, _ ->
            if (hasLevelStat(player, Skills.CRAFTING, 35)) {
                if (freeSlots(player) >= 1
                    && getQuestStage(player, Quests.HAUNTED_MINE) >= 7
                ) {

                    // set quest stage 8
                    if (getQuestStage(player, Quests.HAUNTED_MINE) <= 7) setQuestStage(player, Quests.HAUNTED_MINE, 8)

                    // add crystal
                    addItem(player, Items.SALVE_SHARD_4082)

                    // complete the quest
                    if (getQuestStage(player, Quests.HAUNTED_MINE) != 100) {
                        finishQuest(player, Quests.HAUNTED_MINE)
                    }

                } else sendMessage(player, "You don't have enough inventory space to hold the crystal.")
            } else sendMessage(player, "You need at least 35 Crafting to do this.")
            return@on true
        }
    }

    // for the flooded lift
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.LIFT_4942), "Go-up") { _, _ ->
            return@setDest Location.create(2725, 4452, 0)
        }
    }
}

/* * * * * * * * * * * * * * * * * * * * * *
 *
 *   COMPANION FUNCTIONS USED ABOVE
 *
 * * * * * * * * * * * * * * * * * * * * * */

// The individual levers, their pulled counterparts, and the varbits for each.
class LeverConfig(
    val from: Int,
    val to: Int,
    val varbit: Int
)

val leverConfigs = listOf(
    LeverConfig(Scenery.LEVER_4951, Scenery.LEVER_4958, HauntedMine.LEVER_A_VARBIT),
    LeverConfig(Scenery.LEVER_4950, Scenery.LEVER_4958, HauntedMine.LEVER_B_VARBIT),
    LeverConfig(Scenery.LEVER_4952, Scenery.LEVER_4958, HauntedMine.LEVER_C_VARBIT),
    LeverConfig(Scenery.LEVER_4953, Scenery.LEVER_4958, HauntedMine.LEVER_D_VARBIT),
    LeverConfig(Scenery.LEVER_4954, Scenery.LEVER_4958, HauntedMine.LEVER_E_VARBIT),
    LeverConfig(Scenery.LEVER_4955, Scenery.LEVER_4958, HauntedMine.LEVER_I_VARBIT),
    LeverConfig(Scenery.LEVER_4956, Scenery.LEVER_4958, HauntedMine.LEVER_J_VARBIT),
    LeverConfig(Scenery.LEVER_4957, Scenery.LEVER_4958, HauntedMine.LEVER_K_VARBIT)
)

// spawns treus dayth and all the minecarts
fun startBossFight(player: Player) {

    // mark player as in fight
    setAttribute(player, HauntedMine.ATTR_DAYTH_FIGHT, true)

    // spawn carts
    NPC.create(NPCs.MINE_CART_1544, location(2781, 4462, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2785, 4456, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2791, 4456, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2783, 4446, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2785, 4447, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2791, 4447, 0)).init()
    NPC.create(NPCs.MINE_CART_1544, location(2793, 4446, 0)).init()

    // spawn dayth
    val dayth = NPC.create(NPCs.TREUS_DAYTH_1540, location(2784, 4457, 0))

    // mark dayth to target the player
    setAttribute(dayth, HauntedMine.ATTR_HAUNTED_TARGET, player.name)

    // start
    dayth.init()
    dayth.isWalks = true
    dayth.attack(player)
    registerHintIcon(player, dayth)
}

// checks if boss is spawned
fun isTreusDaythNearby(player: Player): Boolean {
    return getAttribute(player, HauntedMine.ATTR_DAYTH_FIGHT, false)
}