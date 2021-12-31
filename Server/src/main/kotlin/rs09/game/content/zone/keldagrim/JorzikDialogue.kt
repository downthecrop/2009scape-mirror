package rs09.game.content.zone.keldagrim

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import api.*
import rs09.tools.END_DIALOGUE

const val JORZIK = 2565

/**Just some silly little dialogue for Jorzik hehe
 * @author phil lips*/

@Initializable
class JorzikDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Do you want to trade?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("What are you selling?","No, thanks").also { stage = 10 }
            10 -> when(buttonId){
                1 -> sendPlayerDialogue(player,"What are you selling?").also { stage = 15 }
                2 -> sendPlayerDialogue(player,"No, thanks.").also {stage = 50}
            }
            15 -> sendNPCDialogue(player, JORZIK,"The finest smiths from all over Gielinor come here to work, and I buy the fruit of their craft. Armour made from the strongest metals!").also { stage = 20 }
            20 -> options("Lets have a look, then.","No, thanks").also { stage = 30 }
            30 -> when(buttonId){
                1 -> sendPlayerDialogue(player,"Let's have a look, then.").also { stage = 40 }
                2 -> sendPlayerDialogue(player,"No, thanks.").also {stage = 50}
            }
            40 -> end().also { npc.openShop(player) }
            50 ->sendNPCDialogue(player, JORZIK,"You just don't appreciate the beauty of fine metalwork.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return JorzikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(JORZIK)
    }

}