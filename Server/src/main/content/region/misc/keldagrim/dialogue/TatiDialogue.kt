package content.region.misc.keldagrim.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class TatiDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_ANGRY1, "What'you want?").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Do you have any pickaxes?", 2, true),
                    Topic(FacialExpression.FRIENDLY, "Do you have any quests?", 7, true),
                    Topic(FacialExpression.FRIENDLY, "Nothing really.", 14),
            )
            2 -> playerl(FacialExpression.FRIENDLY, "Do you-").also { stage++ }
            3 -> npcl(FacialExpression.OLD_ANGRY1, "What? Speak up, I can't hear you!").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Uhm... I'm just looking for some pickaxes! Do you have any?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_ANGRY1, "Do I have any pickaxes? Do I? Of course I do, this is a pickaxe shop, isn't it!").also{ stage++ }
            6 -> openNpcShop(player, NPCs.TATI_2160).also {
                stage = END_DIALOGUE
            }
            7 -> playerl(FacialExpression.FRIENDLY, "Do you-").also { stage++ }
            8 -> npcl(FacialExpression.OLD_ANGRY1, "What? Stop mumbling!").also { stage++ }
            9 -> playerl(FacialExpression.FRIENDLY, "I want a quest!").also { stage++ }
            10 -> npcl(FacialExpression.OLD_ANGRY1, "I don't have any lousy quests... I've got someone who's helping me already!").also { stage++ }
            11 -> npcl(FacialExpression.OLD_ANGRY1, "Well, I say helping... he takes his merry time to do his chores, my son does.").also { stage++ }
            12 -> playerl(FacialExpression.FRIENDLY, "Then perhaps I can help in some way?").also { stage++ }
            13 -> npcl(FacialExpression.OLD_ANGRY1, "Pfft, I doubt it... maybe when my son fouls up again, but not now.").also {
                stage = END_DIALOGUE
            }
            14 -> npcl(FacialExpression.OLD_ANGRY1, "Then clear off!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return TatiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TATI_2160)
    }
}