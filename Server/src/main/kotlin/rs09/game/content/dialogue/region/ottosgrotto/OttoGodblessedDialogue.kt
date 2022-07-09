package rs09.game.content.dialogue.region.ottosgrotto

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Represents the dialogue plugin used for Otto
 * @author vddCore
 */
@Initializable
class OttoGodblessedDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return OttoGodblessedDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        npcl(
            FacialExpression.HAPPY,
            "Good day, you seem a hearty warrior. "
            + "Maybe even some barbarian blood in that body of yours?"
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
                "I can teach you how to fish."
            ).also { stage++ }

            2 -> playerl(
                FacialExpression.HALF_ROLLING_EYES,
                "Oh, that's pretty underwhelming. But uhhh, okay!"
            ).also { stage++ }

            3 -> npcl(
                FacialExpression.FRIENDLY,
                "Alright, so here's what you gotta do: you need to grab a pole and some bait, and then... "
                + "Fling it into the water!"
            ).also { stage++ }

            4 -> playerl(
                FacialExpression.HALF_ASKING,
                "The whole... Pole?"
            ).also { stage++ }

            5 -> npcl(
                FacialExpression.ANNOYED,
                "No, not the whole pole!"
            ).also { stage++ }

            6 -> npcl(
                FacialExpression.ANNOYED,
                "Look. Just... Grab the pole from under my bed and go click on that fishing spot."
            ).also { stage++ }

            7 -> playerl(
                FacialExpression.STRUGGLE,
                "...click?"
            ).also { stage++ }

            8 -> npcl(
                FacialExpression.FURIOUS,
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