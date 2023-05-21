package content.region.wilderness.dialogue

import core.api.*
import core.game.dialogue.*
import core.tools.*
import org.rs09.consts.*
import core.game.node.item.Item

class BrotherFintanWildyDialogue : DialogueFile() {
    override fun handle (componentId: Int, buttonId: Int) {
        val currentlyActive = getAttribute (player!!, WILDERNESS_PROT_ATTR, false)

        when (stage) {
            0 -> npcl (FacialExpression.FRIENDLY, "So, you come to discuss the protection of Saradomin?").also { stage++ }
            1 -> npcl (FacialExpression.ASKING, "What would you like to know?").also { stage++ }
            2 -> showTopics (
                    Topic (FacialExpression.NEUTRAL, "I would like to change my protection status.", 20),
                    Topic (FacialExpression.ASKING, "How does it all work?", 40),
                    Topic (FacialExpression.FRIENDLY, "Nevermind, thank you.", END_DIALOGUE)
                 )
            in 20 until 40 -> if (currentlyActive) handleDisableDialogue (stage - 20, componentId, buttonId) else handleEnableDialogue (stage - 20, componentId, buttonId)
            in 40 until END_DIALOGUE -> handleExplanation (stage - 40, componentId, buttonId)
        }
    }

    fun handleEnableDialogue (localStage: Int, componentId: Int, buttonId: Int) {
        val moreExpensive = getAttribute (player!!, WILDERNESS_HIGHER_NEXTFEE, false)
        val cost = if (moreExpensive) 1_500_000 else 500_000
        when (localStage) {
            0 -> npcl (FacialExpression.HALF_THINKING, "I can offer you the protection of Saradomin, but it will not come cheap.").also { stage++ }
            1 -> playerl (FacialExpression.ASKING, "What kind of price are we talking?").also { stage++ }
            2 -> npcl (FacialExpression.NEUTRAL, "The price of earning our lord's protection is ${if (moreExpensive) "1,500,000" else "500,000"} gold. Are you sure you wish to do this?").also { stage++ }
            3 -> showTopics(Topic (FacialExpression.NEUTRAL, "Yes, I'm sure.", stage + 1), Topic (FacialExpression.NEUTRAL, "Nevermind, actually.", END_DIALOGUE))
            4 -> if (amountInInventory(player!!, Items.COINS_995) < cost) {
                playerl (FacialExpression.HALF_GUILTY, "Though I'm afraid I don't seem to have the coins at the moment.")
                stage = END_DIALOGUE
            } else playerl (FacialExpression.FRIENDLY, "Here you go.").also { stage++ }
            5 -> {
                sendDialogue (player!!, "You hand over ${if (moreExpensive) "1,500,000" else "500,000"} gold")
                if (removeItem(player!!, Item(Items.COINS_995, cost))) {
                    setAttribute(player!!, WILDERNESS_PROT_ATTR, true)
                    removeAttribute(player!!, WILDERNESS_HIGHER_NEXTFEE)
                }
                stage++
            }
            6 -> npcl (FacialExpression.FRIENDLY, "Thank you, I will see that your deed is done.").also { stage = END_DIALOGUE }
        }
    }

    fun handleDisableDialogue (localStage: Int, componentId: Int, buttonId: Int) {
        when (localStage) {
            0 -> npcl (FacialExpression.HALF_THINKING, "You currently have the protection of Saradomin. Are you sure you wish to change your mind? It will not come cheap.").also { stage++ }
            1 -> playerl (FacialExpression.ASKING, "What kind of price are we talking?").also { stage++ }
            2 -> npcl (FacialExpression.NEUTRAL, "The price of abandoning our lord's protection is 750,000 gold. Are you sure you wish to do this?").also { stage++ }
            3 -> showTopics(Topic (FacialExpression.NEUTRAL, "Yes, I'm sure.", stage + 1), Topic (FacialExpression.NEUTRAL, "Nevermind, actually.", END_DIALOGUE))
            4 -> if (amountInInventory(player!!, Items.COINS_995) < 750_000) {
                playerl (FacialExpression.HALF_GUILTY, "Though I'm afraid I don't seem to have the coins at the moment.")
                stage = END_DIALOGUE
            } else playerl (FacialExpression.FRIENDLY, "Here you go.").also { stage++ }
            5 -> {
                sendDialogue (player!!, "You hand over 750,000 gold")
                if (removeItem(player!!, Item(Items.COINS_995, 750_000)))
                    setAttribute(player!!, WILDERNESS_PROT_ATTR, false)
                stage++
            }
            6 -> npcl (FacialExpression.FRIENDLY, "Thank you, I will see that your deed is done.").also { stage = END_DIALOGUE }
        }
    }

    fun handleExplanation (localStage: Int, componentId: Int, buttonId: Int) {
        when (localStage) {
            0 -> npcl (FacialExpression.FRIENDLY, "Well, you see, I have been granted the ability to afford adventurers the protection of Saradomin out in the Wilderness.").also { stage++ }
            1 -> npcl (FacialExpression.FRIENDLY, "This protection shields you from the corrupted will of other adventurers.").also { stage++ }
            2 -> npcl (FacialExpression.HALF_THINKING, "There are three stipulations, however.").also { stage++ }
            3 -> npcl (FacialExpression.HALF_THINKING, "The first is that should you ever find your own will corrupted, that is to say that you ever find yourself 'skulled', Saradomin will not be able to protect you while you bear that burden.").also { stage++ }
            4 -> npcl (FacialExpression.HALF_THINKING, "The second is that it is quite an expensive ritual that I must perform. The reagents are extraordinarily expensive. There will be a hefty fee every time you wish to change your protection status.").also { stage++ }
            5 -> npcl (FacialExpression.HALF_THINKING, "The third is that there is some foul devilry in the far north. Saradomin's Will cannot extend beyond the Demonic Ruins to the North. Our Lord is unaware of anything beyond that point.").also { stage++ }
            6 -> playerl (FacialExpression.HALF_ASKING, "So to make sure I'm entirely clear in understanding, the protection is very expensive, and is temporarily nullified if I'm ever skulled?").also { stage++ }
            7 -> npcl (FacialExpression.FRIENDLY, "Exactly!").also { stage++ }
            8 -> playerl (FacialExpression.FRIENDLY, "And the rules of this protection only apply up to the Demonic Ruins in the North and no further?").also { stage++ }
            9 -> npcl (FacialExpression.HALF_GUILTY, "Correct again, unfortunately.").also { stage++ }
            10 -> playerl (FacialExpression.HALF_THINKING, "Okay, that sounds pretty fair to me. Will I be able to attack other adventurers while under the protection of Saradomin?").also { stage++ }
            11 -> npcl (FacialExpression.DISGUSTED, "Such an odd question from someone who seeks the holy protection of our lord. I'm very inclined to instruct you to leave my sight at this request.").also { stage++ }
            12 -> npcl (FacialExpression.THINKING, "However, I do suppose there is virtue in attacking those who bear the burden of a corrupted will.").also { stage++ }
            13 -> npcl (FacialExpression.FRIENDLY, "I do suppose there is value to it. Should you come across those who bear a corrupted will, or 'skulled' as you adventurers call it, you may be able to come upon them.").also { stage++ }
            14 -> npcl (FacialExpression.FURIOUS, "HOWEVER").also { stage++ }
            15 -> npcl (FacialExpression.NEUTRAL, "Doing so may still corrupt your will nonetheless, and leave you vulnerable and without the protection of Saradomin while your will is corrupt.").also { stage++ }
            16 -> npcl (FacialExpression.NEUTRAL, "Moreover, should you EVER attack an innocent person, this blessing will be stripped from you. And should you desire to regain the blessing, the fee will be even higher than before.").also { stage++ }
            17 -> playerl (FacialExpression.HALF_ASKING, "So to recap, while protected I may attack those who are skulled, but doing so will leave me unprotected while I am skulled?").also { stage++ }
            18 -> npcl (FacialExpression.FRIENDLY, "That is correct.").also { stage++ }
            19 -> playerl (FacialExpression.HALF_ASKING, "And if I attack an innocent adventurer, then I lose my protection completely and I must pay an even bigger fee to get it back?").also { stage++ }
            20 -> npcl (FacialExpression.FRIENDLY, "That is also correct.").also { stage++ }
            21 -> playerl (FacialExpression.FRIENDLY, "Alright, I think I got it!").also { stage = 2 }
        }
    }

    companion object {
        val WILDERNESS_PROT_ATTR = "/save:wilderness-protection-active"
        val WILDERNESS_HIGHER_NEXTFEE = "/save:wilderness-higher-next-fee"
    }
}
