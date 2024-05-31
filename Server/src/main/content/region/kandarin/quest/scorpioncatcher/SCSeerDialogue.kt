package content.region.kandarin.quest.scorpioncatcher

import content.region.kandarin.seers.dialogue.SeerDialogue
import core.api.sendDialogue
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class SCSeerDialogue(val questStage: Int, private val dialogueEntry: Int) : DialogueFile() {

    companion object {
        const val FIRST_SCORPION_HELP = 20
        const val FIRST_SCORPION_THORMAC = 30
        const val FIRST_SCORPION_GUIDE = 40
        const val OTHER_SCORPIONS = 50


        // There are many ways that this dialogue can be entered
        const val ENTRY_HELP = SeerDialogue.SC_QUEST_HELP
        const val ENTRY_FRIEND = SeerDialogue.SC_QUEST_FRIEND
        const val ENTRY_OTHERS = SeerDialogue.SC_QUEST_OTHER_SCORPIONS

    }


    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> when (dialogueEntry){
                ENTRY_HELP -> npcl("Well you have come to the right place. I am a master of animal detection.").also { stage = FIRST_SCORPION_HELP }
                ENTRY_FRIEND -> npcl(FacialExpression.NEUTRAL, "What does the old fellow want?").also { stage = FIRST_SCORPION_THORMAC }
                // word wrap doesn't work again
                ENTRY_OTHERS -> npc(FacialExpression.NEUTRAL, "Well, I've checked my looking glass. There seems to be",
                    "a kharid scorpion in a little village to the east,",
                    "surrounded by lots of uncivilized-looking warriors.",
                    "Some kind of merchant there seems to have picked it up.").also { stage = OTHER_SCORPIONS }
                else -> {
                    println("Invalid entry to SCSeerDialogue $dialogueEntry")
                    end()
                }
            }

            FIRST_SCORPION_THORMAC -> playerl(
                FacialExpression.NEUTRAL,
                "He's lost his valuable lesser Kharid scorpions."
            ).also { stage++ }
            FIRST_SCORPION_THORMAC + 1 -> npcl(
                FacialExpression.HAPPY,
                "Well you have come to the right place. I am a master of animal detection."
            ).also { stage = FIRST_SCORPION_GUIDE }

            FIRST_SCORPION_HELP -> npcl("Do you need to locate any particular scorpion? Scorpions are a creature somewhat in abundance.").also { stage++ }
            FIRST_SCORPION_HELP + 1 -> playerl("I'm looking for some lesser Kharid scorpions. They belong to Thormac the Sorcerer.").also {
                stage = FIRST_SCORPION_GUIDE
            }

            FIRST_SCORPION_GUIDE -> npcl(FacialExpression.HAPPY, "Let me look into my looking glass.").also { stage++ }
            FIRST_SCORPION_GUIDE + 1 -> sendDialogue(player!!, "The Seer produces a small mirror.").also { stage++ }
            FIRST_SCORPION_GUIDE + 2 -> sendDialogue(player!!, "The Seer gazes into the mirror.").also { stage++ }
            FIRST_SCORPION_GUIDE + 3 -> sendDialogue(player!!, "The Seer smooths his hair with his hand.").also { stage++ }
            FIRST_SCORPION_GUIDE + 4 -> npcl("I can see a scorpion that you seek. It would appear to be near some nasty spiders. I can see two coffins there as well.").also { stage++ }
            FIRST_SCORPION_GUIDE + 5 -> npcl("The scorpion seems to be going through some crack in the wall. Its gone into some sort of secret room.").also { stage++ }
            FIRST_SCORPION_GUIDE + 6 -> npcl("Well see if you can find the scorpion then, and I'll try and get you some information on the others.").also {
                setQuestStage(player!!, "Scorpion Catcher", ScorpionCatcher.QUEST_STATE_DARK_PLACE)
                stage = END_DIALOGUE
            }

            OTHER_SCORPIONS -> npcl ("That's all I can tell about that scorpion.").also { stage++ }
            OTHER_SCORPIONS + 1 -> playerl("Any more scorpions?").also { stage++ }
            OTHER_SCORPIONS + 2 -> npcl ("It's good that you should ask. I have information on the last scorpion for you.").also { stage++ }
            OTHER_SCORPIONS + 3 -> npcl ("It seems to be in some sort of upstairs room. There seems to be some sort of brown clothing lying on a table.").also {
                setQuestStage(player!!, "Scorpion Catcher", ScorpionCatcher.QUEST_STATE_OTHER_SCORPIONS)
                stage = END_DIALOGUE
            }
        }
    }
}
