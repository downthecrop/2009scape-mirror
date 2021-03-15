package rs09.game.interaction.item

import core.game.node.entity.player.link.quest.QuestRepository
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners.run

class QuestCapeListener : InteractionListener() {

    val IDs = intArrayOf(9813,9814)

    override fun defineListeners() {
        on(IDs,ITEM,"wear"){ player, item ->
            val MAX_QP = QuestRepository.getQuests().values.sumBy { it.questPoints }
            if (player.questRepository.points < MAX_QP) {
                player.packetDispatch.sendMessage("You cannot wear this " + item.name.toLowerCase() + " yet.")
                return@on true
            }
            return@on run(item.id, 0, "equip", player, item)
        }
    }
}