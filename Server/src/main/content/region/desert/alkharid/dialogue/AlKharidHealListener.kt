package content.region.desert.alkharid.dialogue

import core.api.*
import core.game.node.entity.skill.Skills
import org.rs09.consts.Animations
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE

/**
 * @author bushtail
 */

class AlKharidHealListener : InteractionListener {
    override fun defineListeners() {
        on(getIds(), IntType.NPC, "heal") { player, node ->
            openDialogue(player, AlKharidHealDialogue(false), node.asNpc())
            return@on true
        }
    }

    fun getIds() : IntArray {
        return intArrayOf(
            NPCs.AABLA_959,
            NPCs.SABREEN_960,
            NPCs.SURGEON_GENERAL_TAFANI_961,
            NPCs.JARAAH_962
        )
    }
}

class AlKharidHealDialogue(val skipFirst: Boolean) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        if (stage == 0 && skipFirst) stage++
        when(stage) {
            0 -> playerl(FacialExpression.ASKING, "Can you heal me?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Of course!").also { stage++ }
            2 -> {
                animate(npc!!, Animations.HUMAN_PICKPOCKETING_881)
                if(player!!.skills.lifepoints < getStatLevel(player!!, Skills.HITPOINTS)) {
                    player!!.skills.heal(21)
                    npcl(FacialExpression.FRIENDLY, "There you go!")
                } else {
                    npcl(FacialExpression.FRIENDLY, "You look healthy to me!")
                }
                stage = END_DIALOGUE
            }
        }
    }
}