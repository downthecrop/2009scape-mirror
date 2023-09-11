package content.region.misc.keldagrim.dialogue

import core.api.openInterface
import core.cache.def.impl.NPCDefinition
import core.game.dialogue.DialoguePlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.NPCs

/**
 * File for handling Reinald's dialogue and right-click option
 * @author Ceikry
 */
@Initializable
class ReinaldDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc("Hello, human! Would you like to browse","my little shop of bracelets?").also { stage++ }
            1 -> options("Yes, please!", "No, thanks.").also { stage++ }
            2 -> when(buttonId){
                1 -> end().also { openInterface(player,593) }
                2 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ReinaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REINALD_2194)
    }

}

@Initializable
class ReinaldOptionHandler : OptionHandler(){
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        openInterface(player!!,593)
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.REINALD_2194).handlers["option:change-armguards"] = this
        return this
    }

}