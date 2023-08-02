package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Jorzik dialogue.
 * @author phil lips
 */

@Initializable
class JorzikDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npc(FacialExpression.OLD_DEFAULT,"Do you want to trade?").also { stage++ }
            1 -> showTopics(
                Topic(FacialExpression.FRIENDLY,"What are you selling?",2),
                Topic(FacialExpression.FRIENDLY,"No, thanks.", 5)
            )
            2 -> npcl(FacialExpression.OLD_DEFAULT,"The finest smiths from all over Gielinor come here to work, and I buy the fruit of their craft. Armour made from the strongest metals!").also { stage++ }
            3 -> showTopics(
                    Topic(FacialExpression.FRIENDLY,"Let's have a look, then.",4),
                    Topic(FacialExpression.FRIENDLY,"No, thanks.", 5)
            )
            4 -> end().also { npc.openShop(player) }
            5 -> npcl(FacialExpression.OLD_DEFAULT,"You just don't appreciate the beauty of fine metalwork.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return JorzikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JORZIK_2565)
    }

}