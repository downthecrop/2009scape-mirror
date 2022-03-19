package rs09.game.content.tutorial

import api.getAttribute
import api.setAttribute
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Handles the prayer guide's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialPrayerDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialPrayerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            60 -> npcl(FacialExpression.FRIENDLY, "Greetings! I'd just like to briefly go over two topics with you: prayer, and friends.")
            62 -> npcl(FacialExpression.FRIENDLY, "Prayers have all sorts of wonderful benefits! From boosting defence and damage, to protecting you from outside damage, to saving items on death!")
            65 -> npcl(FacialExpression.FRIENDLY, "For your friend and ignore lists, it's quite simple really! Use your friend list to keep track of players who you like, and ignore those you don't!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            60 -> when(stage++){
                0 -> playerl(FacialExpression.FRIENDLY, "Alright, sounds fun!")
                1 -> npcl(FacialExpression.FRIENDLY, "Right, so first thing: prayer. Prayer is trained by offering bones to the gods, and can grant you many boons!")
                2 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 61)
                    TutorialStage.load(player, 61)
                }
            }

            62 -> when(stage++){
                0 -> playerl(FacialExpression.AMAZED, "Very cool!")
                1 -> npcl(FacialExpression.FRIENDLY, "Next up, let's talk about friends.")
                2 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 63)
                    TutorialStage.load(player, 63)
                }
            }

            65 -> {
                end()
                setAttribute(player, "tutorial:stage", 66)
                TutorialStage.load(player, 66)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_BRACE_954)
    }
}