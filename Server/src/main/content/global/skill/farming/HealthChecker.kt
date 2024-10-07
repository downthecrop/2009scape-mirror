package content.global.skill.farming

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Log
import java.util.concurrent.TimeUnit

@Initializable
class HealthChecker : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("check-health", this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val fPatch = FarmingPatch.forObject(node.asScenery())
        fPatch ?: return false
        val patch = fPatch.getPatchFor(player)
        val type = patch.patch.type

        if (type != PatchType.BUSH_PATCH && type != PatchType.FRUIT_TREE_PATCH && type != PatchType.TREE_PATCH && type != PatchType.CACTUS_PATCH) {
            sendMessage(player, "This shouldn't be happening. Please report this.")
            return true
        }

        if (!patch.isCheckHealth) return true

        rewardXP(player, Skills.FARMING, patch.plantable?.checkHealthXP ?: 0.0)
        patch.isCheckHealth = false
        when (type) {
            PatchType.TREE_PATCH -> {
                patch.setCurrentState(patch.getCurrentState() + 1)
                sendMessage(player, "You examine the tree for signs of disease and find that it is in perfect health.")
            }
            PatchType.FRUIT_TREE_PATCH -> {
                patch.setCurrentState(patch.getCurrentState() - 14)
                sendMessage(player, "You examine the tree for signs of disease and find that it is in perfect health.")
            }
            PatchType.BUSH_PATCH -> {
                patch.setCurrentState(patch.plantable!!.value + patch.plantable!!.stages + 4)
                sendMessage(player, "You examine the bush for signs of disease and find that it's in perfect health.")
            }
            PatchType.CACTUS_PATCH -> {
                patch.setCurrentState(patch.plantable!!.value + patch.plantable!!.stages + 3)
                sendMessage(player, "You examine the cactus for signs of disease and find that it is in perfect health.")
            }
            else -> log(this::class.java, Log.ERR, "Unreachable patch type from when(type) switch in HealthChecker.kt")
        }

        if (type == PatchType.FRUIT_TREE_PATCH) {
            patch.nextGrowth = TimeUnit.MINUTES.toMillis(45)
        }

        patch.update()

        return true
    }

}