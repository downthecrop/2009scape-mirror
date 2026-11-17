package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.skill.farming.FarmingPatch
import content.global.skill.farming.PatchRaker
import content.global.skill.farming.PatchType
import content.global.skill.farming.Plantable
import content.global.skill.farming.UseWithPatchHandler
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC as Npc
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import core.tools.colorize
import core.tools.minutesToTicks
import org.rs09.consts.Animations
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds
import kotlin.collections.toIntArray

/**
 * Garden of Tranquility quest listeners.
 */

class GardenOfTranquilityListeners : InteractionListener {

    private val plantCureAnim = getAnimation(Animations.HUMAN_PLANT_CURE_2288)
    private val pushStatue = getAnimation(Animations.HUMAN_GOT_PUSH_2572)

    private val seedNodes = intArrayOf(
        Items.WHITE_ROSE_SEED_6453,
        Items.RED_ROSE_SEED_6454,
        Items.PINK_ROSE_SEED_6455,
        Items.VINE_SEED_6456,
        Items.DELPHINIUM_SEED_6457,
        Items.ORCHID_SEED_6458,
        Items.ORCHID_SEED_6459,
        Items.SNOWDROP_SEED_6460,
        Items.WHITE_TREE_SHOOT_6461,
        Items.WHITE_TREE_SHOOT_6462,
        Items.WHITE_TREE_SHOOT_6463,
        Items.WHITE_TREE_SAPLING_6464
    )

    companion object {
        val questPatches = arrayOf(
            PatchType.WHITE_TREE_PATCH,
            PatchType.DELPHINIUM_PATCH,
            PatchType.SNOWDROP_PATCH,
            PatchType.PINK_ORCHID_PATCH,
            PatchType.YELLOW_ORCHID_PATCH,
            PatchType.PINK_ROSE_PATCH,
            PatchType.WHITE_ROSE_PATCH,
            PatchType.RED_ROSE_PATCH,
            PatchType.VINE_PATCH,
        )

        fun questPatchInspect(player: Player, patchType: PatchType) {
            if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 0) {
                sendDialogue(player, "This patch looks like it's the property of Varrock palace. Maybe I should go and speak to Queen Ellamaria about it.")
                return
            }

            val plsGrow = when (patchType) {
                PatchType.WHITE_TREE_PATCH -> "a White Tree"
                PatchType.DELPHINIUM_PATCH -> "delphiniums"
                PatchType.SNOWDROP_PATCH -> "snowdrops"
                PatchType.PINK_ORCHID_PATCH -> "pink orchids"
                PatchType.YELLOW_ORCHID_PATCH -> "yellow orchids"
                PatchType.PINK_ROSE_PATCH -> "pink roses"
                PatchType.WHITE_ROSE_PATCH -> "white roses"
                PatchType.RED_ROSE_PATCH -> "red roses"
                else -> "Burthorpe Vines"
            }

            sendNPCDialogue(player, NPCs.ELLAMARIA_2581, "I want you to grow $plsGrow there.")
        }
    }

    override fun defineListeners() {

        // Using items on Ellamaria
        onUseAnyWith(IntType.NPC, NPCs.ELLAMARIA_2581) { player, used, with ->
            player.faceLocation(with.location)
            val isQuestSeed = seedNodes.contains(used.id)
            when (isQuestInProgress(player, Quests.GARDEN_OF_TRANQUILITY, 10, 90)) {
                true -> when (isQuestSeed) {
                    true -> sendNPCDialogue(
                        player,
                        with.id,
                        "Very good peasant, you've got the seed; but its the plant that one wishes, not seeds. Plant it in the patch, and let it grow."
                    )

                    false -> sendNPCDialogue(
                        player,
                        with.id,
                        "Very good peasant, thank you for showing one that, but one has given you a job to do. Do it."
                    )
                }

                else -> return@onUseAnyWith true
            }
            return@onUseAnyWith true
        }

        // The list of things to plant
        on(Items.LIST_6479, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.GARDEN_LIST_132)
            setInterfaceText(player, listCross(player, "Delphiniums (see Elstan near Falador)", GardenOfTranquility.VB_DELPHINIUM_PATCH), Components.GARDEN_LIST_132, 1)
            setInterfaceText(player, listCross(player, "Pink orchids (see Lyra near Port Phasmatys)", GardenOfTranquility.VB_PINK_ORCHID_PATCH), Components.GARDEN_LIST_132, 2)
            setInterfaceText(player, listCross(player, "Yellow orchids (see Lyra near Port Phasmatys)", GardenOfTranquility.VB_YELLOW_ORCHID_PATCH), Components.GARDEN_LIST_132, 3)
            setInterfaceText(player, listCross(player, "Snowdrops (see Kragen near Ardougne)", GardenOfTranquility.VB_SNOWDROP_PATCH), Components.GARDEN_LIST_132, 4)
            setInterfaceText(player, listCross(player, "Burthorpe Vine (see Bernald in Burthorpe)", GardenOfTranquility.VB_VINE_PATCH), Components.GARDEN_LIST_132, 5)
            setInterfaceText(player, listCross(player, "White rosebush (go to Edgeville Monastery)", GardenOfTranquility.VB_WHITE_ROSE_PATCH), Components.GARDEN_LIST_132, 6)
            setInterfaceText(player, listCross(player, "Pink rosebush (go to Edgeville Monastery)", GardenOfTranquility.VB_PINK_ROSE_PATCH), Components.GARDEN_LIST_132, 7)
            setInterfaceText(player, listCross(player, "Red rosebush (go to Edgeville Monastery)", GardenOfTranquility.VB_RED_ROSE_PATCH), Components.GARDEN_LIST_132, 8)
            setInterfaceText(player, listCross(player, "A White Tree (see Dantaera in Catherby)", GardenOfTranquility.VB_WHITE_TREE_PATCH), Components.GARDEN_LIST_132, 9)
            setInterfaceText(player, listCross(player, "A statue of Saradomin from Falador.", GardenOfTranquility.VB_SARA_STATUE), Components.GARDEN_LIST_132, 10)
            setInterfaceText(player, listCross(player, "A statue of a king from outside Lumbridge Castle.", GardenOfTranquility.VB_KING_STATUE), Components.GARDEN_LIST_132, 11)
            // these "" actually need to be here, the children default to saying >>> in red
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 13)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 16)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 19)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 22)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 25)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 28)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 31)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 34)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 37)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 40)
            setInterfaceText(player, "", Components.GARDEN_LIST_132, 43)
            return@on true
        }

        // For Burthorpe Vines
        onUseWith(IntType.SCENERY, intArrayOf(Items.PLANT_CURE_6036, Items.PLANT_CURE_6468), Scenery.DISEASED_GRAPEVINES_9256) { player, used, with ->
            val substage = getVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES)
            player.faceLocation(with.location)
            when (used.id) {
                // regular plant cure
                Items.PLANT_CURE_6036 -> {
                    if (substage == 1) {
                        sendMessage(player, "You treat the diseased grapevines with plant cure...")
                        queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    animate(player, plantCureAnim)
                                    playAudio(player, Sounds.FARMING_PLANTCURE_2438)
                                    return@queueScript delayScript(player, plantCureAnim.duration / 2)
                                }

                                1 -> {
                                    if (removeItem(player, used.asItem())) {
                                        addItem(player, Items.VIAL_229)
                                        setVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES, 2, true)
                                        sendMessage(player, "... but it doesn't seem to make any difference.")
                                    }
                                    return@queueScript stopExecuting(player)
                                }

                                else -> return@queueScript stopExecuting(player)
                            }
                        }
                    } else {
                        sendMessage(player, "I have no reason to do this right now.")
                    }
                }

                // strong plant cure
                Items.PLANT_CURE_6468 -> {
                    if (substage == 3) {
                        sendMessage(player, "You treat the diseased grapevines with strong plant cure...")
                        queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    animate(player, plantCureAnim)
                                    playAudio(player, Sounds.FARMING_PLANTCURE_2438)
                                    return@queueScript delayScript(player, plantCureAnim.duration / 2)
                                }

                                1 -> {
                                    if (removeItem(player, used.asItem())) {
                                        addItem(player, Items.VIAL_229)
                                        setVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES, 4, true)
                                        sendMessage(player, "... and they blossom with new found life!")
                                    }

                                    return@queueScript stopExecuting(player)
                                }

                                else -> return@queueScript stopExecuting(player)
                            }
                        }
                    } else {
                        sendMessage(player, "I have no reason to do this right now.")
                    }
                }
            }
            return@onUseWith true
        }

        /* Note from OSRS wiki:
         * "The player can create rune shards and rune dust without starting Garden of Tranquillity, but nothing
         * interesting happens when attempting to put rune dust into plant cure to create strengthened plant cure."
         */

        // For making rune essence dust, see GrindItemPlugin and GrindItem

        // For making rune essence shards
        val tools = intArrayOf(Items.HAMMER_2347, Items.CHISEL_1755)
        onUseWith(IntType.ITEM, intArrayOf(Items.RUNE_ESSENCE_1436, Items.PURE_ESSENCE_7936), *tools) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                addItem(player, Items.RUNE_SHARDS_6466)
                sendMessage(player, "You break up the rune essence into shards.")
            }
            return@onUseWith true
        }

        // For making quest plant cure
        onUseWith(IntType.ITEM, Items.RUNE_DUST_6467, Items.PLANT_CURE_6036) { player, used, with ->
            // has received the recipe from Alain
            val hasRecipe = getVarbit(player, GardenOfTranquility.VB_ALAIN) == 1
            if (hasRecipe) {
                queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, Animations.HUMAN_MIX_POTION_363)
                            playAudio(player, Sounds.FARMING_PLANTCURE_2438)
                            return@queueScript delayScript(player, animationDuration(getAnimation(Animations.HUMAN_MIX_POTION_363)) / 2)
                        }

                        1 -> {
                            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                                addItem(player, Items.PLANT_CURE_6468)
                                sendMessage(player, "You mix the rune dust into the plant cure.")
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendMessage(player, "I have no reason to do this right now.")
            }
            return@onUseWith true
        }

        // For cutting from the White Tree
        onUseWith(IntType.SCENERY, intArrayOf(Items.SECATEURS_5329, Items.MAGIC_SECATEURS_7409), Scenery.WHITE_TREE_9263) { player, used, with ->
            val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
            val substage = getVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE)

            // if the player has talked to Dantaera, agreed to take a cutting, and does not have a cutting aready
            face(player, with)
            if ((substage == 1 || substage == 2)
                && stage <= 40
                && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6461).exists()
                && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6462).exists()
                && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6463).exists()
                && !hasAnItem(player, Items.WHITE_TREE_SAPLING_6464).exists()
            ) {
                when (substage) {
                    // (1) when the player hasn't taken a cutting yet
                    1 -> {
                        openDialogue(player, object : DialogueLabeller() {
                            override fun addConversation() {
                                player("Here is the shoot that Dantaera spoke of. Everything she told me is true - this tree will die once I cut this shoot. Well, here goes...")
                            }
                        })
                    }

                    // (2) if the player has taken a cutting already
                    else -> {
                        openDialogue(player, object : DialogueLabeller() {
                            override fun addConversation() {
                                player("Ah, luckily there is another shoot here that I can cut. Better not lose this one...")
                            }
                        })
                    }
                }

                // get the anim
                when (used.id) {
                    Items.SECATEURS_5329 -> animate(player, Animations.HUMAN_SECATEURS_CURE_3X_2277)
                    Items.MAGIC_SECATEURS_7409 -> animate(player, Animations.HUMAN_MAGIC_SECATEURS_CURE_3X_3340)
                }

                // add the item
                addItemOrDrop(player, Items.WHITE_TREE_SHOOT_6461)

                // set varbit that the player has taken a cutting
                setVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE, 2, true)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // For throwing Ring of Charos down well
        onUseWith(IntType.SCENERY, Items.RING_OF_CHAROSA_6465, Scenery.WELL_26945) { player, used, with ->

            // varbit is 1 when Brother Althric has prompted the player to destroy/throw away the ring
            val substage = getVarbit(player, GardenOfTranquility.VB_BRO_ALTHRIC)

            face(player, with)
            if (substage == 1) {
                queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "You throw the Ring of Charos down the well.")
                            animate(player, Animations.HUMAN_GOT_DROP_RING_2577)
                            return@queueScript delayScript(player, 2)
                        }

                        1 -> {
                            if (removeItem(player, used)) {
                                sendMessage(player, "You hear a splash from below.")
                                setVarbit(player, GardenOfTranquility.VB_RING_WELL, 1, true)
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // For fishing the Ring of Charos back up the Edgeville Well
        onUseWith(IntType.SCENERY, intArrayOf(Items.FISHING_ROD_307, Items.FLY_FISHING_ROD_309), Scenery.WELL_26945) { player, _, with ->
            val inWell = getVarbit(player, GardenOfTranquility.VB_RING_WELL) == 1
            player.faceLocation(with.location)
            if (inWell) {
                queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendMessage(player, "You cast out your line into the well.")
                            animate(player, Animations.HUMAN_GOT_CAST_FISH_ROD_2575)
                            return@queueScript delayScript(player, 3)
                        }

                        1 -> {
                            sendMessage(player, "The hook catches onto something...")
                            return@queueScript delayScript(player, 3)
                        }

                        2 -> {
                            sendChat(player, "la-de-da")
                            return@queueScript delayScript(player, 3)
                        }

                        3 -> {
                            sendChat(player, "dum-de-dum")
                            return@queueScript delayScript(player, 3)
                        }

                        4 -> {
                            // inauthentic chances, but means that multiple tries are likely
                            if (RandomFunction.random(100) >= 60) {
                                sendItemDialogue(player, Items.RING_OF_CHAROSA_6465, "...and you retrieve the Ring of Charos.")
                                addItemOrDrop(player, Items.RING_OF_CHAROSA_6465)
                                setVarbit(player, GardenOfTranquility.VB_RING_WELL, 0, true)
                            } else {
                                sendMessage(player, "But it drops back into the water with a splash.")
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        // picking rose seeds
        on(intArrayOf(Scenery.ROSES_30806, Scenery.ROSES_9261, Scenery.ROSES_9262), SCENERY, "Take-seed") { player, node ->
            // select the item to give
            val roseType: Int = when (node.id) {
                Scenery.ROSES_30806 -> Items.RED_ROSE_SEED_6454
                Scenery.ROSES_9261 -> Items.PINK_ROSE_SEED_6455
                else -> Items.WHITE_ROSE_SEED_6453
            }

            // select the varbit to check
            val roseBit: Int = when (node.id) {
                Scenery.ROSES_30806 -> GardenOfTranquility.VB_RED_ROSE_PATCH
                Scenery.ROSES_9261 -> GardenOfTranquility.VB_PINK_ROSE_PATCH
                else -> GardenOfTranquility.VB_WHITE_ROSE_PATCH
            }

            // select the string to say
            val roseColor: String = when (node.id) {
                Scenery.ROSES_30806 -> "red"
                Scenery.ROSES_9261 -> "pink"
                else -> "white"
            }

            // varbit is 1 after you've first talked to brother althric, and 2 after you've gotten rid of the ring of charos
            when (getVarbit(player, GardenOfTranquility.VB_BRO_ALTHRIC)) {
                2 -> {
                    if (!hasAnItem(player, roseType).exists() // can't have seeds already
                        && getVarbit(player, roseBit) <= 3 // patch is not planted yet
                        && getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) <= 40 // stage of quest where you plant shit
                    ) {
                        sendItemDialogue(player, roseType, "You take some seeds from the $roseColor rosebush.")
                        addItemOrDrop(player, roseType, 4)
                    } else {
                        sendMessage(player, "Nothing interesting happens.") // TODO: Need authentic text.
                    }
                }

                else -> openDialogue(player, BrotherAlthricDialogueFile(), Npc(NPCs.BROTHER_ALTHRIC_2588))
            }
            return@on true
        }

        // Stealing the statues.
        // Note: There is an authentic RS3 softlock where if a player pushes the Lumbridge statue to Varrock, then goes
        //       to Falador to steal that statue, the Lumbridge statue will not immediately despawn, not reset properly,
        //       and the player is locked out of stealing the Lumbridge statue again. I have not implemented this bug,
        //       but someone can if they would like.
        onUseWith(IntType.SCENERY, Items.TROLLEY_6478, Scenery.STATUE_OF_SARADOMIN_9243, Scenery.STATUE_36751) { player, used, with ->
            if (getVarbit(player, GardenOfTranquility.VB_GUARD_CUTSCENE) == 0
                && with.id == Scenery.STATUE_OF_SARADOMIN_9243
            ) {
                SaraStatueCutscene(player).start()
            } else {
                var statueID = 0
                var varbit = 0
                var direction = Direction.SOUTH

                val placeTrollyLoc = with.location.transform(1, 1, 0)
                val trollyLoc = with.location.transform(0, 1, 0)
                val pushLoc = with.location.transform(0, -1, 0)

                when (with.id) {
                    // Falador statue of Saradomin
                    Scenery.STATUE_OF_SARADOMIN_9243 -> {
                        varbit = GardenOfTranquility.VB_SARA_STATUE
                        statueID = NPCs.TROLLEY_2582
                    }

                    // Lumbridge statue of a king
                    Scenery.STATUE_36751 -> {
                        varbit = GardenOfTranquility.VB_KING_STATUE
                        statueID = NPCs.TROLLEY_2583
                        direction = Direction.NORTH
                    }
                }

                val trolley = Npc.create(NPCs.TROLLEY_2584, trollyLoc)
                val statue = Npc.create(statueID, trollyLoc, direction)

                queueScript(player, 0, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            forceWalk(player, placeTrollyLoc, "smart")
                            return@queueScript delayScript(player, 3)
                        }

                        1 -> {
                            face(player, trollyLoc)
                            return@queueScript delayScript(player, 1)
                        }

                        2 -> {
                            if (removeItem(player, used)) {
                                animate(player, Animations.HUMAN_BURYING_BONES_827)
                                trolley.init()
                            } else {
                                return@queueScript stopExecuting(player)
                            }
                            return@queueScript delayScript(player, animationDuration(getAnimation(Animations.HUMAN_BURYING_BONES_827)))
                        }

                        3 -> {
                            forceWalk(player, pushLoc, "smart")
                            return@queueScript delayScript(player, 3)
                        }

                        4 -> {
                            face(player, with.location)
                            return@queueScript delayScript(player, 1)
                        }

                        5 -> {
                            animate(player, pushStatue)
                            return@queueScript delayScript(player, animationDuration(pushStatue))
                        }

                        6 -> {
                            if (varbit != 0) {
                                setVarbit(player, varbit, 1)
                                registerLogoutListener(player, GardenOfTranquility.STATUE_LOGOUT) { player ->
                                    setVarbit(player, varbit, 0)
                                }
                            }
                            if (statueID != 0) {
                                statue.init()
                                setAttribute(statue, GardenOfTranquility.ATTR_STATUE_OWNER, player.name)
                                registerHintIcon(player, statue)
                                trolley.clear()
                            }
                            return@queueScript stopExecuting(player)
                        }
                    }
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        // Pushing a trolley one square
        on(intArrayOf(NPCs.TROLLEY_2582, NPCs.TROLLEY_2583), IntType.NPC, "Push") { player, node ->
            face(player, node)
            val statue = node as Entity
            push(player, statue)
            return@on true
        }

        // Pulling a trolley one square
        on(intArrayOf(NPCs.TROLLEY_2582, NPCs.TROLLEY_2583), IntType.NPC, "Pull") { player, node ->
            face(player, node)
            val statue = node as Entity
            pull(player, statue)
            return@on true
        }

        // Pushing a trolley multiple squares. Per video sources, it looks like it moves 4 or 5 squares, so I made this just a simple 50/50.
        on(intArrayOf(NPCs.TROLLEY_2582, NPCs.TROLLEY_2583), IntType.NPC, "Big-push") { player, node ->
            face(player, node)
            val statue = node as Entity
            bigPush(player, statue)
            return@on true
        }

        // Placing the actual statue.
        on(intArrayOf(NPCs.TROLLEY_2582, NPCs.TROLLEY_2583), IntType.NPC, "Place") { player, node ->
            face(player, node)
            val statue = node as Entity
            place(player, statue)
            return@on true
        }

        // Inspecting the plinths.
        on(intArrayOf(Scenery.STATUE_PLINTH_9246, Scenery.STATUE_PLINTH_9248), SCENERY, "Inspect") { player, node ->
            if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 0) {
                sendDialogue(player, "This plinth looks like it's the property of Varrock palace. Maybe I should go and speak to Queen Ellamaria about it.")
                return@on true
            }
            val statueType = when (node.id) {
                Scenery.STATUE_PLINTH_9248 -> "a king"
                else -> "Saradomin"
            }
            sendNPCDialogue(player, NPCs.ELLAMARIA_2581, "I want you to place a statue of $statueType there.", FacialExpression.NEUTRAL)
            return@on true
        }

        // Quest patch interactions
        onUseWith(IntType.SCENERY, UseWithPatchHandler.allowedNodes.toIntArray(), *FarmingPatch.tranquilityPatchNodes.toIntArray()) { player, used, with ->
            val patch = FarmingPatch.forObject(with.asScenery()) ?: return@onUseWith false
            val usedItem = used.asItem()
            val plantable = Plantable.forItemID(usedItem.id)

            if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 0) {
                sendDialogue(player, "This patch looks like it's the property of Varrock palace. Maybe I should go and speak to Queen Ellamaria about it.")
                return@onUseWith true
            }

            // rake (you can't rake the patches before the quest, and can't rake the orchid plantpots)
            if (usedItem.id == Items.RAKE_5341) {
                if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 0) {
                    return@onUseWith true
                }
                if (patch.type == PatchType.YELLOW_ORCHID_PATCH || patch.type == PatchType.PINK_ORCHID_PATCH) {
                    val patchName = patch.type.displayName()
                    sendMessage(player, "This $patchName doesn't need weeding right now.")
                    return@onUseWith true
                }
                PatchRaker.rake(player, patch)
                return@onUseWith true
            }

            // spade (you can't dig the quest patches up)
            if (usedItem.id == Items.SPADE_952) {
                openDialogue(player, QueenEllamariaDontTouchMyFuckingPlantsDialogueFile(), Npc(NPCs.ELLAMARIA_2581))
                return@onUseWith true
            }

            // compost (you can't compost the patches, and the orchid pots get filled with compost)
            if (usedItem.id == Items.SUPERCOMPOST_6034 || usedItem.id == Items.COMPOST_6032) {
                val p = patch.getPatchFor(player)
                if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) > 0
                    && (patch.type == PatchType.PINK_ORCHID_PATCH || patch.type == PatchType.YELLOW_ORCHID_PATCH)
                    && p.getCurrentState() == 0
                ) {
                    p.setCurrentState(1)
                    UseWithPatchHandler.compostPatch(player, patch, usedItem, true)
                } else {
                    sendDialogue(player, "This patch doesn't really need treating with compost - the soil seems to be extremely fertile.")
                    return@onUseWith true
                }
                return@onUseWith true
            }

            // watering can
            if (usedItem.id in intArrayOf(Items.WATERING_CAN_5331,Items.WATERING_CAN1_5333,Items.WATERING_CAN2_5334,Items.WATERING_CAN3_5335,Items.WATERING_CAN4_5336,Items.WATERING_CAN5_5337,Items.WATERING_CAN6_5338,Items.WATERING_CAN7_5339,Items.WATERING_CAN8_5340)) {
                sendDialogue(player, "These don't really need watering - the soil seems to be extremely fertile.")
                return@onUseWith true
            }

            // planting
            if (plantable != null) {
                val p = patch.getPatchFor(player)

                // the orchids have a custom "wrong patch" refusal message and authentically it procs before checking if the plant has been composted
                if (plantable.applicablePatch != patch.type && GardenOfTranquility.isPlantPot(patch.type)) {
                    sendMessage(player, "These seeds need to be planted in the other plantpot.")
                    return@onUseWith true
                }

                // the orchid pots need to be filled with compost before you can plant them
                if (p.getCurrentState() == 0 && (plantable == Plantable.PINK_ORCHID_SEED || plantable == Plantable.YELLOW_ORCHID_SEED)) {
                    sendMessage(player, "This plantpot needs some compost in it first.")
                    return@onUseWith true
                }

                val amount = when (patch.type) {
                    PatchType.YELLOW_ORCHID_PATCH, PatchType.PINK_ORCHID_PATCH -> 3
                    PatchType.DELPHINIUM_PATCH, PatchType.SNOWDROP_PATCH, PatchType.VINE_PATCH, PatchType.RED_ROSE_PATCH, PatchType.WHITE_ROSE_PATCH, PatchType.PINK_ROSE_PATCH -> 4
                    else -> 1
                }
                val plantItem = Item(plantable.itemID, amount)
                UseWithPatchHandler.plantPatch(player, patch, plantable, plantItem)
                return@onUseWith true
            }
            return@onUseWith false
        }
    }

    fun push(player: Player, statue: Entity) {
        if (getAttribute(statue, GardenOfTranquility.ATTR_STATUE_OWNER, null) == player.name) {
            val xStep = -(player.location.x - statue.location.x)
            val yStep = -(player.location.y - statue.location.y)
            val newLoc = statue.location.transform(xStep, yStep, 0)
            if (!checkBounds(player, statue, newLoc)) return
            animate(player, pushStatue)
            unlock(statue)
            forceWalk(statue, newLoc, "dumb")
            lock(statue, minutesToTicks(5))
        } else sendMessage(player, "That's somebody else's statue!")
    }

    fun pull(player: Player, statue: Entity) {
        if (getAttribute(statue, GardenOfTranquility.ATTR_STATUE_OWNER, null) == player.name) {
            val xStep = -(player.location.x - statue.location.x)
            val yStep = -(player.location.y - statue.location.y)
            val newLoc = statue.location.transform(-xStep, -yStep, 0)
            if (!checkBounds(player, statue, newLoc)) return
            unlock(statue)
            forceWalk(statue, newLoc, "dumb")
            lock(statue, minutesToTicks(5))
        } else sendMessage(player, "That's somebody else's statue!")
    }

    fun bigPush(player: Player, statue: Entity) {
        if (getAttribute(statue, GardenOfTranquility.ATTR_STATUE_OWNER, null) == player.name) {
            val xStep = -(player.location.x - statue.location.x)
            val yStep = -(player.location.y - statue.location.y)
            val power = (3..4).random()
            val newLoc = statue.location.transform(xStep * power, yStep * power, 0)
            if (!checkBounds(player, statue, newLoc)) return
            animate(player, pushStatue)
            unlock(statue)
            forceWalk(statue, newLoc, "dumb")
            lock(statue, minutesToTicks(5))
        } else sendMessage(player, "That's somebody else's statue!")
    }

    // Checks if the statue is being pushed in the correct direction.
    // Return false if the statue would go out of bounds.
    // Credit to DeadlyGenga for confirming the authentic bounds on RS3.
    fun checkBounds(player: Player, statue: Entity, newLoc: Location): Boolean {
        // if the statue is in Varrock
        if (statue.location.y > 3434) {
            if (newLoc.x < 3202 || newLoc.y < 3471) {
                teleBackToStart(player, statue)
            }
            return true
        } else {
            when (statue.id) {
                // Saradomin (Falador)
                NPCs.TROLLEY_2582 -> {
                    if (newLoc.x !in 2959..2971 || newLoc.y < 3366) {
                        sendPlayerDialogue(player, "I don't think that's the quickest way to Varrock - perhaps I should push the trolley out of the northern gates.")
                        return false
                    } else return true
                }

                // King (Lumbridge) (NPCs.TROLLEY_2583)
                else -> {
                    if (newLoc.y !in 3201..3236) {
                        sendPlayerDialogue(player, "I don't think that's the quickest way to Varrock - perhaps I should push the trolley out over the bridge.")
                        return false
                    } else return true
                }
            }
        }
    }

    // It has been authentically confirmed in RS3 that if you push the statue backwards trying to go back to Lumbridge/Falador,
    // you'll just get teleported back to the Varrock statue tele point.
    fun teleBackToStart(player: Player, statue: Entity) {
        queueScript(player, 0, QueueStrength.SOFT) { counter ->
            when (counter) {
                0 -> {
                    openOverlay(player, Components.FADE_TO_BLACK_120)
                    setInterfaceText(player, "Much puffing and panting later...", Components.FADE_TO_BLACK_120, 1)
                    return@queueScript delayScript(player, 4)
                }

                1 -> {
                    teleport(player, location(3213, 3500, 0))
                    teleport(statue, location(3214, 3500, 0))
                    return@queueScript delayScript(player, 4)
                }

                2 -> {
                    openOverlay(player, Components.FADE_FROM_BLACK_170)
                    return@queueScript delayScript(player, 4)
                }

                3 -> {
                    closeOverlay(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    fun place(player: Player, statue: Entity) {
        val zone = when (statue.id) {
            NPCs.TROLLEY_2582 -> ZoneBorders(3228, 3478, 3231, 3480) // near Saradomin plinth
            NPCs.TROLLEY_2583 -> ZoneBorders(3232, 3486, 3233, 3488) // near King plinth
            else -> null
        }
        if (getAttribute(statue, GardenOfTranquility.ATTR_STATUE_OWNER, null) == player.name) {
            if (inBorders(statue, zone!!)) {
                animate(player, pushStatue)
                val trolley = Npc(NPCs.TROLLEY_2584, statue.location)
                statue.clear()
                trolley.init()

                when (statue.id) {
                    NPCs.TROLLEY_2582 -> {
                        setVarbit(player, GardenOfTranquility.VB_SARA_STATUE, 2, true)
                    }

                    NPCs.TROLLEY_2583 -> {
                        setVarbit(player, GardenOfTranquility.VB_KING_STATUE, 2, true)
                    }
                }
                queueScript(player, pushStatue.duration, QueueStrength.STRONG) {
                    addItemOrDrop(player, Items.TROLLEY_6478)
                    trolley.clear()
                    animate(player, Animations.HUMAN_BURYING_BONES_827)
                    clearLogoutListener(player, GardenOfTranquility.STATUE_LOGOUT)
                    return@queueScript true
                }
            }
        } else sendMessage(player, "That's somebody else's statue!")
    }

    // returns a crossed-out line in the text if a player has planted the item in the garden
    fun listCross(player: Player, message: String, varbit: Int): String {
        val gardenState = getVarbit(player, varbit)
        if (varbit == GardenOfTranquility.VB_SARA_STATUE
            || varbit == GardenOfTranquility.VB_KING_STATUE
        ) {
            val crossed = gardenState >= 2 // the statues are placed in garden
            return if (crossed) colorize("<str>$message</str>") else colorize("%B$message")
        } else {
            val crossed = gardenState >= 4 // the plants are planted in the garden beds
            return if (crossed) colorize("<str>$message</str>") else colorize("%B$message")
        }
    }
}
