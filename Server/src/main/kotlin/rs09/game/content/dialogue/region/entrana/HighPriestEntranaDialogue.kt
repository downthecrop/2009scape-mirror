package rs09.game.content.dialogue.region.entrana

import api.addItemOrDrop
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class HighPriestEntranaDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (!player.questRepository.isComplete("Heroes' Quest")) {
            npc(FacialExpression.FRIENDLY, "Many greetings. Welcome to our fair island.").also { stage = 10 }
        } else {
            options("Have you seen a pair of ice gloves?", "Ask about Entrana").also { stage = 5 }
        }


        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.FRIENDLY, "and devoted the island to those who wish peace for the world.").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "The inhabitants of this island are mostly monks who spend their time meditating on Saradomin's ways.").also { stage++ }
            3 -> npcl(FacialExpression.FRIENDLY, "Of course, there are now more pilgrims to this holy site, since Saradomin defeated Zamorak in the battle of Lumbridge. It is good that so many see Saradomin's true glory!").also { stage = 99 }

            5 -> when (buttonId) {
                1 -> npcl(FacialExpression.FRIENDLY, "By which you mean the pair of mythical gloves you stole from the cold dead body of the Queen of the Ice?").also { stage = 20 }
                2 -> npcl(FacialExpression.FRIENDLY,"You are standing on the holy island of Entrana. It was here that Saradomin first stepped upon Gielinor. In homage to Saradomin's first arrival, we have built a great church,").also { stage = 1 }
            }

            10 -> npc(FacialExpression.FRIENDLY, "Enjoy our stay here. May it be spiritually uplifting!").also { stage = 99 }

            20 -> player(FacialExpression.SUSPICIOUS, "Er...").also { stage++ }
            21 -> npcl(FacialExpression.ANNOYED, "The gloves that you acquired to aid you in plucking the Entranan firebird? Another victim of your murderous nature, I should add.").also { stage++ }
            22 -> npcl(FacialExpression.ANNOYED, "The ice gloves that, from context, I would assume you have lost, if not carelessly discarded?").also { stage++ }

            23 -> player(FacialExpression.FRIENDLY, "Those ice gloves, yes.").also { stage++ }
            24 -> sendDialogue("The high priest of Entrana shivers as he hands you your lost ice gloves.").also {
                addItemOrDrop(player, Items.ICE_GLOVES_1580, 1)
                stage++
            }
            25 -> npcl(FacialExpression.ANNOYED, "One of my monks found these gloves. If only to spare this world from further carnage, I return them to you.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HighPriestEntranaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HIGH_PRIEST_216)
    }
}
