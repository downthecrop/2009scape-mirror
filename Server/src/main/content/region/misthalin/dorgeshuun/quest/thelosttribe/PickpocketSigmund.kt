package content.region.misthalin.dorgeshuun.quest.thelosttribe

import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.world.GameWorld

/**
 * handles pickpocketing sigmund during the lost tribe quest
 * @author Ceikry
 */
class PickpocketSigmund : InteractionListener {
    val SIGMUND = NPCs.SIGMUND_2082

    override fun defineListeners() {
        on(SIGMUND, IntType.NPC, "pickpocket"){ player, node ->
            player.lock()
            GameWorld.Pulser.submit(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> player.animator.animate(Animation(881))
                        3 -> {
                            if(player.questRepository.getQuest("Lost Tribe").getStage(player) == 47 && !player.inventory.containsItem(Item(Items.KEY_423))){
                                player.inventory.add(Item(Items.KEY_423))
                                player.dialogueInterpreter.sendItemMessage(Items.KEY_423,"You find a small key on Sigmund.")
                            } else {
                                player.dialogueInterpreter.sendDialogues(2082, core.game.dialogue.FacialExpression.ANGRY,"What do you think you're doing?!")
                            }
                            player.unlock()
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