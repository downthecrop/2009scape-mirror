package content.global.skill.summoning.familiar

import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.NPCs;

/**
 * Represents the plugin used to handle the spirit graahk familiar
 * @author Splinter
 */
@Initializable
class SpiritGraahkOptionPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        NPCDefinition.forId(NPCs.SPIRIT_GRAAHK_7363).getHandlers()["option:interact"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String?): Boolean {
        player.dialogueInterpreter.open(NPCs.SPIRIT_GRAAHK_7364, node.asNpc())
        return true
    }
}
