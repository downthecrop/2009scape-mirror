package content.region.misc.tutisland.dialogue

import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import content.region.misc.tutisland.handlers.TutorialStage

/**
 * Handles the prayer guide's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialPrayerDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return TutorialPrayerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            60 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Greetings! I'd just like to briefly go over two topics with you: prayer, and friends.")
            62 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Prayers have all sorts of wonderful benefits! From boosting defence and damage, to protecting you from outside damage, to saving items on death!")
            65 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "For your friend and ignore lists, it's quite simple really! Use your friend list to keep track of players who you like, and ignore those you don't!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            60 -> when(stage++){
                0 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Alright, sounds fun!")
                1 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Right, so first thing: prayer. Prayer is trained by offering bones to the gods, and can grant you many boons!")
                2 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 61)
                    TutorialStage.load(player, 61)
                }
            }

            62 -> when(stage++){
                0 -> playerl(core.game.dialogue.FacialExpression.AMAZED, "Very cool!")
                1 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Next up, let's talk about friends.")
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