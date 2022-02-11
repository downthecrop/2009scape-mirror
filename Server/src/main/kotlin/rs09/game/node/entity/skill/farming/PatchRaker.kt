package rs09.game.node.entity.skill.farming

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

object PatchRaker {
    val RAKE_ANIM = Animation(2273)

    @JvmStatic
    fun rake(player: Player, patch: FarmingPatch){
        player.pulseManager.run(object : Pulse(){
            override fun pulse(): Boolean {
                var patchStage = patch.getPatchFor(player).getCurrentState()
                if(patchStage <= 2){
                    player.animator.animate(RAKE_ANIM)
                    player.audioManager.send(2442)                         
                }
                if(delay < 5) {
                    delay = 5
                } else {
                    patch.getPatchFor(player).currentGrowthStage++
                    patch.getPatchFor(player).setCurrentState(++patchStage)
                    player.inventory.add(Item(Items.WEEDS_6055))
                    player.skills.addExperience(Skills.FARMING,4.0)
                }
                return patchStage >= 3
            }
        })
    }
}