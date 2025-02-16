package content.region.kandarin.ardougne.quest.plaguecity.dialogue.mourners

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * This is the Mourner patrolling close to the wall in E Ardougne
 */
@Initializable
class MournerArdougneWallDialogue(player: Player? = null) : DialoguePlugin(player){

    companion object{
        const val START_DIALOGUE = 0
        const val PLAGUE = 20
        const val SYMPTOMS = 40
    }

    override fun open(vararg args: Any?): Boolean {
        playerl(FacialExpression.NEUTRAL, "Hi.").also { stage = START_DIALOGUE }
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.NEUTRAL, " What are you up to?").also { stage++ }
            START_DIALOGUE + 1 -> playerl(FacialExpression.NEUTRAL, " Just sight-seeing.").also { stage++ }
            START_DIALOGUE + 2 -> npcl(FacialExpression.NEUTRAL, "This is no place for sight-seeing. Don't you know there's been a plague outbreak?").also { stage++ }
            START_DIALOGUE + 3 -> playerl(FacialExpression.NEUTRAL, " Yes, I had heard.").also { stage++ }
            START_DIALOGUE + 4 -> npcl(FacialExpression.NEUTRAL, " Then I suggest you leave as soon as you can.").also { stage++ }
            START_DIALOGUE + 5 -> showTopics(
                Topic("What brought the plague to Ardougne?", PLAGUE),
                Topic("What are the symptoms of the plague?", SYMPTOMS),
                Topic("Thanks for the advice.", END_DIALOGUE),
            )

            PLAGUE -> npcl(FacialExpression.NEUTRAL, " It's all down to King Tyras of West Ardougne. " +
                    "'Rather than protecting his people he spends his time in the lands to the West. ").also { stage++ }
            PLAGUE + 1 -> npcl(FacialExpression.NEUTRAL, "When he returned last he brought the plague with him then left before the problem became serious.").also { stage++ }
            PLAGUE + 2 -> playerl(FacialExpression.ASKING, " Does he know how bad the situation is now?").also { stage++ }
            PLAGUE + 3 -> npcl(FacialExpression.ANGRY, " If he did he wouldn't care. I believe he wants his people to suffer, he's an evil man.").also { stage++ }
            PLAGUE + 4 -> playerl(FacialExpression.EXTREMELY_SHOCKED, " Isn't that treason?").also { stage++ }
            PLAGUE + 5 -> npcl(FacialExpression.DISGUSTED_HEAD_SHAKE, " He's not my king.").also { stage = END_DIALOGUE }

            SYMPTOMS -> npcl(FacialExpression.NEUTRAL, "The first signs are typical flu symptoms. These tend to be followed by severe nightmares, horrifying hallucinations which drive many to madness.").also { stage++ }
            SYMPTOMS + 1 -> playerl(FacialExpression.DISGUSTED, "Sounds nasty.").also { stage++ }
            SYMPTOMS + 2 -> npcl(FacialExpression.NEUTRAL, " It gets worse. Next the victim's blood changes into a thick black tar-like liquid, at this point they're past help.").also { stage++ }
            SYMPTOMS + 3 -> npcl(FacialExpression.NEUTRAL, "Their skin is cold to the touch, the victim is now brain dead. Their body however lives on driven by the virus, roaming like a zombie, spreading itself further wherever possible.").also { stage++ }
            SYMPTOMS + 4 -> playerl(FacialExpression.DISGUSTED_HEAD_SHAKE, " I think I've heard enough.").also { stage = END_DIALOGUE }

        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_719)
    }
}