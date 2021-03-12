package core.game.interaction.item

import core.game.interaction.OptionListener
import core.game.content.global.EnchantedJewellery
import core.game.content.global.EnchantedJewelleryDialogueFile
import java.util.ArrayList

/**
 * Listener for enchanted jewellery options
 * @author Ceikry
 */
class EnchantedJewelleryListener : OptionListener() {
    val IDs: IntArray

    init {
        val idsList = ArrayList<Int>()
        for (j in EnchantedJewellery.values()) {
            for (id in j.ids) {
                idsList.add(id)
            }
        }
        IDs = idsList.toIntArray()
    }

    override fun defineListeners() {

        on(IDs,ITEM,"rub","operate"){player,node ->
            val item = node.asItem()
            val jewellery = EnchantedJewellery.forItem(item)
            if (jewellery.isLast(jewellery.getItemIndex(item))) {
                player.packetDispatch.sendMessage("The " + jewellery.getNameType(item) + " has lost its charge.")
                player.packetDispatch.sendMessage("It will need to be recharged before you can use it again.")
                return@on true
            }
            player.packetDispatch.sendMessage("You rub the " + jewellery.getNameType(item) + "...")

            if (jewellery == EnchantedJewellery.DIGSITE_PENDANT) {
                jewellery.use(player, item, 0, player.equipment.containsItem(item))
                return@on true
            } else {
                player.dialogueInterpreter.open(EnchantedJewelleryDialogueFile(jewellery,item))
            }
            return@on true
        }

    }
}