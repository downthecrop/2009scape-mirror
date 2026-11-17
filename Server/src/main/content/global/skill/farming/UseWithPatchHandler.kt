package content.global.skill.farming

import core.api.*
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs09.consts.Items
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.tools.StringUtils
import core.tools.prependArticle
import org.rs09.consts.Sounds
import content.data.Quests
import content.region.misthalin.varrock.quest.gardenoftranquility.GardenOfTranquility
import core.game.node.entity.player.Player
import org.rs09.consts.Animations

class UseWithPatchHandler : InteractionListener {

    companion object {

        val seedDibberAnim = getAnimation(2291)
        val wateringCanAnim = getAnimation(2293)

        var treePatches = arrayListOf(PatchType.TREE_PATCH, PatchType.FRUIT_TREE_PATCH, PatchType.SPIRIT_TREE_PATCH, PatchType.WHITE_TREE_PATCH, PatchType.CALQUAT_TREE_PATCH)

        @JvmField
        val allowedNodes = ArrayList<Int>()

        init {
            for (p in Plantable.values()) {
                allowedNodes.add(p.itemID)
            }
            allowedNodes.addAll(
                arrayListOf(
                    Items.RAKE_5341,
                    Items.SEED_DIBBER_5343,
                    Items.SPADE_952,
                    Items.SECATEURS_5329,
                    Items.MAGIC_SECATEURS_7409,
                    Items.GARDENING_TROWEL_5325,
                    Items.SUPERCOMPOST_6034,
                    Items.COMPOST_6032,
                    Items.PLANT_CURE_6036,
                    Items.WATERING_CAN_5331,
                    Items.WATERING_CAN1_5333,
                    Items.WATERING_CAN2_5334,
                    Items.WATERING_CAN3_5335,
                    Items.WATERING_CAN4_5336,
                    Items.WATERING_CAN5_5337,
                    Items.WATERING_CAN6_5338,
                    Items.WATERING_CAN7_5339,
                    Items.WATERING_CAN8_5340,
                    Items.PLANT_POT_5350
                )
            )
        }

        /**
         * Patch helper functions.
         *
         * Digging, Composting, and Planting are done in the Garden of Tranquility quest, but those interactions
         * have been moved to there, where they are subject to their own set of checks, instead of checking them
         * inside the main farming code.
         */

        // Digging a patch with a spade
        fun digPatch(player: Player, patch: FarmingPatch) {
            val p = patch.getPatchFor(player)

            if (p.isDead) {
                sendMessage(player, "You start digging the farming patch...")
                queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, Animations.HUMAN_DIG_SPADE_830)
                            playAudio(player, Sounds.DIGSPADE_1470)
                            return@queueScript delayScript(player, animationDuration(getAnimation(Animations.HUMAN_DIG_SPADE_830)) + 2)
                        }
                        1 -> {
                            animate(player, Animations.HUMAN_DIG_SPADE_830)
                            playAudio(player, Sounds.DIGSPADE_1470)
                            return@queueScript delayScript(player, animationDuration(getAnimation(Animations.HUMAN_DIG_SPADE_830)) + 1)
                        }
                        2 -> {
                            p.clear()
                            sendMessage(player, "You have successfully cleared this patch for new crops.")
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(player, 67984003, patch.getPatchFor(player)) // DigUpPatchDialogue.kt
            }
        }

        // Pouring a bucket of compost on a patch
        fun compostPatch(player: Player, patch: FarmingPatch, usedItem: Item, isPlantPot: Boolean = false) {
            val p = patch.getPatchFor(player)
            val patchName = p.patch.type.displayName()

            if (!p.isEmptyAndWeeded()) {
                sendMessage(player, "This patch needs to be empty and weeded to do that.")
            } else if (p.compost == CompostType.NONE) {
                animate(player, Animations.HUMAN_POUR_BUCKET_2283)
                playAudio(player, Sounds.FARMING_COMPOST_2427)
                runTask(player) {
                    if (removeItem(player, usedItem)) {
                        if (isPlantPot) { // plantpot is used in Garden of Tranquility. all one word is authentic.
                            sendMessage(player, "You fill the plantpot with ${usedItem.name.lowercase()}.")
                        } else {
                            sendMessage(player, "You treat the $patchName with ${usedItem.name.lowercase()}.")
                            p.compost = if (usedItem.id == Items.SUPERCOMPOST_6034) CompostType.SUPERCOMPOST else CompostType.COMPOST
                            if (p.compost == CompostType.SUPERCOMPOST) rewardXP(player, Skills.FARMING, 26.0) else rewardXP(player, Skills.FARMING, 18.5)
                            if (p.plantable != null && p.plantable?.applicablePatch != PatchType.FLOWER_PATCH) {
                                p.harvestAmt += if (p.compost == CompostType.COMPOST) 1 else if (p.compost == CompostType.SUPERCOMPOST) 2 else 0
                            }
                            p.cropLives += if (p.compost == CompostType.SUPERCOMPOST) 2 else 1
                        }
                        addItem(player, Items.BUCKET_1925)
                    }
                    return@runTask
                }
            } else {
                sendMessage(player, "This $patchName has already been treated with ${p.compost.name.lowercase()}.")
            }
        }

        // Planting seeds in a patch
        fun plantPatch(player: Player, patch: FarmingPatch, plantable: Plantable, plantItem: Item) {
            if (plantable.applicablePatch != patch.type) {
                val plantableNamePlural = StringUtils.plusS(plantable.displayName)
                val patchType = if (plantable.applicablePatch == PatchType.ALLOTMENT) "a vegetable patch" else prependArticle(plantable.applicablePatch.displayName())
                sendMessage(player, "You can only plant $plantableNamePlural in $patchType.")
                return
            }

            if (!hasLevelDyn(player, Skills.FARMING, plantable.requiredLevel)) {
                sendMessage(player, "You need a Farming level of ${plantable.requiredLevel} to plant this.")
                return
            }

            val p = patch.getPatchFor(player)

            if (p.getCurrentState() < 3 && p.isWeedy()) {
                sendMessage(player, "This patch needs weeding first.")
                return
            } else if (p.getCurrentState() > 3) {
                sendMessage(player, "There is already something growing in this patch.")
                return
            }

            if (!inInventory(player, plantItem.id, plantItem.amount)) {
                val seedPlural = if (plantItem.amount == 1) "seed" else "seeds"
                sendMessage(player, "You need ${plantItem.amount} $seedPlural to plant ${prependArticle(patch.type.displayName())}.")
                return
            }

            /*if (patch == FarmingPatch.WILDERNESS_FLOWER && plantable != Plantable.LIMPWURT_SEED) {
                return
            }*/

            val requiredItem = when (patch.type) {
                in treePatches -> Items.SPADE_952
                PatchType.FLOWER_PATCH -> if (plantable == Plantable.SCARECROW) null else Items.SEED_DIBBER_5343
                else -> Items.SEED_DIBBER_5343
            }
            if (requiredItem != null && !inInventory(player, requiredItem)) {
                sendDialogue(player, "You need ${prependArticle(requiredItem.asItem().name.lowercase())} to plant that.")
                return
            }
            val spiritTreePlanted = (getVarbit(player, 720) > 3 || getVarbit(player, 722) > 3 || getVarbit(player, 724) > 3)
            if (plantable == Plantable.SPIRIT_SAPLING && spiritTreePlanted) {
                sendDialogue(player, "You already have a spirit tree. You can't plant another one.")
                return
            }

            queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        val (anim, sound) = when (requiredItem) {
                            Items.SPADE_952 -> Pair(getAnimation(Animations.HUMAN_DIG_SPADE_830), Sounds.DIGSPADE_1470)
                            else -> Pair(seedDibberAnim,Sounds.FARMING_DIBBING_2432)
                        }
                        animate(player, anim)
                        playAudio(player, sound)
                        val delay = if (patch.type in treePatches || plantable == Plantable.SCARECROW) 3 else 0
                        return@queueScript delayScript(player,anim.duration + delay)
                    }
                    1 -> {
                        if (removeItem(player, plantItem)) {

                            // Achievement Diary check
                            if (plantable == Plantable.JUTE_SEED && patch == FarmingPatch.MCGRUBOR_HOPS && !player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE, 0, 7)) {
                                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 7)
                            }

                            // Garden of Tranquility planting checks
                            if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) <= 40) {

                                // planting marigolds for Elstan
                                if (getVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE) == 1
                                    && plantable == Plantable.MARIGOLD_SEED
                                    && patch == FarmingPatch.S_FALADOR_FLOWER_C) {
                                    setVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE, 2, true)
                                }

                                // planting onions for Lyra
                                if (getVarbit(player, GardenOfTranquility.VB_LYRA_STAGE) in 1..2
                                    && plantable == Plantable.ONION_SEED) {
                                    if (patch == FarmingPatch.PORT_PHAS_ALLOTMENT_NW) {
                                        setVarbit(player, GardenOfTranquility.VB_LYRA_NORTH, 1, true)
                                        setVarbit(player, GardenOfTranquility.VB_LYRA_STAGE, 2, true)
                                    } else if (patch == FarmingPatch.PORT_PHAS_ALLOTMENT_SE) {
                                        setVarbit(player, GardenOfTranquility.VB_LYRA_SOUTH, 1, true)
                                        setVarbit(player, GardenOfTranquility.VB_LYRA_STAGE, 2, true)
                                    }
                                }

                                // planting cabbages for Kragen
                                if (getVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE) in 1..2
                                    && plantable == Plantable.CABBAGE_SEED) {
                                    if (patch == FarmingPatch.ARDOUGNE_ALLOTMENT_N) {
                                        setVarbit(player, GardenOfTranquility.VB_KRAGEN_NORTH, 1, true)
                                        setVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE, 2, true)
                                    } else if (patch == FarmingPatch.ARDOUGNE_ALLOTMENT_S) {
                                        setVarbit(player, GardenOfTranquility.VB_KRAGEN_SOUTH, 1, true)
                                        setVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE, 2, true)
                                    }
                                }
                            }

                            // Plant the plant
                            p.plant(plantable)
                            rewardXP(player, Skills.FARMING, plantable.plantingXP)
                            p.setNewHarvestAmount()
                            if (p.patch.type in treePatches) {
                                addItem(player, Items.PLANT_POT_5350)
                            }

                            val itemAmount =
                                if (p.patch.type in treePatches) "the"
                                else if (plantItem.amount == 1) "a"
                                else plantItem.amount
                            val itemName =
                                if (plantItem.amount == 1) plantable.displayName else StringUtils.plusS(
                                    plantable.displayName
                                )
                            val patchName = p.patch.type.displayName()
                            if (plantable == Plantable.SCARECROW) {
                                sendMessage(player, "You place the scarecrow in the $patchName.")
                            } else {
                                sendMessage(player, "You plant $itemAmount $itemName in the $patchName.")
                            }
                        }
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, allowedNodes.toIntArray(), *FarmingPatch.patchNodes.toIntArray()) { player, used, with ->
            val patch = FarmingPatch.forObject(with.asScenery()) ?: return@onUseWith true
            val usedItem = used.asItem()
            val trimmablePatches = arrayOf(PatchType.TREE_PATCH, PatchType.BUSH_PATCH, PatchType.FRUIT_TREE_PATCH, PatchType.SPIRIT_TREE_PATCH, PatchType.CALQUAT_TREE_PATCH)

            if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) {
                if (!hasRequirement(player, Quests.MY_ARMS_BIG_ADVENTURE))
                    return@onUseWith true
            }

            player.faceLocation(with.location)
            when (usedItem.id) {
                Items.RAKE_5341 -> PatchRaker.rake(player,patch)
                Items.SEED_DIBBER_5343 -> sendMessage(player, "I should plant a seed, not the seed dibber.")
                Items.SPADE_952 -> digPatch(player, patch)
                Items.SECATEURS_5329, Items.MAGIC_SECATEURS_7409 -> {
                    val p = patch.getPatchFor(player)
                    if (patch.type in trimmablePatches) {
                        if (p.isDiseased && !p.isDead) {
                            submitIndividualPulse(player, object: Pulse() {
                                override fun pulse(): Boolean {
                                    if (usedItem.id == Items.SECATEURS_5329) animate(player, Animations.HUMAN_SECATEURS_CURE_3X_2277) else animate(player, Animations.HUMAN_MAGIC_SECATEURS_CURE_3X_3340)
                                    p.cureDisease()
                                    return true
                                }
                            })
                        } else if (p.plantable == Plantable.WILLOW_SAPLING && p.harvestAmt > 0) {
                            val pulse = CropHarvester.harvestPulse(player, with, Items.WILLOW_BRANCH_5933) ?: return@onUseWith false
                            submitIndividualPulse(player, pulse)
                        }
                    }
                }
                Items.GARDENING_TROWEL_5325, Items.PLANT_POT_5350 -> {
                    if (!inInventory(player, Items.GARDENING_TROWEL_5325)) {
                        sendMessage(player, "You need a trowel to fill plant pots with dirt.")
                        return@onUseWith true
                    }
                    val p = patch.getPatchFor(player)
                    if (!p.isWeedy() && !p.isEmptyAndWeeded()) {
                        sendMessage(player, "This patch has something growing in it.")
                        return@onUseWith true
                    } else if (p.currentGrowthStage != 3) {
                        sendMessage(player, "I should clear this of weeds before trying to take some dirt.")
                        return@onUseWith true
                    }

                    val potAmount = amountInInventory(player, Items.PLANT_POT_5350)

                    if (potAmount == 0) {
                        sendMessage(player, "You have no plant pots to fill.")
                        return@onUseWith true
                    }

                    submitIndividualPulse(player, object : Pulse(getAnimation(Animations.HUMAN_DIG_TROWEL_2272).duration) {
                        override fun pulse(): Boolean {
                            if (removeItem(player, Items.PLANT_POT_5350)) {
                                animate(player, Animations.HUMAN_DIG_TROWEL_2272)
                                addItem(player, Items.PLANT_POT_5354)
                            } else return true
                            return false
                        }
                    })
                }

                Items.PLANT_CURE_6036 -> {
                    val p = patch.getPatchFor(player)
                    val patchName = p.patch.type.displayName()
                    if (p.isDiseased && !p.isDead && patch.type !in trimmablePatches) {
                        sendMessage(player, "You treat the $patchName with the plant cure.")
                        queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    animate(player, Animations.HUMAN_PLANT_CURE_2288)
                                    playAudio(player, Sounds.FARMING_PLANTCURE_2438)
                                    return@queueScript delayScript(player, getAnimation(Animations.HUMAN_PLANT_CURE_2288).duration / 2)
                                }
                                1 -> {
                                    if (removeItem(player, usedItem)) {
                                        addItem(player, Items.VIAL_229)
                                        p.cureDisease()
                                        sendMessage(player, "It is restored to health.")
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

                Items.WATERING_CAN_5331,Items.WATERING_CAN1_5333,Items.WATERING_CAN2_5334,Items.WATERING_CAN3_5335,Items.WATERING_CAN4_5336,Items.WATERING_CAN5_5337,Items.WATERING_CAN6_5338,Items.WATERING_CAN7_5339,Items.WATERING_CAN8_5340 -> {
                    val p = patch.getPatchFor(player)
                    val t = p.patch.type
                    if (t == PatchType.ALLOTMENT || t == PatchType.FLOWER_PATCH || t == PatchType.HOPS_PATCH) {
                        submitIndividualPulse(player, object : Pulse() {
                            override fun pulse(): Boolean {
                                if (p.isWeedy() || p.isEmptyAndWeeded()) {
                                    sendMessage(player, "You should grow something first.")
                                    return true
                                }
                                if (p.isWatered || p.isGrown() || p.plantable == Plantable.SCARECROW) {
                                    sendMessage(player, "This patch doesn't need watering.")
                                    return true
                                }
                                if (p.isDiseased || p.isDead) {
                                    sendMessage(player, "Water isn't going to cure that!")
                                    return true
                                }
                                if (usedItem.id == Items.WATERING_CAN_5331) {
                                    sendMessage(player, "You need to fill the watering can first.")
                                    return true
                                }
                                animate(player, wateringCanAnim)
                                playAudio(player, Sounds.FARMING_WATERING_2446)
                                if (removeItem(player, usedItem)) {
                                    addItem(player, usedItem.id.getNext())
                                    p.water()
                                }
                                return true
                            }
                        })
                    } else {
                        sendMessage(player, "This patch doesn't need watering.")
                    }
                }

                Items.SUPERCOMPOST_6034, Items.COMPOST_6032 -> compostPatch(player, patch, usedItem)

                else -> {
                    val plantable = Plantable.forItemID(usedItem.id) ?: return@onUseWith false
                    val plantItem = when (patch.type) {
                        PatchType.ALLOTMENT -> Item(plantable.itemID, 3)
                        PatchType.HOPS_PATCH -> if (plantable == Plantable.JUTE_SEED) Item(plantable.itemID, 3) else Item(plantable.itemID, 4)
                        else -> Item(plantable.itemID,1)
                    }

                    plantPatch(player, patch, plantable, plantItem)
                }
            }
            return@onUseWith true
        }
    }

    private fun Int.getNext(): Int {
        if (this == Items.WATERING_CAN1_5333) return Items.WATERING_CAN_5331
        else return this - 1
    }
}
