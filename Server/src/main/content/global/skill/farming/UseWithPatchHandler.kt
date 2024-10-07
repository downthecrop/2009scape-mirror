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

class UseWithPatchHandler : InteractionListener {
    val RAKE = Items.RAKE_5341
    val SEED_DIBBER = Items.SEED_DIBBER_5343
    val SPADE = Items.SPADE_952
    val SECATEURS = Items.SECATEURS_5329
    val MAGIC_SECATEURS = Items.MAGIC_SECATEURS_7409
    val TROWEL = Items.GARDENING_TROWEL_5325
    val spadeDigAnim = getAnimation(830)
    val trowelDigAnim = getAnimation(2272)
    val pourBucketAnim = getAnimation(2283)
    val seedDibberAnim = getAnimation(2291)
    val wateringCanAnim = getAnimation(2293)
    val plantCureAnim = getAnimation(2288)
    val secateursTreeAnim = getAnimation(2277)
    val magicSecateursTreeAnim = getAnimation(3340)

    @JvmField
    val allowedNodes = ArrayList<Int>()

    override fun defineListeners() {
        loadNodes()
        onUseWith(IntType.SCENERY, allowedNodes.toIntArray(), *FarmingPatch.patchNodes.toIntArray()) { player, used, with ->
            val patch = FarmingPatch.forObject(with.asScenery()) ?: return@onUseWith true
            val usedItem = used.asItem()

            if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) {
                if (!hasRequirement(player, "My Arm's Big Adventure"))
                    return@onUseWith true
            }

            player.faceLocation(with.location)
            when (usedItem.id) {
                RAKE -> PatchRaker.rake(player,patch)
                SEED_DIBBER -> sendMessage(player, "I should plant a seed, not the seed dibber.")
                SPADE -> {
                    val anim = spadeDigAnim
                    val p = patch.getPatchFor(player)
                    if (p.isDead) {
                        sendMessage(player, "You start digging the farming patch...")
                        queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    animate(player, anim)
                                    playAudio(player, Sounds.DIGSPADE_1470)
                                    return@queueScript delayScript(player,anim.duration + 2)
                                }
                                1 -> {
                                    animate(player, anim)
                                    playAudio(player, Sounds.DIGSPADE_1470)
                                    return@queueScript delayScript(player, anim.duration + 1)
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
                SECATEURS, MAGIC_SECATEURS -> {
                    val p = patch.getPatchFor(player)
                    if (patch.type == PatchType.TREE_PATCH) {
                        if (p.isDiseased && !p.isDead) {
                            submitIndividualPulse(player, object: Pulse() {
                                override fun pulse(): Boolean {
                                    if (usedItem.id == SECATEURS) animate(player, secateursTreeAnim) else animate(player, magicSecateursTreeAnim)
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
                TROWEL, Items.PLANT_POT_5350 -> {
                    if (!inInventory(player, TROWEL)) {
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

                    val anim = trowelDigAnim

                    submitIndividualPulse(player, object : Pulse(anim.duration) {
                        override fun pulse(): Boolean {
                            if (removeItem(player, Items.PLANT_POT_5350)) {
                                animate(player, anim)
                                addItem(player, Items.PLANT_POT_5354)
                            } else return true
                            return false
                        }
                    })
                }

                Items.PLANT_CURE_6036 -> {
                    val p = patch.getPatchFor(player)
                    val patchName = p.patch.type.displayName()
                    if (p.isDiseased && !p.isDead) {
                        sendMessage(player, "You treat the $patchName with the plant cure.")
                        queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    animate(player, plantCureAnim)
                                    playAudio(player, Sounds.FARMING_PLANTCURE_2438)
                                    return@queueScript delayScript(player, plantCureAnim.duration / 2)
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

                Items.SUPERCOMPOST_6034, Items.COMPOST_6032 -> {
                    val p = patch.getPatchFor(player)
                    val patchName = p.patch.type.displayName()

                    if (!p.isEmptyAndWeeded()) {
                        sendMessage(player, "This patch needs to be empty and weeded to do that.")
                    } else if (p.compost == CompostType.NONE) {
                        animate(player, pourBucketAnim)
                        playAudio(player, Sounds.FARMING_COMPOST_2427)
                        runTask(player) {
                            if (player.inventory.remove(usedItem,false)) {
                                sendMessage(player, "You treat the $patchName with ${usedItem.name.lowercase()}.")
                                p.compost = if (usedItem.id == Items.SUPERCOMPOST_6034) CompostType.SUPERCOMPOST else CompostType.COMPOST
                                if (p.compost == CompostType.SUPERCOMPOST) rewardXP(player, Skills.FARMING, 26.0) else rewardXP(player, Skills.FARMING, 18.5)
                                if (p.plantable != null && p.plantable?.applicablePatch != PatchType.FLOWER_PATCH) {
                                    p.harvestAmt += if (p.compost == CompostType.COMPOST) 1 else if (p.compost == CompostType.SUPERCOMPOST) 2 else 0
                                }
                                p.cropLives += if (p.compost == CompostType.SUPERCOMPOST) 2 else 1
                                addItem(player, Items.BUCKET_1925)
                            }
                            return@runTask
                        }
                    } else {
                        sendMessage(player, "This $patchName has already been treated with ${p.compost.name.lowercase()}.")
                    }
                }

                else -> {
                    val plantable = Plantable.forItemID(usedItem.id) ?: return@onUseWith false

                    if (plantable.applicablePatch != patch.type) {
                        val plantableNamePlural = StringUtils.plusS(plantable.displayName)
                        val patchType = if (plantable.applicablePatch == PatchType.ALLOTMENT) "a vegetable patch" else prependArticle(plantable.applicablePatch.displayName())
                        sendMessage(player, "You can only plant $plantableNamePlural in $patchType.")
                        return@onUseWith true
                    }

                    if (!hasLevelDyn(player, Skills.FARMING, plantable.requiredLevel)) {
                        sendMessage(player, "You need a Farming level of ${plantable.requiredLevel} to plant this.")
                        return@onUseWith true
                    }

                    val p = patch.getPatchFor(player)
                    if (p.getCurrentState() < 3 && p.isWeedy() && plantable != Plantable.SCARECROW) {
                        sendMessage(player, "This patch needs weeding first.")
                        return@onUseWith true
                    } else if (p.getCurrentState() > 3) {
                        sendMessage(player, "There is already something growing in this patch.")
                        return@onUseWith true
                    }

                    val plantItem = when (patch.type) {
                        PatchType.ALLOTMENT -> Item(plantable.itemID, 3)
                        PatchType.HOPS_PATCH -> if (plantable == Plantable.JUTE_SEED) Item(plantable.itemID, 3) else Item(plantable.itemID, 4)
                        else -> Item(plantable.itemID,1)
                    }

                    if (!player.inventory.containsItem(plantItem)) {
                        val seedPlural = if (plantItem.amount == 1) "seed" else "seeds"
                        sendMessage(player, "You need ${plantItem.amount} $seedPlural to plant ${prependArticle(patch.type.displayName())}.")
                        return@onUseWith true
                    }

                    val requiredItem = when (patch.type) {
                        PatchType.TREE_PATCH, PatchType.FRUIT_TREE_PATCH -> Items.SPADE_952
                        PatchType.FLOWER_PATCH -> if (plantable == Plantable.SCARECROW) null else Items.SEED_DIBBER_5343
                        else -> Items.SEED_DIBBER_5343
                    }
                    if (requiredItem != null && !inInventory(player, requiredItem)) {
                        sendMessage(player, "You need ${prependArticle(requiredItem.asItem().name.lowercase())} to plant that.")
                        return@onUseWith true
                    }
                    player.lock()
                    if (removeItem(player, plantItem)) {
                        when (requiredItem) {
                            Items.SPADE_952 -> {
                                animate(player, spadeDigAnim)
                                playAudio(player, Sounds.DIGSPADE_1470)
                            }
                            Items.SEED_DIBBER_5343 -> {
                                animate(player, seedDibberAnim)
                                playAudio(player, Sounds.FARMING_DIBBING_2432)
                            }
                        }
                        val delay = if (patch.type == PatchType.TREE_PATCH || patch.type == PatchType.FRUIT_TREE_PATCH || plantable == Plantable.SCARECROW) 0 else 3
                        submitIndividualPulse(player, object : Pulse(delay) {
                            override fun pulse(): Boolean {
                                if (plantable == Plantable.JUTE_SEED && patch == FarmingPatch.MCGRUBOR_HOPS && !player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE, 0, 7)) {
                                    player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 7)
                                }
                                p.plant(plantable)
                                rewardXP(player, Skills.FARMING, plantable.plantingXP)
                                p.setNewHarvestAmount()
                                if (p.patch.type == PatchType.TREE_PATCH || p.patch.type == PatchType.FRUIT_TREE_PATCH) {
                                    addItem(player, Items.PLANT_POT_5350)
                                }

                                val itemAmount =
                                    if (p.patch.type == PatchType.TREE_PATCH || p.patch.type == PatchType.FRUIT_TREE_PATCH) "the"
                                    else if (plantItem.amount == 1) "a"
                                    else plantItem.amount
                                val itemName = if (plantItem.amount == 1) plantable.displayName else StringUtils.plusS(plantable.displayName)
                                val patchName = p.patch.type.displayName()
                                if (plantable == Plantable.SCARECROW) {
                                    sendMessage(player, "You place the scarecrow in the $patchName.")
                                } else {
                                    sendMessage(player, "You plant $itemAmount $itemName in the $patchName.")
                                }

                                player.unlock()
                                return true
                            }
                        })
                    }
                }
            }

            return@onUseWith true
        }
    }

    fun loadNodes() {
        for (p in Plantable.values()) {
            allowedNodes.add(p.itemID)
        }
        allowedNodes.addAll(
            arrayListOf(
                RAKE,
                SEED_DIBBER,
                SPADE,
                SECATEURS,
                MAGIC_SECATEURS,
                TROWEL,
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

    private fun Int.getNext(): Int {
        if (this == Items.WATERING_CAN1_5333) return Items.WATERING_CAN_5331
        else return this - 1
    }
}
