package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class MortenHoldstromDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MortenHoldstromDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Good day to you.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.ASKING, "Hello. What are you up to?").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.HAPPY, "Ah, today is a surströmming day! The herring I buried six months ago is ready to be dug up.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.DISGUSTED, "Eughr! What are you going to do with it?").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.HAPPY, "Eat it, of course! It will be fermented just-right by now.").also { stage++ }
            4 -> playerl(core.game.dialogue.FacialExpression.ASKING, "Fermented? You eat rotten fish?").also { stage++ }
            5 -> npcl(core.game.dialogue.FacialExpression.HAPPY, "Hmmm, tasty. I'm guessing you don't want to come round and try it?").also { stage++ }
            6 -> playerl(core.game.dialogue.FacialExpression.ANNOYED, "You guess correctly.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MORTEN_HOLDSTROM_5510)
    }

}