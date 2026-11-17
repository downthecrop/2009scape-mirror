package content.region.misthalin.varrock.dialogue

import content.region.misthalin.varrock.diary.VarrockAchivementDiary
import core.api.addItem
import core.api.closeAllInterfaces
import core.api.finishTask
import core.api.removeItem
import core.api.sendMessage
import core.game.diary.DiaryLevel
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items

class GertrudeKittenChooseInterface : InterfaceListener {

    companion object {
        val kittens = intArrayOf(
            // normal kittens
            Items.PET_KITTEN_1555, Items.PET_KITTEN_1556, Items.PET_KITTEN_1557,
            Items.PET_KITTEN_1558, Items.PET_KITTEN_1559, Items.PET_KITTEN_1560,

            // hell-kitten
            Items.HELL_KITTEN_7583,

            // purple kitten
            Items.PET_KITTEN_14089
        )
    }

    override fun defineInterfaceListeners() {
        on(Components.KITTEN_PICK_737) { player, _, _, buttonID, _, _ ->
            // buttons 3, 4, 5, 6, 7, 8 are the buttons that select each cat.
            val index = buttonID - 3

            // fetch the cat based on button. If you click button 3, 3-3=0, so gives the first kitten in the array.
            val kittenId = if (index in kittens.indices) kittens[index] else kittens[0]
            val kitty = Item(kittenId)

            // note, inventory and money checks are already done earlier in Gertrude's dialogue
            if (removeItem(player, Item(Items.COINS_995, 100))) {
                sendMessage(player, "Gertrude gives you another kitten.")
                if (player.familiarManager.hasFamiliar()) {
                    addItem(player, kitty.id)
                } else {
                    player.familiarManager.summon(kitty, true, false)
                }
                finishTask(player, DiaryType.VARROCK, DiaryLevel.MEDIUM, VarrockAchivementDiary.Companion.MediumTasks.SELECT_KITTEN_COLOR)
            } else {
                sendMessage(player, "You need 100 coins to buy a kitten.")
            }
            closeAllInterfaces(player)
            return@on true
        }
    }
}