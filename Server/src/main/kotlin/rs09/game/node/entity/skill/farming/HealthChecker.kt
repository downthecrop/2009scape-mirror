package rs09.game.node.entity.skill.farming

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import rs09.game.system.SystemLogger
import core.plugin.Initializable
import core.plugin.Plugin
import java.util.concurrent.TimeUnit

@Initializable
class HealthChecker : OptionHandler(){
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("check-health",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val fPatch = FarmingPatch.forObject(node.asScenery())
        fPatch ?: return false
        val patch = fPatch.getPatchFor(player)
        val type = patch.patch.type

        if(type != PatchType.BUSH && type != PatchType.FRUIT_TREE && type != PatchType.TREE && type != PatchType.CACTUS){
            player.sendMessage("This shouldn't be happening. Please report this.")
            return true
        }

        if(!patch.isCheckHealth) return true

        player.skills.addExperience(Skills.FARMING,patch.plantable?.checkHealthXP ?: 0.0)
        patch.isCheckHealth = false
        when(type){
            PatchType.TREE -> patch.setCurrentState(patch.getCurrentState() + 1)
            PatchType.FRUIT_TREE -> patch.setCurrentState(patch.getCurrentState() - 14)
            PatchType.BUSH -> patch.setCurrentState(patch.plantable!!.value + patch.plantable!!.stages + 4)
            PatchType.CACTUS -> patch.setCurrentState(patch.plantable!!.value + patch.plantable!!.stages + 3)
            else -> SystemLogger.logErr("Unreachable patch type from when(type) switch in HealthChecker.kt line 36")
        }

        if(type == PatchType.FRUIT_TREE){
            patch.nextGrowth = TimeUnit.MINUTES.toMillis(45)
        }

        patch.update()

        return true
    }

}