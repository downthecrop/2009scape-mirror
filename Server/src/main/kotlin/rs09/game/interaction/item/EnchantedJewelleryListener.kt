package rs09.game.interaction.item

import api.sendMessage
import core.game.node.Node
import core.game.node.entity.player.Player
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.global.EnchantedJewellery
import rs09.game.interaction.InteractionListener
import rs09.tools.START_DIALOGUE
import java.util.*

/**
 * Listener for enchanted jewellery options
 * @author Ceikry
 */
class EnchantedJewelleryListener : InteractionListener {
    val ids: IntArray

    init {
        val idsList = ArrayList<Int>()
        for (j in EnchantedJewellery.values()) {
            for (id in j.ids) {
                idsList.add(id)
            }
        }
        ids = idsList.toIntArray()
    }

    override fun defineListeners() {
        on(ids,ITEM,"operate"){ player, node ->
            handle(player,node,true)
            return@on true
        }
        on(ids,ITEM,"rub"){ player, node ->
            handle(player,node,false)
            return@on true;
        }
    }
    private fun handle(player: Player, node: Node, isEquipped: Boolean){
        player.pulseManager.current.stop()
        val item = node.asItem()
        val jewellery = EnchantedJewellery.forItem(item)
        if (jewellery != null) {
            if (jewellery.isLastItemId(jewellery.getItemIndex(item)) && !jewellery.isCrumble) {
                sendMessage(player,"The ${jewellery.getJewelleryType(item)} has lost its charge.")
                sendMessage(player,"It will need to be recharged before you can use it again.")
                return
            }
            sendMessage(player,"You rub the ${jewellery.getJewelleryType(item)}...")
            if (jewellery.options.isNotEmpty()) {
                player.dialogueInterpreter.open(object : DialogueFile(){
                    override fun handle(componentID: Int, buttonID: Int) {
                        when(stage){
                            START_DIALOGUE -> {
                                interpreter!!.sendOptions("Where would you like to go?", *jewellery.options)
                                stage++
                            }
                            1 -> end().also{ jewellery.use(player, item, buttonID - 1,isEquipped) }
                        }
                    }
                })
            } else {
                jewellery.use(player, item, 0, isEquipped)
            }
        }
    }
}