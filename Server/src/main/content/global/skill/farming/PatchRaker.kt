package content.global.skill.farming

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import org.rs09.consts.Items
import org.rs09.consts.Sounds

object PatchRaker {
    val RAKE_ANIM = getAnimation(2273)

    @JvmStatic
    fun rake(player: Player, patch: FarmingPatch) {
        val p = patch.getPatchFor(player)
        val patchName = p.patch.type.displayName()
        var firstRake = true
        if (!p.isWeedy()) {
            sendMessage(player, "This $patchName doesn't need weeding right now.")
            return
        }
        submitIndividualPulse(player, object : Pulse() {
            override fun pulse(): Boolean {
                var patchStage = patch.getPatchFor(player).getCurrentState()
                if (firstRake || patchStage < 2) {
                    // don't play the animation when on patchStage 2 as it has already
                    // played three times at this point and the patch will be weed-free
                    // after the third play
                    animate(player, RAKE_ANIM)
                    playAudio(player, Sounds.FARMING_RAKING_2442)
                    firstRake = false
                }
                if (delay < 5) {
                    delay = 5
                } else {
                    patch.getPatchFor(player).currentGrowthStage++
                    patch.getPatchFor(player).setCurrentState(++patchStage)
                    addItem(player, Items.WEEDS_6055)
                    rewardXP(player, Skills.FARMING, 4.0)
                }
                if (patchStage >= 3) {
                    resetAnimator(player)
                }
                return patchStage >= 3
            }
        })
    }
}