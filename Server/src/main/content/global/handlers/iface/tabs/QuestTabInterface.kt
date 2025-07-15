package content.global.handlers.iface.tabs

import content.global.handlers.iface.tabs.QuestTabUtils.showRequirementsInterface
import core.api.openInterface
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.diary.DiaryType
import org.rs09.consts.Components

class QuestTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.QUESTJOURNAL_V2_274) { player, _, _, buttonID, _, _ ->
            if (buttonID == 3) {
                player.achievementDiaryManager.openTab()
            } else {
                val quest = player.questRepository.forButtonId(buttonID)
                if (quest != null) {
                    openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
                    quest.drawJournal(player, quest.getStage(player))
                } else {
                    showRequirementsInterface(player, buttonID)
                }
            }
            return@on true
        }

        on(Components.AREA_TASK_259) { player, _, _, buttonID, _, _ ->
            if (buttonID == 8) {
                player.interfaceManager.openTab(2, Component(Components.QUESTJOURNAL_V2_274))
            } else {
                player.achievementDiaryManager.getDiary(DiaryType.forChild(buttonID))?.open(player)
            }
            return@on true
        }
    }
}