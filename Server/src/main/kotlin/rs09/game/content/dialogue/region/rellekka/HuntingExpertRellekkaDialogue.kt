package rs09.game.content.dialogue.region.rellekka

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Represents the dialogue plugin used for the Hunting Expert in the Rellekkan Hunter area
 * @author vddcore
 */
@Initializable
class HuntingExpertRellekkaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return HuntingExpertRellekkaDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        npcl(
            FacialExpression.HAPPY,
            "Good day, you seem to have a keen eye. "
            + "Maybe even some hunter's blood in that body of yours?"
        )

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(FacialExpression.ASKING, "Is there anything you can teach me?", 1),
                Topic(FacialExpression.NEUTRAL, "Nevermind.", END_DIALOGUE)
            )

            1 -> npcl(
                FacialExpression.FRIENDLY,
                "I can teach you how to hunt."
            ).also { stage++ }

            2 -> playerl(
                FacialExpression.THINKING,
                "What kind of creatures can I hunt?"
            ).also { stage++ }

            3 -> npcl(
                FacialExpression.FRIENDLY,
                "Many creatures in many ways. You need to make some traps "
                + "and catch birds!"
            ).also { stage++ }

            4 -> playerl(
                FacialExpression.HALF_ASKING,
                "Birds?"
            ).also { stage++ }

            5 -> npcl(
                FacialExpression.ANNOYED,
                "Yes, birds! Like the ones here!"
            ).also { stage++ }

            6 -> npcl(
                FacialExpression.ANNOYED,
                "Look. Just... Get some hunting gear and go set up some traps."
            ).also { stage++ }

            7 -> playerl(
                FacialExpression.HALF_ROLLING_EYES,
                "Is that it?"
            ).also { stage++ }

            8 -> npcl(
                FacialExpression.FURIOUS,
                "JUST GO DO IT!"
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray
        = intArrayOf(NPCs.HUNTING_EXPERT_5112)
}