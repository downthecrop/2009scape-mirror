package content.region.fremennik.rellekka.dialogue

import core.api.*
import core.game.dialogue.FacialExpression
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

const val COUNCIL_WORKER = 1287

class CouncilWorkerDiaryDialogue() : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {

        when (stage) {
            START_DIALOGUE -> {
                player(core.game.dialogue.FacialExpression.FRIENDLY, "About my achievement diary...");stage++
            }
            1 -> {
                npc(COUNCIL_WORKER,  "You have completed the Fremennik Easy Diary!");stage++
                //player.achievementDiaryManager.getDiary(DiaryType.FREMENNIK).setLevelRewarded(0)
            }
            2 -> {
                player?.let { addItem(it, Items.FREMENNIK_SEA_BOOTS_1_14571) }
                player?.let {
                    sendItemDialogue(
                        it,
                        Items.FREMENNIK_SEA_BOOTS_1_14571,
                        "The worker hands you some old sea boots."
                    );stage++
                }
            }
            3 -> {
                npc(
                    COUNCIL_WORKER,
                    "You can now use Peer the Seer to deposit your items!"
                ).also { stage = END_DIALOGUE }
            }
        }
    }
}