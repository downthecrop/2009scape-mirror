package content.region.kandarin.quest.scorpioncatcher

import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import core.ServerConstants
import core.api.*
import core.game.dialogue.Topic
import core.game.node.item.Item
import org.rs09.consts.Items

class SCThormacDialogue(val questStage: Int) : DialogueFile() {

    companion object {
        const val ASK_FOR_HELP = 10
        const val HOW_TO_CATCH = 30
        const val WHY_SHOULD_I_START = 50

        const val WAITING_FOR_SCORPIONS = 100
        const val GIVE_ANOTHER_CAGE = 110

        const val GOT_THEM_ALL = 200
        const val FULL_INVENTORY = 300
    }


    override fun handle(componentID: Int, buttonID: Int) {
        when (stage){
            START_DIALOGUE -> if (questStage == 0) {
                npcl(FacialExpression.FRIENDLY, "Hello I am Thormac the Sorcerer, " +
                        "I don't suppose you could be of assistance to me?").also { stage = ASK_FOR_HELP }
            }
            else {
                npcl(FacialExpression.NEUTRAL, "How goes your quest?").also {
                    stage = if (hasAnItem(player!!, Items.SCORPION_CAGE_463).exists()){
                        // Player has all the scorpions caught
                        GOT_THEM_ALL
                    } else {
                        WAITING_FOR_SCORPIONS
                    }
                }
            }

            ASK_FOR_HELP -> showTopics(
                Topic(FacialExpression.HAPPY,"What do you need assistance with?", ASK_FOR_HELP+1),
                Topic(FacialExpression.NEUTRAL, "I'm a little busy.", END_DIALOGUE)
            )
            ASK_FOR_HELP+1 -> npcl(FacialExpression.WORRIED, " I've lost my pet scorpions. " +
                    "They're lesser Kharid scorpions, a very rare breed.").also { stage++ }
            ASK_FOR_HELP+2 -> npcl(FacialExpression.WORRIED, "I left their cage door open, now I don't know where they've gone.").also { stage++ }
            ASK_FOR_HELP+3 -> npcl(FacialExpression.WORRIED, "There's three of them, and they're quick little beasties. " +
                    "They're all over " + ServerConstants.SERVER_NAME + ".").also { stage++ }
            ASK_FOR_HELP+4 -> showTopics(
                Topic(FacialExpression.ASKING, "So how would I go about catching them then?", HOW_TO_CATCH),
                Topic(FacialExpression.ASKING, "What's in it for me?", WHY_SHOULD_I_START),
                Topic(FacialExpression.NEUTRAL, "I'm not interested then.", END_DIALOGUE)
                )

            WHY_SHOULD_I_START -> npcl(FacialExpression.WORRIED, "Well I suppose I can aid you with my skills as a staff sorcerer. " +
                    "Most battlestaffs around here are a bit puny. I can beef them up for you a bit.").also {
                        // Need to recheck the quest stage since it may have been changed in this dialogue
                        if(getQuestStage(player!!, "Scorpion Catcher") == 0) stage++
                        else stage = END_DIALOGUE
                    }
            WHY_SHOULD_I_START+1 -> showTopics(
                Topic(FacialExpression.ASKING, "So how would I go about catching them then?", HOW_TO_CATCH),
                Topic(FacialExpression.NEUTRAL, "I'm not interested then.", END_DIALOGUE)
            )

            HOW_TO_CATCH -> npcl(FacialExpression.WORRIED, "Well I have a scorpion cage here which you can " +
                    "use to catch them in.").also {
                        if (hasSpaceFor(player!!,Item(Items.SCORPION_CAGE_456))) stage++
                        else stage = FULL_INVENTORY
                    }
            HOW_TO_CATCH+1 -> {
                sendItemDialogue(player!!, Items.SCORPION_CAGE_456, "Thormac gives you a cage.").also { stage++ }
                startQuest(player!!, "Scorpion Catcher")
                addItem(player!!, Items.SCORPION_CAGE_456)
            }
            HOW_TO_CATCH+2 -> npcl(FacialExpression.WORRIED, "If you go up to the village of Seers, to the North of " +
                    "here, one of them will be able to tell you where the scorpions are now.").also { stage++ }
            HOW_TO_CATCH+3 -> showTopics(
                Topic(FacialExpression.ASKING, "What's in it for me?", WHY_SHOULD_I_START),
                Topic(FacialExpression.NEUTRAL, "Ok, I will do it then.", END_DIALOGUE )
                )


            WAITING_FOR_SCORPIONS -> {
                if (!hasAnItem(player!!, arrayOf(Items.SCORPION_CAGE_456, Items.SCORPION_CAGE_457, Items.SCORPION_CAGE_458,
                    Items.SCORPION_CAGE_459, Items.SCORPION_CAGE_460, Items.SCORPION_CAGE_461,
                    Items.SCORPION_CAGE_462), false).exists()){
                    playerl(FacialExpression.SAD, "I've lost my cage.").also { stage = GIVE_ANOTHER_CAGE }
                }
                else{
                    playerl(FacialExpression.NEUTRAL, "I've not caught all the scorpions yet.").also { stage++ }
                }
            }
            WAITING_FOR_SCORPIONS+1 -> npcl(FacialExpression.WORRIED, "Well remember to go speak to the Seers" +
                    " North of here, if you need any help.").also { stage = END_DIALOGUE }

            GIVE_ANOTHER_CAGE -> {
                player!!.inventory.add(Item(Items.SCORPION_CAGE_456))
                // Clear all attributes keeping track of the scorpions
                player!!.removeAttribute("scorpion_catcher:caught_taverly")
                player!!.removeAttribute("scorpion_catcher:caught_barb")
                player!!.removeAttribute("scorpion_catcher:caught_monk")
                npcl(FacialExpression.NEUTRAL, "Ok, here's another cage. You're almost as bad" +
                        " at losing things as me.").also { stage = END_DIALOGUE }
            }

            GOT_THEM_ALL -> {
                playerl ("I have retrieved all your scorpions.").also { stage++ }
            }
            GOT_THEM_ALL+1 ->{
                npcl(FacialExpression.HAPPY, "Aha, my little scorpions home at last!").also { stage++ }
                player!!.inventory.remove(Item(Items.SCORPION_CAGE_463))

                // Don't need to keep track of which scorpions have been caught anymore
                player!!.removeAttribute("scorpion_catcher:caught_taverly")
                player!!.removeAttribute("scorpion_catcher:caught_barb")
                player!!.removeAttribute("scorpion_catcher:caught_monk")
            }
            GOT_THEM_ALL+2 ->{
                end().also { finishQuest(player!!, "Scorpion Catcher") }
            }


            FULL_INVENTORY -> npcl("You don't have space to hold a cage").also { stage = END_DIALOGUE }
        }
    }
}