package rs09.game.content.tutorial

import api.getAttribute
import api.setAttribute
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.world.GameWorld.settings

/**
 * Handles the finance tutor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialFinanceAdvisorDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialFinanceAdvisorDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            58 -> playerl(FacialExpression.FRIENDLY, "Hello, who are you?")
            59 -> npcl(FacialExpression.FRIENDLY, "Move along, now.").also { return false }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0)){
            58 -> when(stage++){
                0 -> npcl(FacialExpression.FRIENDLY, "I'm the Financial Advisor. I'm here to tell people how to make money.")
                1 -> playerl(FacialExpression.FRIENDLY, "Okay. How can I make money then?")
                2 -> npcl(FacialExpression.HALF_THINKING, "How you can make money? Quite.")
                3 -> npcl(FacialExpression.FRIENDLY, "Well there are three basic ways of making money here: combat, quests, and trading. I will talk you through each of them very quickly.")
                4 -> npcl(FacialExpression.FRIENDLY, "Let's start with combat as it is probably still fresh in your mind. Many enemies, both human and monster will drop items when they die.")
                5 -> npcl(FacialExpression.FRIENDLY, "Now, the next way to earn money quickly is by quests. Many people on " + settings!!.name + " have things they need doing, which they will reward you for.")
                6 -> npcl(FacialExpression.FRIENDLY, "By getting a high level in skills such as Cooking, Mining, Smithing or Fishing, you can create or catch your own items and sell them for pure profit.")
                7 -> npcl(FacialExpression.FRIENDLY, "Lastly, we have jobs you can get from tutors in Lumbridge. These pay very handsomely early on!").also { stage++ }
                8 -> npcl(FacialExpression.FRIENDLY, "Well, that about covers it. Move along now.")
                9 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 59)
                    TutorialStage.load(player, 59)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FINANCIAL_ADVISOR_947)
    }

}