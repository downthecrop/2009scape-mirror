package content.global.ame.events.evilbob

import content.global.ame.events.evilbob.EvilBobUtils.giveEventFishingSpot
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class EvilBobDialogue(val rewardDialogue: Boolean = false, val rewardXpSkill: Int = Skills.FISHING) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.EVIL_BOB_2479)
        if (getAttribute(player!!, EvilBobUtils.assignedFishingZone, "none") == "none") giveEventFishingSpot(player!!)
            when (stage) {
                0 -> {
                    if (rewardDialogue) {
                        sendPlayerDialogue(player!!, "That was the strangest dream I've ever had! Assuming it was a dream...", FacialExpression.HALF_ASKING)
                        stage = if (rewardXpSkill == Skills.FISHING) 900 else 901
                    } else if (getAttribute(player!!, EvilBobUtils.eventComplete, false)) {
                        sendDialogue(player!!, "Evil Bob appears to be sleeping, best not to wake him up.").also { stage = END_DIALOGUE }
                    } else if (removeItem(player!!, Items.RAW_FISHLIKE_THING_6200)) {
                        playerl(FacialExpression.NEUTRAL, "Here, I've brought you some fish.").also { stage = 500 }
                    } else if (removeItem(player!!, Items.RAW_FISHLIKE_THING_6204)) {
                        setAttribute(player!!, EvilBobUtils.attentive, true)
                        setAttribute(player!!, EvilBobUtils.attentiveNewSpot, true)
                        playerl(FacialExpression.NEUTRAL, "Here, I've brought you some fish.").also { stage = 600 }
                    } else if (inInventory(player!!, Items.FISHLIKE_THING_6202) || inInventory(player!!, Items.FISHLIKE_THING_6206)) {
                        npcl(FacialExpression.CHILD_NORMAL, "What, are you giving me cooked fish? What am I going to do with that? Uncook it first!").also { stage = 700 }
                    } else if (!getAttribute(player!!, EvilBobUtils.startingDialogueSeen, false)) {
                        playerl(FacialExpression.ASKING, "Huh?").also { stage = 800 }
                        setAttribute(player!!, EvilBobUtils.startingDialogueSeen, true)
                    } else playerl(FacialExpression.ANNOYED, "Let me out of here!").also { stage++ }
                }
                1 -> npcl(FacialExpression.CHILD_NORMAL, "I will never let you go, ${player!!.username}!").also { stage++ }
                2 -> options("Why not?", "What's it all about?", "How is it possible that you're talking?", "What did you do to Bob?").also { stage++ }
                3 -> when (buttonID) {
                    1 -> playerl(FacialExpression.ASKING, "Why not?").also { stage = 100 }
                    2 -> playerl(FacialExpression.ASKING, "What's it all about?").also { stage = 200 }
                    3 -> playerl(FacialExpression.ASKING, "How is it possible that you're talking?").also { stage = 300 }
                    4 -> playerl(FacialExpression.ASKING, "What did you do to Bob?").also { stage = 400 }
                }

                100 -> npcl(FacialExpression.CHILD_NORMAL, "Uhm...").also { stage++ }
                101 -> npcl(FacialExpression.CHILD_NORMAL, "Because I say so! And because I can never have enough servants!").also { stage++ }
                102 -> npcl(FacialExpression.CHILD_NORMAL, "Now catch me some fish, I'm hungry.").also { stage++ }
                103 -> playerl(FacialExpression.ASKING, "What fish, where?").also { stage++ }
                104 -> npcl(FacialExpression.CHILD_NORMAL, "Talk to my other servants, and hurry it up!").also { stage = END_DIALOGUE }

                200 -> npcl(FacialExpression.CHILD_NORMAL, "Sit down and I'll tell you. It's a question of servant shortage.").also { stage++ }
                201 -> playerl(FacialExpression.NEUTRAL, "Go on.").also { stage++ }
                202 -> npcl(FacialExpression.CHILD_NORMAL, "You are a skilled worker. A human like you is worth a great deal as a slave.").also { stage++ }
                203 -> playerl(FacialExpression.ANGRY, "A slave?? I will have nothing to do with you. Is that clear? Absolutely nothing.").also { stage++ }
                204 -> npcl(FacialExpression.CHILD_NORMAL, "Now be reasonable little human. It's just a matter of time before you will do everything I ask you to. Play along and this can be a very nice place.").also { stage++ }
                205 -> playerl(FacialExpression.ANGRY, "I will not make any deals with you. I'm a human. I will not be pushed, numbered, meowed at, teleported or enslaved. My life is my own.").also { stage++ }
                206 -> npcl(FacialExpression.CHILD_NORMAL, "Is it?").also { stage++ }
                207 -> playerl(FacialExpression.ANNOYED, "Yes. You won't hold me.").also { stage++ }
                208 -> npcl(FacialExpression.CHILD_NORMAL, "Won't I?").also { stage++ }
                209 -> npcl(FacialExpression.CHILD_NORMAL, "Let me assure you there's no way out.").also { stage++ }
                210 -> npcl(FacialExpression.CHILD_NORMAL, "Just ask my servants!").also { stage = END_DIALOGUE }

                300 -> npcl(FacialExpression.CHILD_NORMAL, "How is it possible that you're not meowing?").also { stage++ }
                301 -> playerl(FacialExpression.HALF_ASKING, "Meowing?? Why would I be meowing?").also { stage++ }
                302 -> npcl(FacialExpression.CHILD_NORMAL, "Most humans do, that's why I'm wearing this amulet of Man speak... but I do try to train my staff in civilised speech.").also { stage++ }
                303 -> npcl(FacialExpression.CHILD_NORMAL, "Ah, but I suppose things are slightly different in your world... what a dreadful place that must be.").also { stage++ }
                304 -> playerl(FacialExpression.NEUTRAL, "I think I'm getting a headache...").also { stage = END_DIALOGUE }

                400 -> npcl(FacialExpression.CHILD_NORMAL, "Bob? I am Bob!").also { stage++ }
                401 -> npcl(FacialExpression.CHILD_NORMAL, "Oh, you mean the Bob in your world? Nothing. I am an incarnation of Bob here on Scape2009.").also { stage++ }
                402 -> playerl(FacialExpression.NEUTRAL, "What, you mean this is... like some kind of mirror land?").also { stage++ }
                403 -> npcl(FacialExpression.CHILD_NORMAL, " Something like that... somewhere in Scape2009 there must be a human servant called ${player!!.username}.").also { stage++ }
                404 -> npcl(FacialExpression.CHILD_NORMAL, "But you work just as well for me! Now get to work, human! Fish for me!").also { stage = END_DIALOGUE }

                500 ->{
                    if (getAttribute(player!!, EvilBobUtils.attentive , false)) stage = 505 else stage++
                    npcl(FacialExpression.CHILD_NORMAL, "Mmm, mmm...that's delicious.")
                }
                501 -> npcl(FacialExpression.CHILD_NORMAL, "Now, let me take...a little...catnap.").also { stage++ }
                502 -> {
                    end()
                    setAttribute(player!!, EvilBobUtils.eventComplete, true)
                    findNPC(NPCs.EVIL_BOB_2479)!!.sendChat("ZZZzzz")
                }
                505 -> {
                    setAttribute(player!!, EvilBobUtils.attentive , false)
                    setAttribute(player!!, EvilBobUtils.attentiveNewSpot , true)
                    giveEventFishingSpot(player!!)
                    npcl(FacialExpression.CHILD_NORMAL, "Now get me another, you no-good human.").also { stage++ }
                }
                506 -> sendDialogue(player!!, "Evil Bob seems slightly less attentive of you.").also { stage = END_DIALOGUE }

                600 -> npcl(FacialExpression.CHILD_NORMAL, "What was this? That was absolutely disgusting!").also { stage++ }
                601 -> npcl(FacialExpression.CHILD_NORMAL, "Don't you know what kind of fish I like? Talk to my other servants for some advice.").also { stage++ }
                602 -> sendDialogue(player!!, "Evil Bob seems more attentive of you.").also { stage = END_DIALOGUE }

                700 -> playerl(FacialExpression.HALF_ASKING, "Errr... Uncook it?").also { stage++ }
                701 -> npcl(FacialExpression.CHILD_NORMAL,"You heard me! There's the cold fire, by the trees, now get uncooking!").also { stage = END_DIALOGUE }

                800 -> playerl(FacialExpression.ANGRY, "Where am I?").also { stage++ }
                801 -> npcl(FacialExpression.CHILD_NORMAL, "On my island.").also { stage++ }
                802 -> playerl(FacialExpression.ANGRY, "Who brought me here?").also { stage++ }
                803 -> npcl(FacialExpression.CHILD_NORMAL, "That would be telling.").also { stage++ }
                804 -> playerl(FacialExpression.ANGRY, "Take me to your leader!").also { stage++ }
                805 -> npcl(FacialExpression.CHILD_NORMAL,"I am your leader, you are but a slave.").also { stage++ }
                806 -> playerl(FacialExpression.ANGRY, "I am not a slave, I am a free man!").also { stage++ }
                807 -> npcl(FacialExpression.CHILD_NORMAL, "Ah-ha-ha-ha-ha-ha!").also { stage = END_DIALOGUE }
                900 -> sendDialogue(player!!, "You feel somehow that you've become better at fishing.").also { stage = END_DIALOGUE }
                901 -> sendDialogue(player!!, "You feel somehow that you've become better at magic.").also { stage = END_DIALOGUE }
            }
        }
}
