package content.region.kandarin.seers.quest.elementalworkshop

import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.*
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.BELLOWS_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.FURNACE_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.LEFT_WATER_CONTROL_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.RIGHT_WATER_CONTROL_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.WATER_WHEEL_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.currentStage
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.Log
import content.data.Quests
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.api.handleDoor
import core.game.global.action.DoorHelper
import core.tools.prependArticle

/**
 * Listeners for the Elemental Workshop I quest
 *
 *  @author Woah, with love
 */
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
    private val smithElementalShield = Animation(Animations.HUMAN_COOKING_RANGE_896)

    /* Sound effects */
    private val fillStoneBowlSFX = Sounds.FILL_STONE_BOWL_1537
    private val fillFurnaceWithLavaSFX = Sounds.FILL_FURNACE_WITH_LAVA_1538
    private val pullLeverResetGatesSFX = Sounds.PULL_LEVER_RESET_GATE_1540
    private val turnWaterControlsSFX = Sounds.TURN_METAL_WATER_VALVE
    private val pullLeverEnabledSFX = Sounds.PULL_LEVER_ENABLED_1547
    private val pullLeverDisabledSFX = Sounds.PULL_LEVER_DISABLED_1548
    private val bellowLeverSFX = Sounds.PULL_LEVER_GENERAL_2400
    private val smithElementalShieldSFX = Sounds.ANVIL_4_2721

    /* In-game locations */
    private val leftWaterControlsLocation = Location.create(2713, 9907, 0)
    private val rightWaterControlsLocation = Location.create(2726, 9907, 0)

    /* Special interactions */
    private val furnaceIDS = intArrayOf(4304, 6189, 11010, 11666, 12100, 12809, 14921, 18497, 26814, 30021, 30510, 36956, 37651)

    private val DISABLED = 0
    private val ENABLED = 1

    override fun defineListeners() {
        /* * * * * * * * * * * * * * * * * * *
         *  Seers Village House listeners   *
         * * * * * * * * * * * * * * * * * */
        // Bookcase (quest start)
        on(Scenery.BOOKCASE_26113, IntType.SCENERY, "search") { player, _ ->
            val stage = currentStage(player)

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
                addItemOrDrop(player, batteredBook.id)
                return@on true
            }

            // --- AFTER QUEST START ---

            // if player doesn't have book and doesn't have key
            if (!inInventory(player, slashedBook.id) && !inInventory(player, batteredKey.id)) {
                addItemOrDrop(player, batteredKey.id)
                addItemOrDrop(player, slashedBook.id)
                openDialogue(player, object : DialogueLabeller() {
                    override fun addConversation() {
                        item(slashedBook, "You find a book titled 'The Elemental Shield'.")
                        item(batteredKey,"You also find a key.")
                    }
                })
                return@on true
            }

            // if player doesn't have book and does have key
            if (!inInventory(player, slashedBook.id) && inInventory(player, batteredKey.id)) {
                addItemOrDrop(player, slashedBook.id)
                sendItemDialogue(player, slashedBook, "You find a book titled 'The Elemental Shield'.")
                return@on true
            }

            // if player does have book but doesn't have key
            if (inInventory(player, slashedBook.id) && !inInventory(player, batteredKey.id)) {
                addItemOrDrop(player, batteredKey.id)
                openDialogue(player, object : DialogueLabeller() {
                    override fun addConversation() {
                        item(slashedBook, "There is a book here titled 'The Elemental Shield'. " + "It can stay here, as you have a copy in your backpack.")
                        item(batteredKey,"You also find a key.")
                    }
                })
                return@on true
            }

            // if player does have book and does have key
            if (inInventory(player, slashedBook.id) && inInventory(player, batteredKey.id)) {
                sendItemDialogue(player, slashedBook, "There is a book here titled 'The Elemental Shield'. " + "It can stay here, as you have a copy in your backpack.")
                return@on true
            }

            return@on true
        }

        // Knife on Battered book -> Slashed book + Battered key
        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.BATTERED_BOOK_2886) { player, _, with ->
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
                                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_I, 3)
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
        on(intArrayOf(Scenery.ODD_LOOKING_WALL_26114, Scenery.ODD_LOOKING_WALL_26115), IntType.SCENERY, "open") { player, wall ->
            // Nothing interesting happens if quest wasn't started.
            if (currentStage(player) < 1) {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }
            // Player is allowed to exit without key
            if (player.location == Location.create(2710, 3496, 0) || player.location == Location.create(2709, 3496, 0)) {
                handleDoor(player, wall.asScenery())
                return@on true
            }
            // Player does not have battered key in inventory
            if (!inInventory(player, Items.BATTERED_KEY_2887)) {
                sendMessage(player, "You see a small hole in the wall but no way to open it.")
                return@on true
            }
            // Increment quest stage
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_I) < 5) {
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_I, 5)
            }
            // Allow player through the wall
            sendMessage(player, "You use the battered key to open the doors.")
            handleDoor(player, wall.asScenery())
            return@on true
        }
        setDest(IntType.SCENERY, intArrayOf(Scenery.ODD_LOOKING_WALL_26114, Scenery.ODD_LOOKING_WALL_26115), "open") { entity, node ->
            DoorHelper.getDestination(entity, node.asScenery())
        }

        on(Scenery.STAIRCASE_3415, IntType.SCENERY, "climb-down") { player, _ ->
            val stage = currentStage(player)
            // Prevent player from somehow skipping beginning quest stages
            if (stage < 1) {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }
            // Move player to downstairs location
            teleport(player, Location.create(2716, 9888, 0))
            // If it is the players first time in the workshop
            if (stage < 6) {
                sendPlayerDialogue(player,
                    "Now to explore this area thoroughly, to find what " +
                            "forgotten secrets it contains.", core.game.dialogue.FacialExpression.NEUTRAL)
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_I, 7)
            }
            return@on true
        }

        on(Scenery.STAIRCASE_3416, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(2709, 3498, 0))
            return@on true
        }


        /* * * * * * * * * * * * * * * *
         *  Workshop Area listeners   *
         * * * * * * * * * * * * * * */
        // Stone bowl box, player can get more at any time
        on(Scenery.BOXES_3397, IntType.SCENERY, "search") { player, _ ->
            // If the player doesn't have a stone bowl in their inventory
            if (player.inventory.addIfDoesntHave(emptyStoneBowl)) {
                sendMessage(player, "You find a stone bowl.")
            } else {
                sendMessage(player, "It's empty.")
            }
            return@on true
        }

        // Needle crate, player can only receive once
        on(Scenery.CRATE_3400, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, "/save:ew1:got_needle", false)) {
                setAttribute(player, "/save:ew1:got_needle", true)
                addItemOrDrop(player, Items.NEEDLE_1733)
                sendMessage(player, "You find a needle.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        // Leather crate, player can only receive once
        on(Scenery.CRATE_3394, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, "/save:ew1:got_leather", false)) {
                setAttribute(player, "/save:ew1:got_leather", true)
                addItemOrDrop(player, Items.LEATHER_1741)
                sendMessage(player, "You find some leather.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        // before completing EW1 you can only make an elemental shield from an elemental bar. Smithing the shield will complete EW1
        // during EW2 you have the option to make an elemental shield, claw, or helm from an elemental bar
        onUseWith(IntType.SCENERY, Items.ELEMENTAL_METAL_2893, Scenery.WORKBENCH_3402) { player, used, with ->
            // if EW1 is not complete, smith the shield and finish the quest. After EW2 is started, the player has a choice
            if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) < 5) {
                smithElementalItem(player, used, with, Items.ELEMENTAL_SHIELD_2890, intArrayOf(Items.SLASHED_BOOK_9715, Items.BATTERED_BOOK_2886))
            } else {
                openDialogue(player, object : DialogueLabeller() {
                    override fun addConversation() {
                        options(
                            DialogueOption("shield", "An elemental shield.", skipPlayer = true),
                            DialogueOption("claw", "An elemental claw.", skipPlayer = true),
                            DialogueOption("helm", "An elemental helm.", skipPlayer = true,
                                optionIf = { player, _ -> isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_II)}
                            ),
                            title = "What would you like to make..."
                        )

                        label("shield")
                        exec { player, _ ->
                            end()
                            smithElementalItem(player, used, with, Items.ELEMENTAL_SHIELD_2890, intArrayOf(Items.SLASHED_BOOK_9715, Items.BATTERED_BOOK_2886))
                        }

                        label("claw")
                        exec { player, _ ->
                            end()
                            smithElementalItem(player, used, with, Items.CRANE_CLAW_9720, intArrayOf(Items.CRANE_SCHEMATIC_9718))
                        }

                        label("helm")
                        exec { player, _ ->
                            end()
                            smithElementalItem(player, used, with, Items.ELEMENTAL_HELMET_9729, intArrayOf(Items.BEATEN_BOOK_9717))
                        }
                    }
                }, NPC(NPCs.JIG_CART_4913)) // pass an NPC because otherwise the exec function freaks out
            }
            return@onUseWith true
        }

        // smithing an elemental mind helm from an elemental mind bar will complete EW2
        onUseWith(IntType.SCENERY, Items.ELEMENTAL_MIND_BAR_9728, Scenery.WORKBENCH_3402) { player, used, with ->
            // if EW2 is not complete, smith the helmet and finish the quest. After the quest, the player has a choice
            if (!isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_II)) {
                smithElementalItem(player, used, with, Items.MIND_HELMET_9733, intArrayOf(Items.BEATEN_BOOK_9717))
            } else {
                openDialogue(player, object : DialogueLabeller() {
                    override fun addConversation() {
                        options(
                            DialogueOption("shield", "An elemental mind shield.", skipPlayer = true),
                            DialogueOption("helm", "An elemental mind helm.", skipPlayer = true,),
                            title = "What would you like to make..."
                        )

                        label("shield")
                        exec { player, _ ->
                            end()
                            smithElementalItem(player, used, with, Items.MIND_SHIELD_9731, intArrayOf(Items.SLASHED_BOOK_9715, Items.BATTERED_BOOK_2886))
                        }

                        label("helm")
                        exec { player, _ ->
                            end()
                            smithElementalItem(player, used, with, Items.MIND_HELMET_9733, intArrayOf(Items.BEATEN_BOOK_9717))
                        }
                    }
                }, NPC(NPCs.JIG_CART_4913)) // pass an NPC because otherwise the exec function freaks out
            }
            return@onUseWith true
        }

        /* * * * * * * * * * * * * *
         *  Fire room listeners   *
         * * * * * * * * * * * * */
        // Lava trough stone bowl (empty)
        onUseWith(IntType.SCENERY, Items.A_STONE_BOWL_2888, *lavaTrough) { player, used, trough ->
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
        onUseWith(IntType.SCENERY, fullStoneBowl.id, *lavaTrough) { player, _, _ ->
            sendMessage(player, "The bowl is already full of lava.")
            return@onUseWith true
        }

        // Pour lava into unlit furnace
        onUseWith(IntType.SCENERY, fullStoneBowl.id, Scenery.FURNACE_18525) { player, used, _ ->
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
                            setVarbit(player, FURNACE_STATE, ENABLED, true)
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
        onUseWith(IntType.SCENERY, fullStoneBowl.id, Scenery.FURNACE_18526) { player, used, _ ->
            player.faceLocation(Location.create(2726, 9875, 0))
            runTask(player) {
                playAudio(player, fillFurnaceWithLavaSFX)
                replaceSlot(player, used.asItem().slot, emptyStoneBowl)
                sendMessage(player, "The lava makes little difference to the furnace's searing heat.")
            }
            return@onUseWith true
        }

        // Use elemental ore on lit furnace
        onUseWith(IntType.SCENERY, Items.ELEMENTAL_ORE_2892, Scenery.FURNACE_18526) { player, _, _ ->
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

            val barSmeltingDuration = animationDuration(smeltElementalBar)
            lock(player, barSmeltingDuration + 2)
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
                        barSmeltingDuration -> {
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

        /* Special Interactions:
         * onUseWith FURNACE scenery (this handler)
         * onUseWith BLAST FURNACE BELT (this handler)
         * onCast SUPERHEAT spell (ModernListeners.kt)
         * SKIPPED DUE TO SPAGHETTI: on BLAST FURNACE BELT "put-ore-on" (BlastfurnaceListeners.kt)
         */
        onUseWith(IntType.SCENERY, Items.ELEMENTAL_ORE_2892, *furnaceIDS) { player, _, _ ->
            sendMessage(player, "This furnace is not hot enough to smelt elemental ore.")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.ELEMENTAL_ORE_2892, Scenery.CONVEYOR_BELT_9100) { player, _, _ ->
            sendMessage(player, "There would be no point in smelting that.")
            return@onUseWith true
        }

        /* * * * * * * * * * * * * *
         *  Water room listeners  *
         * * * * * * * * * * * * */
        // Water controls
        on(waterControls, IntType.SCENERY, "turn") { player, _ ->
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
                    log(this::class.java, Log.ERR,  "Unhandled location when determining enabled water controls! ${player.location}")
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
                            setVarbit(player, offset, enabled, true)
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
        on(Scenery.LEVER_3406, IntType.SCENERY, "pull") { player, lever ->
            // Default will happen no matter what
            lock(player, 2)
            sendMessage(player, "You pull the lever")
            replaceScenery(lever.asScenery(), Scenery.LEVER_3417, 2)

            // Check to see if the water wheel is running
            if (EWUtils.waterWheelEnabled(player)) {
                playAudio(player, pullLeverDisabledSFX)
                sendMessage(player, "You hear the sound of a water wheel coming to a standstill.")
                setVarbit(player, WATER_WHEEL_STATE, DISABLED, true)
                setVarbit(player, BELLOWS_STATE, DISABLED, true)
                return@on true
            }

            // Check to reset both valves to the "off" position if both aren't green
            if (!EWUtils.leftWaterControlEnabled(player) || !EWUtils.rightWaterControlEnabled(player)) {
                playAudio(player, pullLeverResetGatesSFX)
                sendMessage(player, "You hear the sound of the flow gates resetting.")
                setVarbit(player, LEFT_WATER_CONTROL_STATE, DISABLED, true)
                setVarbit(player, RIGHT_WATER_CONTROL_STATE, DISABLED, true)
                return@on true
            }

            // If both of the above are false, the water wheel can start running
            playAudio(player, pullLeverEnabledSFX)
            sendMessage(player, "You hear the sound of a water wheel starting up.")
            setVarbit(player, WATER_WHEEL_STATE, ENABLED, true)
            return@on true
        }


        /* * * * * * * * * * * * * *
         *  Air room listeners    *
         * * * * * * * * * * * * */
        on(Scenery.BELLOWS_18516, IntType.SCENERY, "fix") { player, bellows ->
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
            bellowFixReqItems.forEach { reqItem ->
                if (!player.inventory.contains(reqItem, 1)) {
                    val missingItemMsg = when (reqItem) {
                        Items.NEEDLE_1733 -> "a needle"
                        Items.THREAD_1734 -> "some thread"
                        Items.LEATHER_1741 -> "a piece of leather"
                        else -> "NULL" // Should never be called; no sense in making an enum
                    }
                    sendMessage(player, "You need $missingItemMsg to fix the bellows.")
                    return@on true
                }
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

        on(Scenery.LEVER_3409, IntType.SCENERY, "pull") { player, lever ->
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
                setVarbit(player, BELLOWS_STATE, DISABLED, true)
                return@on true
            }

            // Bellows lever "ON" state
            sendMessage(player, "The bellows pump air down the pipe.")
            setVarbit(player, BELLOWS_STATE, ENABLED, true)
            return@on true
        }
    }

    // smiths an elemental item
    fun smithElementalItem(player: Player, used: Node, with: Node, item: Int, book: IntArray) {
        val smithLvl = if (item == Items.MIND_HELMET_9733 || item == Items.MIND_SHIELD_9731) 30 else 20
        val smithXp = if (item == Items.MIND_HELMET_9733 || item == Items.MIND_SHIELD_9731) 30.0 else 20.0

        // check hammer
        if (inInventory(player, Items.HAMMER_2347)) {
            // check level
            if (getDynLevel(player, Skills.SMITHING) >= smithLvl) {
                // check book
                if (anyInInventory(player, *book)) {
                    // authentically the bar does get removed, then the item is added after the anim is played
                    if (removeItem(player, used)) {
                        lock(player, 4)
                        queueScript(player, 0, QueueStrength.SOFT) { stage ->
                            when (stage) {
                                // smith
                                0 -> {
                                    face(player, with)
                                    animate(player, smithElementalShield)
                                    playAudio(player, smithElementalShieldSFX)
                                    return@queueScript delayScript(player, Animation(smithElementalShieldSFX).duration)
                                }

                                // reward
                                1 -> {
                                    addItemOrDrop(player, item)
                                    rewardXP(player, Skills.SMITHING, smithXp)
                                    when (item) {
                                        Items.ELEMENTAL_SHIELD_2890 -> sendMessage(player, "Following the instructions in the book you make an elemental shield.")
                                        Items.ELEMENTAL_HELMET_9729 -> sendMessage(player, "Following the instructions in the book you make an elemental helm.")
                                        Items.MIND_SHIELD_9731 -> sendMessage(player, "Following the instructions in the book you make an elemental mind shield.")
                                        Items.MIND_HELMET_9733 -> sendMessage(player, "Following the instructions in the book you make an elemental mind helm.")
                                        Items.CRANE_CLAW_9720 -> sendMessage(player, "Following the instructions from the schematic you make an elemental claw.")
                                    }
                                    return@queueScript delayScript(player, 1)
                                }

                                // finish quest when needed
                                2 -> {
                                    when (item) {
                                        Items.ELEMENTAL_SHIELD_2890 -> if (!isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_I)) finishQuest(player, Quests.ELEMENTAL_WORKSHOP_I)
                                        Items.MIND_HELMET_9733 -> if (!isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_II)) finishQuest(player, Quests.ELEMENTAL_WORKSHOP_II)
                                    }
                                    return@queueScript stopExecuting(player)
                                }
                            }
                            return@queueScript stopExecuting(player)
                        }
                    }
                } else sendDialogue(player, "This workbench is too complicated. You need instructions to follow.")
            } else sendMessage(player, "You need a Smithing level of $smithLvl to create ${prependArticle(item.asItem().name.lowercase())}.")
        } else sendDialogue(player, "You don't have a tool available to shape the metal.")
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.FURNACE_18525, Scenery.FURNACE_18526), "use") { _, _ ->
            return@setDest Location.create(2724,9875)
        }
        setDest(IntType.SCENERY, intArrayOf(Scenery.BELLOWS_18516), "fix") { _, _ ->
            return@setDest Location.create(2733, 9884, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(Scenery.ODD_LOOKING_WALL_26115), "open") { player, _ ->
            if (inBorders(player, 2709, 3496, 2711, 3498)) {
                return@setDest Location.create(2709, 3496, 0)
            } else {
                return@setDest Location.create(2709, 3495, 0)
            }
        }
        setDest(IntType.SCENERY, intArrayOf(Scenery.ODD_LOOKING_WALL_26114), "open") { player, _ ->
            if (inBorders(player, 2709, 3496, 2711, 3498)) {
                return@setDest Location.create(2710, 3496, 0)
            } else {
                return@setDest Location.create(2710, 3495, 0)
            }
        }
    }
}
