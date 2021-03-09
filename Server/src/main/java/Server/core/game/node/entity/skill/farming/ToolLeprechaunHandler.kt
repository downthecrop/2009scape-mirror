package core.game.node.entity.skill.farming

import core.cache.def.impl.NPCDefinition
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Components

val TL_IDS = arrayOf(3021,8000,4965)
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
            "exchange" -> player?.interfaceManager?.open(Component(Components.farming_tools_125))
            "teleport" -> player?.dialogueInterpreter?.sendDialogues(node.id,if(node.id == 4965) FacialExpression.FRIENDLY else FacialExpression.OLD_NORMAL, "I forgot how to do that it seems.")
        }
        return true
    }

}