package rs09.game.content.dialogue

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import rs09.tools.START_DIALOGUE

class EmptyPlugin(player: Player? = null,val file: DialogueFile?) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return EmptyPlugin(player,null)
    }

    override fun open(vararg args: Any?): Boolean {
        if(args.isNotEmpty() && args[0] is NPC){
            npc = args[0] as NPC
        }
        stage = START_DIALOGUE
        loadFile(file)
        interpreter.handle(0,0)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(Integer.MAX_VALUE)
    }

}