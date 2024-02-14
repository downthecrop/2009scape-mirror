package content.global.skill.farming

import core.api.*
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

class UseWithBinHandler : InteractionListener {
    @JvmField
    val allowedNodes = ArrayList<Int>(100)
    val fillAnim = Animation(832)
    val compostPotionAnimation = Animation(2259)
    val scoopAnimation = Animation(8905)

    val bins = 7836..7840

    override fun defineListeners() {
        loadNodes()
        onUseWith(IntType.SCENERY, allowedNodes.toIntArray(), *bins.toIntArray()) { player, usedNode, with ->
            val cBin = CompostBins.forObject(with.asScenery()) ?: return@onUseWith true
            val bin = cBin.getBinForPlayer(player)
            val used = usedNode.id


            when (used) {
                Items.COMPOST_POTION1_6476, Items.COMPOST_POTION2_6474, Items.COMPOST_POTION3_6472, Items.COMPOST_POTION4_6470 -> {
                    if (!bin.isSuperCompost && bin.isFinished && !bin.isClosed) {
                        animate(player, compostPotionAnimation)
                        submitIndividualPulse(player, object : Pulse(compostPotionAnimation.duration) {
                            override fun pulse(): Boolean {
                                if (removeItem(player, usedNode.asItem())) {
                                    bin.convert()
                                    addItem(player, used.getNext())
                                }
                                return true
                            }
                        })
                    } else {
                        sendDialogue(player, "You can only do this with an open bin of finished regular compost.")
                    }
                }

                Items.BUCKET_1925 -> {
                    if (bin.isFinished && !bin.isClosed) {
                        submitIndividualPulse(player, object : Pulse(scoopAnimation.duration) {
                            override fun pulse(): Boolean {
                                if (!player.inventory.containsItem(usedNode.asItem())) return true
                                animate(player, scoopAnimation)
                                val item = bin.takeItem()
                                if (item != null && removeItem(player, usedNode.asItem())) {
                                    player.inventory.add(item)
                                }
                                return item == null || !player.inventory.containsItem(usedNode.asItem())
                            }
                        })
                    } else {
                        sendDialogue(player, "You can only scoop an opened bin of finished compost.")
                    }
                }

                else ->
                    if (bin.isFull()) {
                        sendMessage(player, "This compost bin is already full.")
                        return@onUseWith true
                    } else if (!bin.isFinished) {
                        submitIndividualPulse(player, object : Pulse(fillAnim.duration) {
                            override fun pulse(): Boolean {
                                animate(player, fillAnim)
                                if (removeItem(player, usedNode.asItem())) {
                                    bin.addItem(usedNode.asItem())
                                }
                                return bin.isFull() || player.inventory.getAmount(usedNode.asItem()) == 0
                            }
                        })
                    } else {
                        sendDialogue(player, "The compost bin must be empty of compost before you can put new items in it.")
                    }
            }
            return@onUseWith true
        }
    }

    fun loadNodes() {
        for (p in Plantable.values()) {
            if (p.harvestItem != Items.SCARECROW_6059) {
                allowedNodes.add(p.harvestItem)
            }
        }
        allowedNodes.add(Items.COCONUT_SHELL_5978)
        allowedNodes.add(Items.WEEDS_6055)
        allowedNodes.add(Items.COMPOST_POTION4_6470)
        allowedNodes.add(Items.COMPOST_POTION3_6472)
        allowedNodes.add(Items.COMPOST_POTION2_6474)
        allowedNodes.add(Items.COMPOST_POTION1_6476)
        allowedNodes.add(Items.BUCKET_1925)
    }

    private fun Int.getNext(): Int {
        if (this != 6476) return this + 2
        else return Items.VIAL_229
    }
}
