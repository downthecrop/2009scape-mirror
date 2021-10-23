package rs09.game.content.global

import core.game.content.global.EnchantedJewellery
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.START_DIALOGUE

class EnchantedJewelleryDialogueFile(val jewellery: EnchantedJewellery, val item: Item) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> interpreter!!.sendOptions("Where would you like to go?", *jewellery.options).also { stage++ }
            1 -> {
                jewellery.use(player, item, buttonID - 1,item.slot < player!!.equipment.capacity() && player!!.equipment[item.slot] == item)
                end()
            }
        }
    }
}