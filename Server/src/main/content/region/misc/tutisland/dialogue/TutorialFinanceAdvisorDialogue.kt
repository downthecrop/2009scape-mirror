package content.region.misc.tutisland.dialogue

import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import content.region.misc.tutisland.handlers.TutorialStage
import core.game.world.GameWorld.settings

/**
 * Handles the finance tutor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialFinanceAdvisorDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return TutorialFinanceAdvisorDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            58 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello, who are you?")
            59 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Move along, now.").also { return false }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0)){
            58 -> when(stage++){
                0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I'm the Financial Advisor. I'm here to tell people how to make money.")
                1 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Okay. How can I make money then?")
                2 -> npcl(core.game.dialogue.FacialExpression.HALF_THINKING, "How you can make money? Quite.")
                3 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well there are three basic ways of making money here: combat, quests, and trading. I will talk you through each of them very quickly.")
                4 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Let's start with combat as it is probably still fresh in your mind. Many enemies, both human and monster will drop items when they die.")
                5 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Now, the next way to earn money quickly is by quests. Many people on " + settings!!.name + " have things they need doing, which they will reward you for.")
                6 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "By getting a high level in skills such as Cooking, Mining, Smithing or Fishing, you can create or catch your own items and sell them for pure profit.")
                7 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Lastly, we have jobs you can get from tutors in Lumbridge. These pay very handsomely early on!").also { stage++ }
                8 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well, that about covers it. Move along now.")
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