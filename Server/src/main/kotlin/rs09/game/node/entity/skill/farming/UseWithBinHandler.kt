package rs09.game.node.entity.skill.farming

import core.game.interaction.NodeUsageEvent
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

object UseWithBinHandler {
    @JvmField
    val allowedNodes = ArrayList<Int>(100)
    val fillAnim = Animation(832)
    val compostPotionAnimation = Animation(2259)
    val scoopAnimation = Animation(8905)

    init {
        loadNodes()
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

    @JvmStatic
    fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val used = event.used.id
        val cBin = CompostBins.forObject(event.usedWith.asScenery()) ?: return false
        val bin = cBin.getBinForPlayer(player)

        when(used){
            Items.COMPOST_POTION1_6476,Items.COMPOST_POTION2_6474, Items.COMPOST_POTION3_6472,Items.COMPOST_POTION4_6470 -> {
                if(!bin.isSuperCompost && bin.isFinished && !bin.isClosed){
                    player.animator.animate(compostPotionAnimation)
                    player.pulseManager.run(object : Pulse(compostPotionAnimation.duration){
                        override fun pulse(): Boolean {
                            if(player.inventory.remove(event.usedItem)) {
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
                            if(!player.inventory.containsItem(event.usedItem)) return true
                            player.animator.animate(scoopAnimation)
                            val item = bin.takeItem()
                            if(item != null && player.inventory.remove(event.usedItem)){
                                player.inventory.add(item)
                            }
                            return item == null || !player.inventory.containsItem(event.usedItem)
                        }
                    })
                } else {
                    player.dialogueInterpreter.sendDialogue("You can only scoop an opened bin of finished compost.")
                }
            }

            else ->
            if(bin.isFull()){
                player.sendMessage("This compost bin is already full.")
                return true
            } else {
                player.pulseManager.run(object : Pulse(fillAnim.duration){
                    override fun pulse(): Boolean {
                        player.animator.animate(fillAnim)
                        if(player.inventory.remove(event.usedItem)){
                            bin.addItem(event.usedItem)
                        }
                        return bin.isFull() || player.inventory.getAmount(event.usedItem) == 0
                    }
                })
            }
        }

        return true
    }

    private fun Int.getNext(): Int{
        if(this != 6476) return this + 2
        else return Items.VIAL_229
    }
}