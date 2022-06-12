package rs09.game.content.activity.achievementdiary

import core.game.component.Component
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import org.rs09.consts.Components
import rs09.game.interaction.InterfaceListener

private const val TASK_DRAYNOR_BANK = 15

// This will not work until the listener system allows more than one
// listener to handle the event.
class LumbridgeAchievementDiaryListener : InterfaceListener {
    private fun checkDraynorBankAchievement(player: Player, component: Component): Boolean {
        if (player.location.withinDistance(Location(3092, 3243, 0))) {
            player.achievementDiaryManager.finishTask(
                player,
                DiaryType.LUMBRIDGE,
                1,
                TASK_DRAYNOR_BANK
            )
        }

        return true
    }

    override fun defineInterfaceListeners() {
        // onOpen(Components.BANK_V2_MAIN_762, ::checkDraynorBankAchievement)
    }
}