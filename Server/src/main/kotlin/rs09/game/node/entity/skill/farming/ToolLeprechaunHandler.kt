package rs09.game.node.entity.skill.farming

import core.cache.def.impl.NPCDefinition
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Components
import org.rs09.consts.NPCs

val TL_IDS = arrayOf(NPCs.TOOL_LEPRECHAUN_3021,NPCs.GOTH_LEPRECHAUN_8000,NPCs.TOOL_LEPRECHAUN_4965,NPCs.TECLYN_2861)
@Initializable
class ToolLeprechaunHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(id in TL_IDS){
            val def = NPCDefinition.forId(id)
            def.handlers["option:exchange"] = this
            def.handlers["option:teleport"] = this
        }
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return false
        when(option){
            "exchange" -> player?.interfaceManager?.open(Component(Components.FARMING_TOOLS_125))
            "teleport" -> VinesweeperTeleport.teleport(node!! as NPC, player!!)
        }
        return true
    }

}
