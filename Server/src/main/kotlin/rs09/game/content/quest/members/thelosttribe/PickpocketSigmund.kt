package rs09.game.content.quest.members.thelosttribe

import core.cache.def.impl.NPCDefinition
import core.game.content.dialogue.FacialExpression
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.world.GameWorld

@Initializable
/**
 * handles pickpocketing sigmund during the lost tribe quest
 * @author Ceikry
 */
class PickpocketSigmund : OptionHandler(){
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(2082).handlers["option:pickpocket"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false

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
                            player.dialogueInterpreter.sendDialogues(2082,FacialExpression.ANGRY,"What do you think you're doing?!")
                        }
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
        return true
    }

}