package content.region.kandarin.dialogue

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
 * Represents the dialogue plugin used for Otto
 * @author vddCore
 */
@Initializable
class OttoGodblessedDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
        return OttoGodblessedDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        npcl(
            core.game.dialogue.FacialExpression.HAPPY,
            "Good day, you seem a hearty warrior. "
            + "Maybe even some barbarian blood in that body of yours?"
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
                "I can teach you how to fish."
            ).also { stage++ }

            2 -> playerl(
                core.game.dialogue.FacialExpression.HALF_ROLLING_EYES,
                "Oh, that's pretty underwhelming. But uhhh, okay!"
            ).also { stage++ }

            3 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Alright, so here's what you gotta do: you need to grab a pole and some bait, and then... "
                + "Fling it into the water!"
            ).also { stage++ }

            4 -> playerl(
                core.game.dialogue.FacialExpression.HALF_ASKING,
                "The whole... Pole?"
            ).also { stage++ }

            5 -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "No, not the whole pole!"
            ).also { stage++ }

            6 -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "Look. Just... Grab the pole from under my bed and go click on that fishing spot."
            ).also { stage++ }

            7 -> playerl(
                core.game.dialogue.FacialExpression.STRUGGLE,
                "...click?"
            ).also { stage++ }

            8 -> npcl(
                core.game.dialogue.FacialExpression.FURIOUS,
                "JUST GO DO IT!"
            ).also {
                stage = END_DIALOGUE;
                player.setAttribute("/save:barbtraining:fishing", true)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OTTO_GODBLESSED_2725)
}