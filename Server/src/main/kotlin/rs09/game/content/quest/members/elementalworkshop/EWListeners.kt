package rs09.game.content.quest.members.elementalworkshop

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.*
import rs09.game.content.quest.members.elementalworkshop.EWUtils.BELLOWS_STATE
import rs09.game.content.quest.members.elementalworkshop.EWUtils.FURNACE_STATE
import rs09.game.content.quest.members.elementalworkshop.EWUtils.LEFT_WATER_CONTROL_STATE
import rs09.game.content.quest.members.elementalworkshop.EWUtils.RIGHT_WATER_CONTROL_STATE
import rs09.game.content.quest.members.elementalworkshop.EWUtils.WATER_WHEEL_STATE
import rs09.game.content.quest.members.elementalworkshop.EWUtils.currentStage
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger

class EWListeners : InteractionListener {

    /* Items */
    private val batteredBook = Item(Items.BATTERED_BOOK_2886)
    private val batteredKey = Item(Items.BATTERED_KEY_2887)
    private val slashedBook = Item(Items.SLASHED_BOOK_9715)
    private val emptyStoneBowl = Item(Items.A_STONE_BOWL_2888)
    private val fullStoneBowl = Item(Items.A_STONE_BOWL_2889)
    private val elementalOre = Item(Items.ELEMENTAL_ORE_2892)
    private val elementalMetal = Item(Items.ELEMENTAL_METAL_2893)
    private val elementalShield = Item(Items.ELEMENTAL_SHIELD_2890)
    private val bellowFixReqItems = intArrayOf(
        Items.NEEDLE_1733,
        Items.THREAD_1734,
        Items.LEATHER_1741
    )
    private val elementalShieldReqItems = intArrayOf(
        Items.SLASHED_BOOK_9715,
        Items.HAMMER_2347,
        Items.ELEMENTAL_METAL_2893
    )

    /* Scenery */
    private val bookcase = Scenery.BOOKCASE_26113
    private val lavaTrough = intArrayOf(
        Scenery.LAVA_TROUGH_18519, Scenery.LAVA_TROUGH_18520,
        Scenery.LAVA_TROUGH_18521, Scenery.LAVA_TROUGH_18522,
        Scenery.LAVA_TROUGH_18523
    )
    // Handling for on/off state (one obj id for both left and right)
    private val waterControls = intArrayOf(
        Scenery.WATER_CONTROLS_18509, Scenery.WATER_CONTROLS_18510
    )

    /* Animations */
    private val turnWaterControlAnimation = Animation(Animations.HUMAN_TURN_LARGE_VALVE_4861)
    private val fillStoneBowlAnimation = Animation(Animations.HUMAN_FILL_STONE_BOWL_4862)
    private val fixBellowsAnimation = Animation(Animations.HUMAN_SEW_LARGE_SCENERY_4862)
    private val smeltElementalBar = Animation(Animations.HUMAN_FURNACE_SMELTING_3243)

    /* Sound effects */
    private val fillStoneBowlSFX = Audio(Sounds.FILL_STONE_BOWL_1537, 1)
    private val fillFurnaceWithLavaSFX = Audio(Sounds.FILL_FURNACE_WITH_LAVA_1538)
    private val pullLeverResetGatesSFX = Audio(Sounds.PULL_LEVER_RESET_GATE_1540)
    private val turnWaterControlsSFX = Audio(Sounds.TURN_METAL_WATER_VALVE)
    private val pullLeverEnabledSFX = Audio(Sounds.PULL_LEVER_ENABLED_1547)
    private val pullLeverDisabledSFX = Audio(Sounds.PULL_LEVER_DISABLED_1548)
    private val bellowLeverSFX = Audio(Sounds.PULL_LEVER_GENERAL_2400)

    /* In-game locations */
    private val leftWaterControlsLocation = Location.create(2713, 9907, 0)
    private val rightWaterControlsLocation = Location.create(2726, 9907, 0)

    private val DISABLED = 0
    private val ENABLED = 1

    override fun defineListeners() {
        /* * * * * * * * * * * * * * * * * * *
         *  Seers Village House listeners   *
         * * * * * * * * * * * * * * * * * */
        // Bookcase (quest start)
        on(Scenery.BOOKCASE_26113, SCENERY, "search") { player, _ ->
            val stage = player.questRepository.getStage("Elemental Workshop I")

            if (stage < 3) {
                // Player already has battered book in inventory
                if (player.inventory.containsItem(batteredBook)) {
                    sendItemDialogue(
                        player, Item(Items.BATTERED_BOOK_2886), "There is a book here titled 'The Elemental Shield'. " +
                                "It can stay here, as you have a copy in your backpack."
                    )
                    return@on true
                }
                // Player needs to receive a battered book
                sendItemDialogue(player, Item(Items.BATTERED_BOOK_2886), "You find a book titled 'The Elemental Shield'.")
                addItem(player, batteredBook.id)
                return@on true
            }

            // --- AFTER QUEST START ---
            if (player.inventory.containsItem(slashedBook)) {
                sendItemDialogue(player, Item(Items.SLASHED_BOOK_9715),
                    "There is a book here titled 'The Elemental Shield'. " +
                            "It can stay here, as you have a copy in your backpack."
                )
                player.inventory.addIfDoesntHave(batteredKey)
                return@on true
            }

            sendItemDialogue(player, Item(Items.SLASHED_BOOK_9715), "You find a book titled 'The Elemental Shield'.")
            addItem(player, slashedBook.id)
            player.inventory.addIfDoesntHave(batteredKey)
            return@on true
        }

        // Knife on Battered book -> Slashed book + Battered key
        onUseWith(ITEM, Items.KNIFE_946, Items.BATTERED_BOOK_2886) { player, _, with ->
            val stage = currentStage(player)
            if (stage >= 1) {
                lock(player, 2)
                player.pulseManager.run(object : Pulse() {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            0 -> sendMessage(player, "You make a small cut in the spine of the book.")
                            1 -> {
                                sendMessage(player, "Inside you find a small, old, battered key.")
                                replaceSlot(player, with.asItem().slot, slashedBook)
                                addItemOrDrop(player, Items.BATTERED_KEY_2887)
                                setQuestStage(player, "Elemental Workshop I", 3)
                                return true
                            }
                        }
                        count++
                        return false
                    }
                })
                return@onUseWith true
            } else {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith true
            }
        }


        /* * * * * * * * * * * * * * * * * * *
         *  Seers Village Smithy listeners  *
         * * * * * * * * * * * * * * * * * */
        // Odd looking wall handler
        on(intArrayOf(Scenery.ODD_LOOKING_WALL_26114, Scenery.ODD_LOOKING_WALL_26115), SCENERY, "open") { player, wall ->
            // Player is allowed to exit without key
            if (player.location == Location.create(2710, 3496, 0) || player.location == Location.create(2709, 3496, 0)) {
                DoorActionHandler.handleAutowalkDoor(player, wall.asScenery())
                return@on true
            }
            // Player does not have battered key in inventory
            if (!inInventory(player, Items.BATTERED_KEY_2887)) {
                sendMessage(player, "You see a small hole in the wall but no way to open it.")
                return@on true
            }
            // Increment quest stage
            if (questStage(player, "Elemental Workshop I") < 5) {
                setQuestStage(player, "Elemental Workshop I", 5)
            }
            // Allow player through the wall
            sendMessage(player, "You use the battered key to open the doors.")
            DoorActionHandler.handleAutowalkDoor(player, wall.asScenery())
            return@on true
        }

        on(Scenery.STAIRCASE_3415, SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2716, 9888, 0))
            // If it is the players first time in the workshop
            if (currentStage(player) < 6) {
                sendPlayerDialogue(player,
                    "Now to explore this area thoroughly, to find what " +
                        "forgotten secrets it contains.", FacialExpression.NEUTRAL)
                setQuestStage(player, "Elemental Workshop I", 7)
            }
            return@on true
        }

        on(Scenery.STAIRCASE_3416, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(2709, 3498, 0))
            return@on true
        }


        /* * * * * * * * * * * * * * * *
         *  Workshop Area listeners   *
         * * * * * * * * * * * * * * */
        // Center hatch, inaccessible
        // NOTE: REMOVE ME/ADD CORRECT CHECKS FOR ELEMENTAL WORKSHOP II
        on(Scenery.HATCH_18595, SCENERY, "open") { player, _ ->
            sendDialogue(player, "You're unable to open the locked hatch.")
            return@on true
        }

        // Stone bowl box, player can get more at any time
        on(Scenery.BOXES_3397, SCENERY, "search") { player, _ ->
            // If the player doesn't have a stone bowl in their inventory
            if (player.inventory.addIfDoesntHave(emptyStoneBowl)) {
                sendMessage(player, "You find a stone bowl.")
            } else {
                sendMessage(player, "It's empty.")
            }
            return@on true
        }

        // Needle crate, player can only receive once
        on(Scenery.CRATE_3400, SCENERY, "search") { player, _ ->
            if (!getAttribute(player, "/save:ew1:got_needle", false)) {
                setAttribute(player, "/save:ew1:got_needle", true)
                addItem(player, Items.NEEDLE_1733)
                sendMessage(player, "You find a needle.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        // Leather crate, player can only receive once
        on(Scenery.CRATE_3394, SCENERY, "search") { player, _ ->
            if (!getAttribute(player, "/save:ew1:got_leather", false)) {
                setAttribute(player, "/save:ew1:got_leather", true)
                addItem(player, Items.LEATHER_1741)
                sendMessage(player, "You find some leather.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        // Workbenches/Anvil
        onUseWith(SCENERY, Items.ELEMENTAL_METAL_2893, Scenery.WORKBENCH_3402) { player, used, _ ->
            // Warn player their smithing level is too low to make the shield
            if (player.skills.getLevel(Skills.SMITHING) < 20) {
                sendMessage(player, "You need a smithing level of 20 to create an elemental shield.")
                return@onUseWith true
            }
            // Warn player they don't have the required book in their inventory
            if (!player.inventory.containsAtLeastOneItem(intArrayOf(Items.SLASHED_BOOK_9715, Items.BATTERED_BOOK_2886))) {
                sendMessage(player, "You are unsure what to do with the bar. Perhaps there is a book to help guide you.")
                return@onUseWith true
            }
            // Warn player they don't have a hammer in their inventory
            if (!inInventory(player, Items.HAMMER_2347)) {
                sendMessage(player, "You don't have a tool available to shape the metal.")
                return@onUseWith true
            }
            // Sanity error check (Should never get thrown)
            if (!player.inventory.containsAll(*elementalShieldReqItems)) {
                SystemLogger.logErr("${player.username} tried to forge an elemental shield without all the required items.")
                return@onUseWith false
            }
            // Successfully smith the elemental shield
            replaceSlot(player, used.asItem().slot, elementalShield)
            sendMessage(player, "Following the instructions in the book you make an elemental shield.")
            player.questRepository.getQuest("Elemental Workshop I").finish(player)
            return@onUseWith true
        }


        /* * * * * * * * * * * * * *
         *  Fire room listeners   *
         * * * * * * * * * * * * */
        // Lava trough stone bowl (empty)
        onUseWith(SCENERY, Items.A_STONE_BOWL_2888, *lavaTrough) { player, used, trough ->
            lock(player, (animationDuration(fillStoneBowlAnimation) + 1))
            runTask(player) {
                face(player, trough)
                playAudio(player, fillStoneBowlSFX)
                animate(player, fillStoneBowlAnimation)
                replaceSlot(player, used.asItem().slot, fullStoneBowl)
                sendMessage(player, "You fill the bowl with hot lava.")
            }
            return@onUseWith true
        }

        // Lava trough stone bowl (filled with lava)
        onUseWith(SCENERY, fullStoneBowl.id, *lavaTrough) { player, _, _ ->
            sendMessage(player, "The bowl is already full of lava.")
            return@onUseWith true
        }

        // Pour lava into unlit furnace
        onUseWith(SCENERY, fullStoneBowl.id, Scenery.FURNACE_18525) { player, used, _ ->
            player.faceLocation(Location.create(2726, 9875, 0))
            lock(player, 3)
            submitIndividualPulse(player, object : Pulse() {
                var count = 0
                override fun pulse(): Boolean {
                    when (count) {
                        0 -> {
                            replaceSlot(player, used.asItem().slot, emptyStoneBowl)
                            playAudio(player, fillFurnaceWithLavaSFX)
                            sendMessage(player, "You empty the lava into the furnace.")
                        }
                        2 -> {
                            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, FURNACE_STATE, ENABLED, true)
                            sendMessage(player, "The furnace bursts to life.")
                            return true
                        }
                    }
                    count++
                    return false
                }
            })
            return@onUseWith true
        }

        // Pour lava into lit furnace
        onUseWith(SCENERY, fullStoneBowl.id, Scenery.FURNACE_18526) { player, used, _ ->
            player.faceLocation(Location.create(2726, 9875, 0))
            runTask(player) {
                playAudio(player, fillFurnaceWithLavaSFX)
                replaceSlot(player, used.asItem().slot, emptyStoneBowl)
                sendMessage(player, "The lava makes little difference to the furnace's searing heat.")
            }
            return@onUseWith true
        }

        // Use elemental ore on lit furnace
        onUseWith(SCENERY, Items.ELEMENTAL_ORE_2892, Scenery.FURNACE_18526) { player, _, _ ->
            player.faceLocation(Location.create(2726, 9875, 0))
            // Warn player their smithing level is too low to smelt the ore
            if (player.skills.getLevel(Skills.SMITHING) < 20) {
                sendMessage(player, "You need a smithing level of 20 to smelt elemental ore.")
                return@onUseWith true
            }
            // Warn player the bellows must be on so the furnace is properly heated
            if (!EWUtils.bellowsEnabled(player)) {
                sendMessage(player, "The furnace needs to be hotter to be of any use.")
                return@onUseWith true
            }
            // Warn player they don't have enough coal to smelt the bar
            if (amountInInventory(player, Items.COAL_453) < 4) {
                sendMessage(player, "You need four heaps of coal to smelt elemental ore.")
                return@onUseWith true
            }

            val duration = animationDuration(smeltElementalBar)
            lock(player, (duration + 1))
            // Run the "smelting" pulse
            submitIndividualPulse(player, object : Pulse() {
                var count = 0
                override fun pulse(): Boolean {
                    when (count) {
                        0 -> {
                            removeItem(player, Item(Items.COAL_453, 4))
                            removeItem(player, elementalOre)
                            animate(player, Animations.HUMAN_FURNACE_SMELTING_3243)
                            sendMessage(player, "You place the elemental ore and four heaps of coal into the furnace.")
                        }
                        duration -> {
                            addItem(player, Items.ELEMENTAL_METAL_2893)
                            sendMessage(player, "You retrieve a bar of elemental metal.")
                            rewardXP(player, Skills.SMITHING, 7.0)
                            return true
                        }
                    }
                    count++
                    return false
                }
            })
            return@onUseWith true
        }


        /* * * * * * * * * * * * * *
         *  Water room listeners  *
         * * * * * * * * * * * * */
        // Water controls
        on(waterControls, SCENERY, "turn") { player, _ ->
            // Notify player they can't change water control values while water wheel is running
            if (EWUtils.waterWheelEnabled(player)) {
                sendMessage(player, "Now that the water wheel is running, the valve seems locked off.")
                return@on true
            }

            // Varbit offset
            val offset: Int
            // Whether it is enabled or disabled (used for xor toggle)
            val enabled: Int
            when (player.location) {
                // Check left control
                leftWaterControlsLocation -> {
                    offset = LEFT_WATER_CONTROL_STATE
                    enabled = EWUtils.leftWaterControlBit(player) xor 0x1
                }
                // Check right control
                rightWaterControlsLocation -> {
                    offset = RIGHT_WATER_CONTROL_STATE
                    if (EWUtils.leftWaterControlEnabled(player)) {
                        enabled = 0
                    } else {
                        enabled = EWUtils.rightWaterControlBit(player) xor 0x1
                    }
                }
                // Sanity control check
                else -> {
                    offset = -1
                    enabled = 0
                    SystemLogger.logErr("Unhandled location when determining enabled water controls! ${player.location}")
                    return@on false
                }
            }

            // Run the turn valve pulse
            lock(player, (animationDuration(turnWaterControlAnimation) + 1))
            submitIndividualPulse(player, object : Pulse() {
                var count = 0
                override fun pulse(): Boolean {
                    when (count) {
                        0 -> {
                            playAudio(player, turnWaterControlsSFX)
                            animate(player, turnWaterControlAnimation)
                            sendMessage(player, "You turn the handle.")
                        }
                        3 -> {
                            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, offset, enabled, true)
                            return true
                        }
                    }
                    count++
                    return false
                }
            })
            return@on true
        }

        // Water wheel lever handler
        on(Scenery.LEVER_3406, SCENERY, "pull") { player, lever ->
            // Default will happen no matter what
            lock(player, 2)
            sendMessage(player, "You pull the lever")
            replaceScenery(lever.asScenery(), Scenery.LEVER_3417, 2)

            // Check to see if the water wheel is running
            if (EWUtils.waterWheelEnabled(player)) {
                playAudio(player, pullLeverDisabledSFX)
                sendMessage(player, "You hear the sound of a water wheel coming to a standstill.")
                setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, WATER_WHEEL_STATE, DISABLED, true)
                setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, BELLOWS_STATE, DISABLED, true)
                return@on true
            }

            // Check to reset both valves to the "off" position if both aren't green
            if (!EWUtils.leftWaterControlEnabled(player) || !EWUtils.rightWaterControlEnabled(player)) {
                playAudio(player, pullLeverResetGatesSFX)
                sendMessage(player, "You hear the sound of the flow gates resetting.")
                setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, LEFT_WATER_CONTROL_STATE, DISABLED, true)
                setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, RIGHT_WATER_CONTROL_STATE, DISABLED, true)
                return@on true
            }

            // If both of the above are false, the water wheel can start running
            playAudio(player, pullLeverEnabledSFX)
            sendMessage(player, "You hear the sound of a water wheel starting up.")
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, WATER_WHEEL_STATE, ENABLED, true)
            return@on true
        }


        /* * * * * * * * * * * * * *
         *  Air room listeners    *
         * * * * * * * * * * * * */
        on(Scenery.BELLOWS_18516, SCENERY, "fix") { player, bellows ->
            player.faceLocation(Location.create(2736, 9883, 0))
            // Warn player bellows are already fixed
            if (getAttribute(player, "/save:ew1:bellows_fixed", false)) {
                sendMessage(player, "The bellows already look fixed to you.")
                return@on true
            }
            // Warn player they don't have a high enough crafting level
            if (player.skills.getLevel(Skills.CRAFTING) < 20) {
                sendMessage(player, "You need a crafting level of 20 to fix the bellows.")
                return@on true
            }
            // Warn player they don't have the required items
            if (!player.inventory.containsAll(*bellowFixReqItems)) {
                sendMessage(player, "You don't have the required item(s) to fix the bellows.")
                return@on true
            }
            // If the player has all the requirements to fix the bellows
            lock(player, (animationDuration(fixBellowsAnimation) + 1))
            runTask(player) {
                removeItem(player, Items.LEATHER_1741)
                removeItem(player, Item(Items.THREAD_1734, 1))
                animate(player, fixBellowsAnimation)
                sendMessage(player, "You stitch the leather over the hole in the bellows.")
                setAttribute(player, "/save:ew1:bellows_fixed", true)
            }
            return@on true
        }

        on(Scenery.LEVER_3409, SCENERY, "pull") { player, lever ->
            // Warn player they have to fix the bellows before pulling the lever
            if (!getAttribute(player, "/save:ew1:bellows_fixed", false)) {
                sendPlayerDialogue(player, "I shouldn't risk damaging the bellows any further.")
                return@on true
            }
            // Pull the lever checks
            lock(player, 2)
            sendMessage(player, "You pull the lever")
            replaceScenery(lever.asScenery(), Scenery.LEVER_3417, 2)
            playAudio(player, bellowLeverSFX)

            // Warn player the water wheel is not running
            if (!EWUtils.waterWheelEnabled(player)) {
                sendMessage(player, "Nothing happens; the lever resets itself.")
                return@on true
            }
            // Bellows lever "OFF" state
            if (EWUtils.bellowsEnabled(player)) {
                sendMessage(player, "The bellows stop pumping air.")
                setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, BELLOWS_STATE, DISABLED, true)
                return@on true
            }

            // Bellows lever "ON" state
            sendMessage(player, "The bellows pump air down the pipe.")
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, BELLOWS_STATE, ENABLED, true)
            return@on true
        }
    }
    override fun defineDestinationOverrides() {
        setDest(SCENERY, intArrayOf(Scenery.FURNACE_18525, Scenery.FURNACE_18526), "use") { _, _ ->
            return@setDest Location.create(2724,9875)
        }
        setDest(SCENERY, intArrayOf( Scenery.BELLOWS_18516), "fix") { _, _ ->
            return@setDest Location.create(2733, 9884, 0)
        }
    }
}