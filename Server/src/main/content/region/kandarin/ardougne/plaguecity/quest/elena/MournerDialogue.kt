package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager.getObject
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class MournerDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.MOURNER_3216)
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            in 0..6 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "What are you up to with old man Edmond?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Nothing, we've just been chatting.").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "What about his daughter?").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "you know about that then?").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "We know about everything that goes on in Ardougne. We have to if we are to contain the plague.").also { stage++ }
                6 -> playerl(FacialExpression.FRIENDLY, "Have you see his daughter recently?").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "I imagine she's caught the plague. Either way she won't be allowed out of West Ardougne, the risk is too great.").also { stage == END_DIALOGUE }
            }

            7 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "Been digging have we?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "What do you mean?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Your hands are covered in mud.Player: Oh that...").also { stage++ }
                4 -> npcl(FacialExpression.NEUTRAL, "Funny, you don't look like the gardening type.").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "Oh no, I love gardening! It's my favorite pastime.").also { stage = END_DIALOGUE }
            }

            8 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "Do you have a problem traveller?").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "No, I just wondered why you're wearing that outfit... Is it fancy dress?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "No! It's for protection.").also { stage++ }
                4 -> playerl(FacialExpression.NEUTRAL, "Protection from what?").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, "The plague of course...").also { stage = END_DIALOGUE }
            }

            in 9..15 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "Can I help you?").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "What are you doing?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "I'm guarding the border to West Ardougne. No-one except we mourners can pass through.").also { stage++ }
                4 -> playerl(FacialExpression.NEUTRAL, "Why?").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, "The plague of course. We can't risk cross contamination.").also { stage++ }
                6 -> playerl(FacialExpression.FRIENDLY, "Ok then, see you around.").also { stage++ }
                7 -> npcl(FacialExpression.FRIENDLY, "Maybe...").also { stage = END_DIALOGUE }
            }

            16 -> when (stage) {
                0 -> if (inBorders(player!!, 2532, 3272, 2534, 3273)) {
                    player!!.dialogueInterpreter.sendDialogue("The door won't open.", "You notice a black cross on the door.").also { stage = END_DIALOGUE }
                } else {
                    playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                }
                1 -> playerl(FacialExpression.FRIENDLY, "I have a warrant from Bravek to enter here.").also { stage++ }
                2 -> npcl(FacialExpression.NEUTRAL, "This is highly irregular. Please wait...").also { stage++ }
                3 -> {
                    runTask(player!!, 0) {
                        findLocalNPC(player!!, NPCs.MOURNER_717)!!.sendChat("Hay... I got someone here with a warrant from Bravek, what should we do?")
                        findLocalNPC(player!!, NPCs.MOURNER_3216)!!.sendChat("Well you can't let them in...", 1)
                    }.also {
                        end()
                        setQuestStage(player!!, "Plague City", 17)
                        DoorActionHandler.handleAutowalkDoor(player, getObject(location(2540, 3273, 0))!!.asScenery())
                        sendDialogue(player!!, "You wait until the mourner's back is turned and sneak into the building.")
                    }
                }
            }

            in 17..100 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "I'd stand away from there. That black cross means that house has been touched by the plague.").also { stage = END_DIALOGUE }
            }
        }
    }
}