package content.global.skill.farming

import content.data.skill.SkillingTool
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs09.consts.Sounds

@Initializable
class FruitTreeChopper : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("chop-down",this)
        SceneryDefinition.setOptionHandler("chop down",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false

        val fPatch = FarmingPatch.forObject(node.asScenery())
        fPatch ?: return false

        val patch = fPatch.getPatchFor(player)

        val plantable = patch.plantable
        plantable ?: return false

        val animation = SkillingTool.getHatchet(player).animation

        submitIndividualPulse(player, object : Pulse(animation.duration) {
            override fun pulse(): Boolean {
                animate(player, animation)
                val soundIndex = RandomFunction.random(0, woodcuttingSounds.size)
                playAudio(player, woodcuttingSounds[soundIndex])
                patch.setCurrentState(patch.getCurrentState() + 19)
                sendMessage(player, "You chop down the ${plantable.displayName.lowercase().removeSuffix(" sapling")}.")
                return true
            }
        })
        return true
    }

    private val woodcuttingSounds = intArrayOf(
        Sounds.WOODCUTTING_HIT_3038,
        Sounds.WOODCUTTING_HIT_3039,
        Sounds.WOODCUTTING_HIT_3040,
        Sounds.WOODCUTTING_HIT_3041,
        Sounds.WOODCUTTING_HIT_3042
    )
}