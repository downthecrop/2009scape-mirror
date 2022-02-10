package rs09.game.content.quest.members.monksfriend


import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable


@Initializable
/**
* Handles MonasteryMonkDialogue Dialogue
* @author Kya
*/
class MonasteryMonkDialogue(player: Player? = null): DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return MonasteryMonkDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage("Monk's Friend") ?: -1

        when(qstage) {
            0  -> npcl(FacialExpression.NEUTRAL,"Peace brother.").also { stage = 1000 }              // First dialogue
            10 -> npcl(FacialExpression.FRIENDLY,"*yawn*.").also { stage = 1000 }                  // Finding blanket
            20 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also { stage = 1000 }                   // Talk to him again
            30 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = 1000}                      // Haven't found Cedric
            40 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = 1000}                      // Haven't given Cedric water
            41 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = 1000}                      // Haven't completed dialogue
            42 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = 1000}                      // Haven't given Cedric logs
            50 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = 1000}                      // Helped Cedric
            100 -> npcl(FacialExpression.HAPPY,"Can't wait for the party!").also{stage = 1000}       // After quest complete
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(281)
    }

}
