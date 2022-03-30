package rs09.game.content.activity.pyramidplunder

import api.setAttribute
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class GuardianMummyDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.OLD_NOT_INTERESTED, "*sigh* Not another one.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.HALF_ASKING, "Another what?").also { stage++ }
            1 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "Another 'archaeologist'. I'm not going to let you plunder my master's tomb you know.").also { stage++ }
            2 -> playerl(FacialExpression.NEUTRAL, "That's a shame. Have you got anything else I could do while I'm here?").also { stage++ }
            3 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "If it will keep you out of mischief I suppose I could set something up for you... I have a few rooms full of some things you humans might consider valuable, do you want to give it a go?").also { stage++ }
            4 -> player.dialogueInterpreter.sendOptions("Play Pyramid Plunder?", "That sounds like fun; what do I do?", "Not right now.", "I know what I'm doing, so let's get on with it.").also { stage++ }
            5 -> when(buttonId)
            {
                1 -> playerl(FacialExpression.FRIENDLY, "That sounds like fun; what do I do?").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "Not right now.").also { stage = END_DIALOGUE }
                3 -> playerl(FacialExpression.ANNOYED, "I know what I'm doing, so let's get on with it.").also { stage = 100 }
            }

            10 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "You have five minutes to explore the treasure rooms and collect as many artefacts as you can. The artefacts are in the urns, chests and sarcophagi found in each room.").also { stage++ }
            11 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "There are eight treasure rooms, each subsequent room requires higher thieving skills to both enter the room and thieve from the urns and other containers.").also { stage++ }
            12 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "The rewards also become more lucrative the further into the tomb you go. You will also have to deactivate a trap in order to enter the main part of each room.").also { stage++ }
            13 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "When you want to move onto the next room you need to find the correct door first.").also { stage++ }
            14 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "There are four possible exits... you must open the door before finding out whether it is the exit or not. Opening the doors require picking their locks. Having a lockpick will make this easier.").also { stage++ }
            15 -> player.dialogueInterpreter.sendOptions("Do you have any more questions?", "How do I leave the game?", "How do I get the artefacts?", "What do I do with the artefacts I collect?", "I'm ready to give it a go now.").also { stage++ }
            16 -> when(buttonId)
            {
                1 -> playerl(FacialExpression.FRIENDLY, "How do I leave the game?").also { stage = 20 }
                2 -> playerl(FacialExpression.FRIENDLY, "How do I get the artefacts?").also { stage = 30 }
                3 -> playerl(FacialExpression.FRIENDLY, "What do I do with the artefacts?").also { stage = 40 }
                4 -> playerl(FacialExpression.FRIENDLY, "I'm ready to give it a go now.").also { stage = 100 }
            }

            20 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "If at any point you decide you need to leave just use a glowing door. The game will end and you will be taken out of the pyramid").also { stage = 15 }

            30 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "The artefacts are in the urns, chests and sarcophagi. Urns contain snakes that guard them. The sarcophagi take some strength to open. They take a while to open.").also { stage++ }
            31 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "Of course, Mummies have been known to take a nap in the sarcophagi, so beware. The golden chests generally contain better artefacts, but are also trapped with scarabs!").also { stage = 15 }

            40 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "There are a number of different artefacts, of three main types. The least valuable are the pottery statuettes and scarabs, and the ivory combs.").also { stage++ }
            41 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "Next are the stone scarabs, statuettes and seals, and finally the gold versions of those artefacts. They are not old, but are well made.").also { stage++ }
            42 -> playerl(FacialExpression.HALF_ASKING, "What do I do with artefacts once I've collected them?").also { stage++ }
            43 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "That Simon Simpleton, I mean Templeton, will probably give you some money for them. He couldn't spot a real artefact if it came up to him and bit him in the face.").also { stage++ }
            44 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "He usually slinks about near the pyramid north-east of Sophanem. I expect he's trying to get some poor fools to steal things from that pyramid as well.").also { stage = 15 }

            100 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "Alright, fine.").also { stage++ }
            101 -> {
                end()
                PyramidPlunderMinigame.join(player)
                setAttribute(player, "/save:pp:mummy-spoken-to", true)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARDIAN_MUMMY_4476)
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GuardianMummyDialogue(player)
    }
}