package rs09.game.content.dialogue.region.alkharid

import api.animate
import api.getDynLevel
import api.getStatLevel
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Animations
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class JaraahDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return JaraahDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY, "Hi!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npcl(FacialExpression.ANNOYED, "What? Can't you see I'm busy?!").also { stage++ }
            1 -> showTopics(
                Topic("Can you heal me?", 101),
                Topic("You must see some gruesome things?", 201),
                Topic("Why do they call you 'The Butcher'?", 301)
            )

            // Jaraah has to be special and have different dialogue for the heal listener so I am repeating it here.
            101 -> {
                animate(npc!!, Animations.HUMAN_PICKPOCKETING_881)
                if(player!!.skills.lifepoints < getStatLevel(player!!, Skills.HITPOINTS)) {
                    player!!.skills.heal(21)
                    npcl(FacialExpression.FRIENDLY, "There you go!")
                } else {
                    npcl(FacialExpression.FRIENDLY, "Okay, this will hurt you more than it will me.")
                }
                stage = END_DIALOGUE
            }

            201 -> npcl(FacialExpression.FRIENDLY, "It's a gruesome business and with the tools they give me it gets mroe gruesome before it gets better!").also { stage = END_DIALOGUE }

            301 -> npcl(FacialExpression.HALF_THINKING, "'The Butcher'?").also { stage++ }
            302 -> npcl(FacialExpression.LAUGH, "Ha!").also { stage++ }
            303 -> npcl(FacialExpression.HALF_ASKING, "Would you like me to demonstrate?").also { stage++ }
            304 -> player(FacialExpression.AFRAID, "Er...I'll give it a miss, thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JARAAH_962)
    }
}