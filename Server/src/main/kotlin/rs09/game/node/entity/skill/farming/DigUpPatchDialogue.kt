package rs09.game.node.entity.skill.farming

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable

@Initializable
class DigUpPatchDialogue(player: Player? = null) : DialoguePlugin(player) {
    var patch: Patch? = null

    override fun newInstance(player: Player?): DialoguePlugin {
        return DigUpPatchDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        patch = args[0] as Patch
        if(patch?.isWeedy() == true){
            player.dialogueInterpreter.sendDialogue("Use a rake to get rid of weeds.")
            stage = 1000
            return true
        }
        player.dialogueInterpreter.sendOptions("Dig up this patch?","Yes","No")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> when(buttonId){
                1 -> {
                    end()
                    player.animator.animate(Animation(830))
                    player.audioManager.send(Audio(1470, 1, 1))
                    player.pulseManager.run(object : Pulse(){
                        override fun pulse(): Boolean {
                            if(patch?.patch?.type == PatchType.TREE){
                                if(patch?.getCurrentState() == (patch?.plantable?.value ?: 0) + (patch?.plantable?.stages ?: 0) + 2 && patch?.isWeedy() != true){
                                    if(!player.inventory.add(Item(patch?.plantable?.harvestItem ?: 0))){
                                        GroundItemManager.create(Item(patch?.plantable?.harvestItem ?: 0),player)
                                    }
                                }
                            }
                            patch?.clear()
                            return true
                        }
                    })
                }
                else -> end()
            }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(67984003)
    }

}