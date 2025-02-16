package content.region.kandarin.ardougne.quest.plaguecity.dialogue.mourners

import content.data.Quests
import core.api.getQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * This is for the mourner wandering around Edmonds house
 */

@Initializable
class MournerEdmondHouseDialogue(player: Player? = null) : DialoguePlugin(player){

    companion object {
        const val BEFORE_PLAGUE_CITY = 10
        const val BEFORE_GAS_MASK = 20
        const val BEFORE_SOFTEN_GROUND = 30
        const val SYMPTOMS1 = 40
        const val FEEL_FINE = 50
        const val PLAGUE_SOURCE1 = 60
        const val BEFORE_DIG = 70
        const val AFTER_DIG = 80
        const val AFTER_ENTER_W_ARDOUGNE = 90
    }

    override fun open(vararg args: Any?): Boolean {
        when (getQuestStage(player, Quests.PLAGUE_CITY)){
            0 -> playerl(FacialExpression.NEUTRAL, "Hello there.").also { stage = BEFORE_PLAGUE_CITY }
            1 -> playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = BEFORE_GAS_MASK }
            2 -> playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = BEFORE_SOFTEN_GROUND }
            3 -> playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = BEFORE_DIG }
            4 -> playerl(FacialExpression.NEUTRAL, "Hello there.").also { stage = AFTER_DIG }
            else -> playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = AFTER_ENTER_W_ARDOUGNE }
        }
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            BEFORE_PLAGUE_CITY -> npcl(FacialExpression.NEUTRAL, "Do you have a problem traveller?").also { stage++ }
            BEFORE_PLAGUE_CITY + 1 -> playerl(FacialExpression.ASKING, "No, I just wondered why you're wearing that outfit... Is it fancy dress?").also { stage++ }
            BEFORE_PLAGUE_CITY + 2 -> npcl(FacialExpression.NEUTRAL, "No! It's for protection.").also { stage++ }
            BEFORE_PLAGUE_CITY + 3 -> playerl(FacialExpression.ASKING, "Protection from what?").also { stage++ }
            BEFORE_PLAGUE_CITY + 4 -> npcl(FacialExpression.NEUTRAL, "The plague of course...").also { stage = END_DIALOGUE }

            BEFORE_GAS_MASK -> npcl(FacialExpression.NEUTRAL, "What do you want?").also { stage++ }
            BEFORE_GAS_MASK + 1 -> showTopics(
                Topic("Who are you?", BEFORE_GAS_MASK + 3),
                Topic("Nothing, just being polite.", BEFORE_GAS_MASK + 2)
            )
            BEFORE_GAS_MASK + 2 -> npcl(FacialExpression.NEUTRAL, "Hmmm, ok then. Be on your way.").also { stage = END_DIALOGUE }
            BEFORE_GAS_MASK + 3 -> npcl(FacialExpression.NEUTRAL, "I'm a mourner. It's my job to help heal the plague victims of West Ardougne and to make sure the disease is contained.").also { stage++ }
            BEFORE_GAS_MASK + 4 -> playerl(FacialExpression.THINKING, "Very noble of you.").also { stage++ }
            BEFORE_GAS_MASK + 5 -> npcl(FacialExpression.NEUTRAL, "If you come down with any symptoms such as flu or nightmares let me know immediately.").also { stage = END_DIALOGUE }

            BEFORE_SOFTEN_GROUND -> npcl(FacialExpression.NEUTRAL, "Are you ok?").also { stage++ }
            BEFORE_SOFTEN_GROUND + 1 -> playerl(FacialExpression.NEUTRAL, "Yes, I'm fine thanks.").also { stage++ }
            BEFORE_SOFTEN_GROUND + 2 -> npcl(FacialExpression.NEUTRAL, "Have you experienced any plague symptoms?").also { stage++ }
            BEFORE_SOFTEN_GROUND + 3 -> showTopics(
                Topic("What are the symptoms?", SYMPTOMS1),
                Topic("No, I feel fine", FEEL_FINE),
                Topic("No, but can you tell me where the plague came from?", PLAGUE_SOURCE1)
            )

            SYMPTOMS1 -> npcl(FacialExpression.NEUTRAL, "First you'll come down with heavy flu, this is usually followed by horrifying nightmares.").also { stage++ }
            SYMPTOMS1 + 1 -> playerl(FacialExpression.HALF_WORRIED, "I used to have nightmares when I was younger.").also { stage++ }
            SYMPTOMS1 + 2 -> npcl(FacialExpression.NEUTRAL, "Not like these I assure you. Soon after a thick black liquid will seep from your nose and eyes.").also { stage++ }
            SYMPTOMS1 + 3 -> playerl(FacialExpression.HALF_WORRIED, "Yuck!").also { stage++ }
            SYMPTOMS1 + 4 -> npcl(FacialExpression.HALF_WORRIED, "When it gets to that stage there's nothing we can do for you.").also { stage = END_DIALOGUE }

            FEEL_FINE -> npcl(FacialExpression.NEUTRAL, "Well if you take a turn for the worse let me know straight away.").also { stage++ }
            FEEL_FINE + 1 -> playerl(FacialExpression.HALF_WORRIED, "Can you cure it then?").also { stage++ }
            FEEL_FINE + 2 -> npcl(FacialExpression.NEUTRAL, "No... But you will have to be treated.").also { stage++ }
            FEEL_FINE + 3 -> playerl(FacialExpression.WORRIED, "Treated?").also { stage++ }
            FEEL_FINE + 4 -> npcl(FacialExpression.NEUTRAL, "We have to take measures to contain the disease. " +
                    "That's why you must let us know immediately if you take a turn for the worse.").also { stage = END_DIALOGUE }

            PLAGUE_SOURCE1 -> npcl(FacialExpression.NEUTRAL, "It all started when King Tyras of West Ardougne came back from one of his visits to the lands west of here.").also { stage++ }
            PLAGUE_SOURCE1 + 1 -> npcl(FacialExpression.NEUTRAL, "Some of his men must have unknowingly caught it out there and brought it back with them").also { stage = END_DIALOGUE }

            BEFORE_DIG -> npcl(FacialExpression.ASKING, "What are you up to with old man Edmond?").also { stage++ }
            BEFORE_DIG + 1 -> playerl(FacialExpression.HALF_GUILTY, "Nothing, we've just been chatting.").also { stage++ }
            BEFORE_DIG + 2 -> npcl(FacialExpression.ASKING, "What about his daughter?").also { stage++ }
            BEFORE_DIG + 3 -> playerl(FacialExpression.HALF_GUILTY, "Oh, you know about that then?").also { stage++ }
            BEFORE_DIG + 4 -> npcl(FacialExpression.NEUTRAL, "We know about everything that goes on in Ardougne. We have to if we are to contain the plague.").also { stage++ }
            BEFORE_DIG + 5 -> playerl(FacialExpression.HALF_ASKING, "Have you see his daughter recently?").also { stage++ }
            BEFORE_DIG + 6 -> npcl(FacialExpression.NEUTRAL, "I imagine she's caught the plague. Either way she won't be allowed out of West Ardougne, the risk is too great.").also { stage = END_DIALOGUE }

            AFTER_DIG -> npcl(FacialExpression.ASKING, "Been digging have we?").also { stage++ }
            AFTER_DIG + 1 -> playerl(FacialExpression.HALF_GUILTY, "What do you mean?").also { stage++ }
            AFTER_DIG + 2 -> npcl(FacialExpression.NEUTRAL, "Your hands are covered in mud.").also { stage++ }
            AFTER_DIG + 3 -> playerl(FacialExpression.HALF_GUILTY, "Oh that...").also { stage++ }
            AFTER_DIG + 4 -> npcl(FacialExpression.THINKING, "Funny, you don't look like the gardening type.").also { stage++ }
            AFTER_DIG + 5 -> playerl(FacialExpression.NEUTRAL, "Oh no, I love gardening! It's my favorite pastime.").also { stage = END_DIALOGUE }

            AFTER_ENTER_W_ARDOUGNE -> npcl(FacialExpression.ASKING, " What are you up to?").also { stage++ }
            AFTER_ENTER_W_ARDOUGNE + 1 -> playerl(FacialExpression.HALF_GUILTY, "Nothing.").also { stage++ }
            AFTER_ENTER_W_ARDOUGNE + 2 -> npcl(FacialExpression.SUSPICIOUS, "I don't trust you.").also { stage++ }
            AFTER_ENTER_W_ARDOUGNE + 3 -> playerl(FacialExpression.NEUTRAL, "You don't have to.").also { stage++ }
            AFTER_ENTER_W_ARDOUGNE + 4 -> npcl(FacialExpression.SUSPICIOUS, "If I find you attempting to cross the wall I'll make sure you never return.").also { stage = END_DIALOGUE }

        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_718)
    }

}