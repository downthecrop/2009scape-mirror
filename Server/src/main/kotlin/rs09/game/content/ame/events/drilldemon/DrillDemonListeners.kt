package rs09.game.content.ame.events.drilldemon

import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class DrillDemonListeners : InteractionListener() {
    val MATS = intArrayOf(10076,10077,10078,10079)
    override fun defineListeners() {

        on(MATS,SCENERY,"use"){ player, node ->
            val correctTask = player.getAttribute(DrillDemonUtils.DD_KEY_TASK,-1)
            if(correctTask == -1){
                player.sendMessage("You can't do that right now.")
                return@on  true
            }

            val task = DrillDemonUtils.getMatTask(node.id,player)
            val npc = NPC(NPCs.SERGEANT_DAMIEN_2790)

            player.lock()
            val anim = DrillDemonUtils.animationForTask(task)
            player.walkingQueue.reset()
            player.walkingQueue.addPath(node.location.x,4820)
            player.pulseManager.run(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        2 -> player.faceLocation(player.location.transform(0,-1,0))
                        3 -> player.animator.animate(DrillDemonUtils.animationForTask(task)).also { delay = anim.duration / 2 }
                        6 -> {
                            if(task == correctTask){
                                player.incrementAttribute(DrillDemonUtils.DD_CORRECT_COUNTER)
                                player.dialogueInterpreter.open(SeargentDamienDialogue(true),npc)
                            } else {
                                player.dialogueInterpreter.open(SeargentDamienDialogue(false),npc)
                            }
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }

    }
}