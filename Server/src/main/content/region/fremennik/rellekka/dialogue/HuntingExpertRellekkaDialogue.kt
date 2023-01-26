package content.region.fremennik.rellekka.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Represents the dialogue plugin used for the Hunting Expert in the Rellekkan Hunter area
 * @author vddcore
 */
@Initializable
class HuntingExpertRellekkaDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
        return HuntingExpertRellekkaDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        npcl(
            core.game.dialogue.FacialExpression.HAPPY,
            "Good day, you seem to have a keen eye. "
            + "Maybe even some hunter's blood in that body of yours?"
        )

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING, "Is there anything you can teach me?", 1),
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "Nevermind.", END_DIALOGUE)
            )

            1 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "I can teach you how to hunt."
            ).also { stage++ }

            2 -> playerl(
                core.game.dialogue.FacialExpression.THINKING,
                "What kind of creatures can I hunt?"
            ).also { stage++ }

            3 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Many creatures in many ways. You need to make some traps "
                + "and catch birds!"
            ).also { stage++ }

            4 -> playerl(
                core.game.dialogue.FacialExpression.HALF_ASKING,
                "Birds?"
            ).also { stage++ }

            5 -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "Yes, birds! Like the ones here!"
            ).also { stage++ }

            6 -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "Look. Just... Get some hunting gear and go set up some traps."
            ).also { stage++ }

            7 -> playerl(
                core.game.dialogue.FacialExpression.HALF_ROLLING_EYES,
                "Is that it?"
            ).also { stage++ }

            8 -> npcl(
                core.game.dialogue.FacialExpression.FURIOUS,
                "JUST GO DO IT!"
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray
        = intArrayOf(NPCs.HUNTING_EXPERT_5112)
}