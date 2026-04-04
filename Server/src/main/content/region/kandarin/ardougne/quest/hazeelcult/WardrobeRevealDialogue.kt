package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.carnilleanArc
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * This dialogue is triggered at stage 5 of Hazeel Cult during the Carnillean Arc by searching the wardrobe for evidence.
 */

@Initializable
class WardrobeRevealDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, WardrobeRevealDialogueFile())
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return WardrobeRevealDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf() // dialogue is triggered manually
    }
}

class WardrobeRevealDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)
        when {
            (questStage == 5 && carnilleanArc(player!!)) -> {
                when (stage) {
                    // note that it's authentic for the 'wardrobe' to be referred to as 'cupboard' in the dialogue. it used to be a cupboard pre-graphical update. https://youtu.be/CH7i06Noc18?si=zAdxBNlIS92cseBR&t=606
                    0 -> sendDialogue(player!!, "You search the cupboard thoroughly. You find a bottle of poison and a mysterious amulet. You pass your discovery on to Ceril.").also { stage++ }
                    1 -> sendPlayerDialogue(player!!, "Ceril!").also { stage++ }
                    2 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "What do you want now you scoundrel?", FacialExpression.ANGRY).also { stage++ }
                    3 -> sendPlayerDialogue(player!!, "Look what I've found in the cupboard!", FacialExpression.ANGRY).also { stage++ }
                    4 -> sendDialogue(player!!, "You hand Ceril the bottle of poison.").also { stage++ }
                    5 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "I... I don't believe it! Poison! JONES!").also { stage++ }
                    6 -> sendNPCDialogue(player!!, NPCs.BUTLER_JONES_890, "You called m'lud?").also { stage++ }
                    7 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "JONES! Just WHAT is this POISON doing here???").also { stage++ }
                    8 -> sendNPCDialogue(player!!, NPCs.BUTLER_JONES_890, "P-p-poison??? Yeah... uh... well, hmm... Oh, it's for Killing rats.").also { stage++ } // '...for Killing...' is authentic
                    9 -> sendNPCDialogue(player!!, NPCs.BUTLER_JONES_890, "Yes, that's right. Killing rats. I am but a loyal butler sir.").also { stage++ }
                    10 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "Rats eh? Yes, fair enough. Sorry to have doubted you old boy.").also { stage++ }
                    11 -> sendPlayerDialogue(player!!, "Then how about this?").also { stage++ }
                    12 -> sendDialogue(player!!, "You show Ceril the amulet from the cupboard.").also { stage++ }
                    13 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "Wh-what??? I've seen this amulet before... The thieves that broke in... One of them was wearing an identical amulet! Jones! I don't believe it!").also { stage++ }
                    14 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "Jones! We trusted you! We took you into our home!").also { stage++ }
                    15 -> sendNPCDialogue(player!!, NPCs.BUTLER_JONES_890, "You senile old fool... It was all too easy! I should have killed you and your pathetic family weeks ago!", FacialExpression.ANGRY).also { stage++ }
                    16 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "To think we took you in and trusted you... and this is how you repay us...").also { stage++ }
                    17 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "GUARD!").also { stage++ }
                    18 -> sendDialogue(player!!, "A guard rushes into the house.").also { stage++ }
                    19 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "Take him away. Attempted murder and burglary.").also { stage++ }
                    20 -> sendNPCDialogue(player!!, NPCs.BUTLER_JONES_890, "Don't think this is the last you have heard of me Ceril. You and your family will pay dearly for this. GLORY TO HAZEEL! ALL PRAISE ZAMORAK!").also { stage++ }
                    21 -> sendNPCDialogue(player!!, NPCs.GUARD_887, "Now now. Come along quietly. There'll be no revenge where YOU'RE going - Port Sarim Jail.").also { stage++ }
                    22 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "It looks like I am indebted to you sirrah!").also { stage++ }
                    23 -> sendPlayerDialogue(player!!, "No problem. We all make mistakes.").also { stage++ }
                    24 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "But if it weren't for you... My whole family... we could have been... I apologize for my harshness before.").also { stage++ }
                    25 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "The very least I can do is to reward you for your noble efforts, and to offer my sincerest apologies as a Lord and a gentleman.").also { stage++ }
                    26 -> sendPlayerDialogue(player!!, "Thank you Lord Ceril.").also { stage++ }
                    27 -> sendNPCDialogue(player!!, NPCs.CERIL_CARNILLEAN_885, "No, no, thank YOU! Feel free to stop by anytime adventurer!"
                    ).also {
                        end()
                        finishQuest(player!!, Quests.HAZEEL_CULT)
                    }
                }
            }

            (questStage in 0..100) -> {
                when(stage) {
                    0 -> sendDialogue(player!!, "You search the cupboard thoroughly but find nothing of interest.").also {stage = END_DIALOGUE }
                }
            }
        }
    }
}