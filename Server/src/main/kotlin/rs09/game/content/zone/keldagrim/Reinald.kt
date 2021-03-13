package rs09.game.content.zone.keldagrim

import core.cache.def.impl.NPCDefinition
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

const val REINALD = 2194

/**
 * File for handling Reinald's dialogue and right-click option
 * @author Ceikry
 */
@Initializable
class ReinaldDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage++){
            0 -> options("Yes, please!", "No, thanks.")
            1 -> when(buttonId){
                1 -> end().also { player.interfaceManager.open(Component(593)) }
                2 -> end()
            }
        }
        return true
    }

    override fun open(vararg args: Any?): Boolean {
        npc("Hello, human! Would you like to browse","my little shop of bracelets?")
        stage = 0
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ReinaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(REINALD)
    }

}

@Initializable
class ReinaldOptionHandler : OptionHandler(){
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player?.interfaceManager?.open(Component(593))
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(REINALD).handlers["option:change-armguards"] = this
        return this
    }

}