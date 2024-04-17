package content.region.misthalin.barbvillage.stronghold.playersafety

import core.api.runTask
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class GuardDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object{
        const val DIALOGUE_COMPLETED = START_DIALOGUE
        const val DIALOGUE_NOT_COMPLETED = 50

    }

    override fun open(vararg args: Any?): Boolean {
        val hasRead = player.savedData.globalData.hasReadPlaques()
        if (hasRead) {
            npcl(FacialExpression.HALF_GUILTY, "Can I help you?").also { stage = DIALOGUE_COMPLETED }
        }
        else {
            npcl(FacialExpression.FURIOUS, "Ahem! Can I help you?").also { stage = DIALOGUE_NOT_COMPLETED }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            DIALOGUE_COMPLETED -> {
                playerl(
                    FacialExpression.HALF_GUILTY, "Can I go upstairs?"
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 1 -> {
                npcl(
                    FacialExpression.FURIOUS,
                    "Yes, citizen. Before you do I am instructed to give " +
                            "you one final piece of information"
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 2 -> {
                playerl(FacialExpression.HALF_GUILTY, "Oh, okay then.").also { stage++ }
            }

            DIALOGUE_COMPLETED + 3 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "In your travels around " + settings!!.name + ", should you find a player who acts in a " +
                            "way that breaks on of our rules, you should report them."
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 4 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "Reporting is very simple and easy to do. Simply click the Report Abuse button at " +
                            "the bottom of the screen and you will be shown the following screen:"
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 5 -> {
                player.interfaceManager.openComponent(700)
                runTask(player, 5) {
                    if (player != null) {
                        player.interfaceManager.close()
                    }
                    return@runTask
                }.also { stage++ }
            }

            DIALOGUE_COMPLETED + 6 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "Simply enter the player's name in the box and click the rule that the offender was breaking."
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 7 -> {
                playerl(FacialExpression.HALF_GUILTY, "Okay. Then what?").also { stage++ }
            }

            DIALOGUE_COMPLETED + 8 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "That's it! It really is that simple and it only takes a moment to do. " +
                            "Now you may enter the training centre. Good luck, citizen."
                ).also { stage++ }
            }

            DIALOGUE_COMPLETED + 9 -> {
                playerl(FacialExpression.HALF_GUILTY, "Thanks!").also { stage = END_DIALOGUE }
            }

            DIALOGUE_NOT_COMPLETED -> {
                playerl(
                    FacialExpression.HALF_GUILTY, "I'd like to go up to the training centre please."
                ).also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 1 -> {
                npcl(FacialExpression.FURIOUS, "Sorry, citizen, you can't go up there.").also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 2 -> {
                playerl(FacialExpression.OLD_SNEAKY, "Why not?").also { stage++ }

            }
            DIALOGUE_NOT_COMPLETED + 3 -> {
                npcl(
                    FacialExpression.ANNOYED,
                    "You must learn about player safety before entering the training centre."
                ).also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 4 -> {
                playerl(FacialExpression.HALF_GUILTY, "Oh. How do I do that?").also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 5 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "Each of these gublinches have been caught breaking the rules of " + settings!!.name + ". " +
                            "You should read the plaques on each of their cells to learn what they did wrong."
                ).also { stage++ }

            }
            DIALOGUE_NOT_COMPLETED + 6 -> {
                playerl(
                    FacialExpression.HALF_GUILTY,
                    "Oh, right. I can enter the training centre once I have done that?"
                ).also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 7 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "Yes. Once you have have examined each of the plaques, come and speak to me " +
                            "and I will tell you about the Report Abuse function."
                ).also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 8 -> {
                npcl(
                    FacialExpression.HALF_GUILTY,
                    "After that, I can let you into the training centre, upstairs."
                ).also { stage++ }
            }
            DIALOGUE_NOT_COMPLETED + 9 -> {
                playerl(FacialExpression.HALF_GUILTY, "Okay, thanks for the help.").also { stage = END_DIALOGUE }
                player.sendMessage("You need to read the jail plaques before the guard will allow you upstairs.")
            }
        }
        return true

    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_7142)
    }


}