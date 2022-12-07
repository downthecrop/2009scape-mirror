package rs09.game.node.entity.skill.farming

import api.toIntArray
import core.game.interaction.NodeUsageEvent
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

class UseWithBinHandler : InteractionListener {
    @JvmField
    val allowedNodes = ArrayList<Int>(100)
    val fillAnim = Animation(832)
    val compostPotionAnimation = Animation(2259)
    val scoopAnimation = Animation(8905)

    val bins = 7836..7840

    override fun defineListeners() {
        loadNodes()
        onUseWith(IntType.SCENERY, allowedNodes.toIntArray(), *bins.toIntArray()) {player, usedNode, with ->
            val cBin = CompostBins.forObject(with.asScenery()) ?: return@onUseWith true
            val bin = cBin.getBinForPlayer(player)
            val used = usedNode.id


            when(used){
                Items.COMPOST_POTION1_6476,Items.COMPOST_POTION2_6474, Items.COMPOST_POTION3_6472,Items.COMPOST_POTION4_6470 -> {
                    if(!bin.isSuperCompost && bin.isFinished && !bin.isClosed){
                        player.animator.animate(compostPotionAnimation)
                        player.pulseManager.run(object : Pulse(compostPotionAnimation.duration){
                            override fun pulse(): Boolean {
                                if(player.inventory.remove(usedNode.asItem())) {
                                    bin.convert()
                                    player.inventory.add(Item(used.getNext()))
                                }
                                return true
                            }
                        })
                    } else {
                        player.dialogueInterpreter.sendDialogue("You can only do this with an open bin of","finished regular compost.")
                    }
                }

                Items.BUCKET_1925 -> {
                    if(bin.isFinished && !bin.isClosed){
                        player.pulseManager.run(object : Pulse(scoopAnimation.duration){
                            override fun pulse(): Boolean {
                                if(!player.inventory.containsItem(usedNode.asItem())) return true
                                player.animator.animate(scoopAnimation)
                                val item = bin.takeItem()
                                if(item != null && player.inventory.remove(usedNode.asItem())){
                                    player.inventory.add(item)
                                }
                                return item == null || !player.inventory.containsItem(usedNode.asItem())
                            }
                        })
                    } else {
                        player.dialogueInterpreter.sendDialogue("You can only scoop an opened bin of finished compost.")
                    }
                }

                else ->
                    if(bin.isFull()){
                        player.sendMessage("This compost bin is already full.")
                        return@onUseWith true
                    } else {
                        player.pulseManager.run(object : Pulse(fillAnim.duration){
                            override fun pulse(): Boolean {
                                player.animator.animate(fillAnim)
                                if(player.inventory.remove(usedNode.asItem())){
                                    bin.addItem(usedNode.asItem())
                                }
                                return bin.isFull() || player.inventory.getAmount(usedNode.asItem()) == 0
                            }
                        })
                    }
            }
            return@onUseWith true
        }
    }

    fun loadNodes() {
        for(p in Plantable.values()){
            if(p.harvestItem != Items.SCARECROW_6059) {
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

    private fun Int.getNext(): Int{
        if(this != 6476) return this + 2
        else return Items.VIAL_229
    }
}