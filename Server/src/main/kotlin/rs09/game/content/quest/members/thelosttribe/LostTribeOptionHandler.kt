package rs09.game.content.quest.members.thelosttribe

import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

private val BOOK = Item(Items.GOBLIN_SYMBOL_BOOK_5009)
@Initializable
/**
 * Handles misc. lost tribe interactions
 * @author Ceikry
 */
class LostTribeOptionHandler : OptionHandler(){
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(5008).handlers["option:look-at"] = this
        ItemDefinition.forId(5009).handlers["option:read"] = this
        SceneryDefinition.forId(6916).handlers["option:search"] = this
        SceneryDefinition.forId(6911).handlers["option:search"] = this
        NPCDefinition.forId(2084).handlers["option:follow"] = this
        NPCDefinition.forId(2086).handlers["option:follow"] = this
        SceneryDefinition.forId(32952).handlers["option:open"] = this
        SceneryDefinition.forId(32953).handlers["option:open"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        when(node.id){
            5008 -> player.interfaceManager.open(Component(50))
            5009 -> player.interfaceManager.open(Component(183))
            6916 -> {
                if(!player.inventory.containsItem(BOOK) && !player.bank.containsItem(BOOK) && player.questRepository.getQuest("Lost Tribe").getStage(player) >= 41){
                    player.dialogueInterpreter.sendDialogue("'A History of the Goblin Race.' This must be it.")
                    player.inventory.add(BOOK)
                } else {
                    return false
                }
            }
            6911 -> {
                if(!player.inventory.containsItem(Item(Items.SILVERWARE_5011)) && player.questRepository.getQuest("Lost Tribe").getStage(player) == 48){
                    player.dialogueInterpreter.sendItemMessage(Items.SILVERWARE_5011,"You find the missing silverware!")
                    player.inventory.add(Item(Items.SILVERWARE_5011))
                    player.questRepository.getQuest("Lost Tribe").setStage(player,49)
                } else {
                    player.sendMessage("You find nothing.")
                }
            }
            32952,32953 -> {
                player.dialogueInterpreter.sendDialogues(player,FacialExpression.THINKING,"I don't think I have permission to go in there.")
            }
        }

        if(option.equals("follow")){
            when(node.id){
                2084 -> GoblinFollower.sendToLumbridge(player)
                2085 -> GoblinFollower.sendToMines(player)
            }
        }
        return true
    }

}