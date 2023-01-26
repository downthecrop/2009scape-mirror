package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class JofridrMordstatterDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return JofridrMordstatterDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Hello there. Would you like to see the goods I have for sale?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please, Jofridr.", "No thank you, Jofridr.", "Why do you have so much wool in your store?").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "Yes please, Jofridr.").also { stage = END_DIALOGUE }.also { npc.openShop(player) }
                2 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "No thank you, Jofridr.").also { stage = 5 }
                3 -> playerl(core.game.dialogue.FacialExpression.THINKING, "Why do you have so much wool in your store? I haven't seen any sheep anywhere.").also { stage = 11}
            }
            5 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Fair thee well.").also { stage = END_DIALOGUE }

            11 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Ah, I have contacts on the mainland. I have a sailor friend who brings me crates of wool on a regular basis.").also { stage++ }
            12 -> playerl(core.game.dialogue.FacialExpression.ASKING,"What do you trade for it?").also { stage++ }
            13 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Rope of course! What else can we sell? Fish would go off before it got so far south.").also { stage++ }
            14 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Where does all this rope go?").also { stage++ }
            15 -> npcl(core.game.dialogue.FacialExpression.THINKING,"Err, I don't remember the name of the place very well. Dreinna? Drennor? Something like that.").also { stage++ }
            16 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL,"That's very interesting. Thanks Jofridr.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOFRIDR_MORDSTATTER_5509)
    }

}