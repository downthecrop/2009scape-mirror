package rs09.game.node.entity.skill.farming

import core.cache.def.impl.ObjectDefinition
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
        ObjectDefinition.setOptionHandler("check-health",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val fPatch = FarmingPatch.forObject(node.asObject())
        fPatch ?: return false
        val patch = fPatch.getPatchFor(player)
        val type = patch.patch.type

        if(type != PatchType.BUSH && type != PatchType.FRUIT_TREE && type != PatchType.TREE){
            player.sendMessage("This shouldn't be happening. Please report this.")
            return true
        }

        player.skills.addExperience(Skills.FARMING,patch.plantable?.checkHealthXP ?: 0.0)
        when(type){
            PatchType.TREE -> patch.setCurrentState(patch.getCurrentState() + 1)
            PatchType.FRUIT_TREE -> patch.setCurrentState(patch.getCurrentState() - 14)
            PatchType.BUSH -> patch.setCurrentState(patch.plantable!!.value + patch.plantable!!.stages + 4)
            else -> SystemLogger.logErr("Unreachable patch type from when(type) switch in HealthChecker.kt line 36")
        }

        if(type == PatchType.FRUIT_TREE){
            patch.nextGrowth = TimeUnit.MINUTES.toMillis(45)
        }

        patch.isCheckHealth = false

        return true
    }

}