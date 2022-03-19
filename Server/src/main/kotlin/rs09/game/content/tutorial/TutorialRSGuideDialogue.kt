package rs09.game.content.tutorial

import api.setAttribute
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Handles the RuneSccape guide's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialRSGuideDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialRSGuideDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val tutStage = player?.getAttribute("tutorial:stage", 0) ?: 0
        if(tutStage < 2) {
            Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.HALF_GUILTY,
                    "Greetings! Please follow the onscreen",
                    "instructions!"
                )
            )
            return false
        }

        if(tutStage == 2)
        {
            Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.HALF_GUILTY,
                    "Greetings! I see you are a new arrival to this land. My",
                    "job is to welcome all new visitors. So welcome!"
                )
            )
            stage = 0
            return true
        }
        else
        {
            Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.HALF_GUILTY,
                    "Please follow the onscreen instructions!"
                )
            )
            return false
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage)
        {
            0 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "You have already learned the first thing needed to",
                    "succeed in this world: talking to other people!"
                )
            ).also { stage++ }

            1 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "You will find many inhabitants of this world have useful",
                    "things to say to you. By clicking on them with your",
                    "mouse you can talk to them."
                )
            ).also { stage++ }

            2 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "I would also suggest reading through some of the",
                    "supporting information on the website. There you can",
                    "find the starter guides, which contain all the",
                    "additional information you're ever likely to need. they also"
                )
            ).also { stage++ }

            3 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "contain helpful tips to help you on your",
                    "journey."
                )
            ).also { stage++ }

            4 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.FRIENDLY,
                        "To continue the tutorial go through that door over",
                        "there and speak to your first instructor!"
                    )
                ).also { stage++ }

            5 -> {
                end()
                setAttribute(player, "tutorial:stage", 3)
                TutorialStage.load(player, 3)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUNESCAPE_GUIDE_945)
    }
}