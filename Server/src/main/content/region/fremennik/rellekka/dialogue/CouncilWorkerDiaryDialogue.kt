package content.region.fremennik.rellekka.dialogue

import core.api.*
import core.game.dialogue.FacialExpression
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType

const val COUNCIL_WORKER = 1287

class CouncilWorkerDiaryDialogue() : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        //todo all this dialogue is inauthentic and sucks, this is just a patch to fix the absolutely garbage and incorrect way of granting the diary rewards that doesn't even give the lamp. -Ceik June 2023
        when (stage) {
            START_DIALOGUE -> {
                player(FacialExpression.FRIENDLY, "About my achievement diary...");stage++
            }
            1 -> {
                if (!AchievementDiary.hasClaimedLevelRewards(player, DiaryType.FREMENNIK, 0)) {
                    AchievementDiary.flagRewarded(player, DiaryType.FREMENNIK, 0)
                    npc(COUNCIL_WORKER,  "You have completed the Fremennik Easy Diary!")
                    stage++
                } else if (AchievementDiary.canReplaceReward(player, DiaryType.FREMENNIK, 0)) {
                    player(FacialExpression.FRIENDLY, "I need a new pair of boots.")
                    stage = 10
                }
                //player.achievementDiaryManager.getDiary(DiaryType.FREMENNIK).setLevelRewarded(0)
            }
            2 -> {
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

            10 -> {
                npc(COUNCIL_WORKER, "Sure!")
                AchievementDiary.grantReplacement(player, DiaryType.FREMENNIK, 0)
                stage = 2
            }
        }
    }
}
