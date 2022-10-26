package rs09.game.interaction.item.withobject

import api.*
import core.game.node.entity.player.link.diary.DiaryType
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for using cheese on Ridgeley on the treadmill
 * @author Byte
 */
@Suppress("unused")
class HairdresserCheeseListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.CHEESE_1985, Scenery.TREADMILL_11677) { player, used, _ ->
            lock(player, 5)

            var isTaskSuccessful = false
            runTask(player,3,1) {
                if (!removeItem(player, used)) {
                    return@runTask
                }

                sendMessage(player,"You throw the cheese to Ridgeley, for which he appears grateful.")
                player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 0, 6)
                unlock(player)

                isTaskSuccessful = true
            }

            return@onUseWith isTaskSuccessful
        }
    }
}
