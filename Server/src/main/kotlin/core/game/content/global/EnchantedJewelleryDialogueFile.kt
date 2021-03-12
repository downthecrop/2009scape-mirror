package core.game.content.global

import core.game.content.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.START_DIALOGUE

class EnchantedJewelleryDialogueFile(val jewellery: EnchantedJewellery, val item: Item) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> interpreter!!.sendOptions("Where would you like to go?", *jewellery.options).also { stage++ }
            1 -> {
                jewellery.use(player, item, buttonID - 1, player!!.equipment.containsItem(item))
                end()
            }
        }
    }
}