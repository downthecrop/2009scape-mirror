package content.region.misthalin.varrock.quest.familycrest

import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable


@Initializable
class PerfectJewelryHandler (player: Player? = null): core.game.dialogue.DialoguePlugin(player){

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return PerfectJewelryHandler(player)
    }

    override fun open(vararg args: Any?): Boolean {
        println(player.name)
        if(player.inventory.containItems(2365, 1603)){
            options("Craft perfect ruby ring", "Craft perfect ruby necklace")
            stage = 1;
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {

        when(stage){
            1-> when(buttonId){

                1-> if(player.inventory.containItems(2365, 1603,1592)) {
                    player.inventory.remove(Item(2365), Item(1603))
                    player.inventory.add(Item(773))
                    sendDialogue("You made a perfect gold ring.")
                    stage = 1000
                }
                else{
                    sendDialogue("You do not have everything to make this item.")
                    stage = 1000;
                }

                2-> if(player.inventory.containItems(2365, 1603) && player.inventory.containItems(1597)){
                    player.inventory.remove(Item(2365), Item(1603))
                    player.inventory.add(Item(774))
                    sendDialogue("You made a perfect gold necklace.")
                    stage = 1000
                }
                else{
                    sendDialogue("You do not have everything to make this item.")
                    stage = 1000;
                }
            }

            1000 -> end()
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(core.game.dialogue.DialogueInterpreter.getDialogueKey("perfect-jewelry"))
    }

}
