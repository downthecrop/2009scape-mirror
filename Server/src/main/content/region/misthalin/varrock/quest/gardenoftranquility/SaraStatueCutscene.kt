package content.region.misthalin.varrock.quest.gardenoftranquility

import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player

class SaraStatueCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0,0,0))
        loadRegion(11828)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            1 -> {
                teleport(player, 20, 53)
                moveCamera(17, 49, 500)
                rotateCamera(21, 53)
                timedUpdate(5)
            }

            2 -> {
                fadeFromBlack()
                playerDialogueUpdate(FacialExpression.SILENT, "(Hmm ... better not let those guards see me...)")
            }

            3 -> end {
                GuardCutscene(player).start()
            }
        }
    }
}
