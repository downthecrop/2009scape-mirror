package content.region.kandarin.quest.scorpioncatcher

import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class SCPeksaDialogue(val questStage: Int) : DialogueFile() {

    companion object {
        const val ASK_ABOUT_SCORPION = START_DIALOGUE
        const val ASK_FOR_HELP = 20
        const val THANK_YOU = 30

    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            // Word wrap issues
            ASK_ABOUT_SCORPION -> npc("Now how could you know about that, I wonder?", "Mind you, I don't have it anymore.").also { stage++ }
            ASK_ABOUT_SCORPION + 1 -> npcl(" I gave it as a present to my brother Ivor when I visited our outpost in the west.").also { stage++ }
            ASK_ABOUT_SCORPION + 2 -> npcl("Well, actually I hid it in his bed so it would nip him. It was a bit of a surprise gift.").also { stage++ }

            ASK_ABOUT_SCORPION + 3 -> showTopics(
                Topic("So where is this outpost?", ASK_FOR_HELP),
                Topic("Thanks for the information", THANK_YOU, true)
            )

            ASK_FOR_HELP -> npcl("Its a fair old trek to the west, across the White Wolf Mountains. Then head west, north-west until you see the axes and horned helmets.").also { stage=
                THANK_YOU }

            THANK_YOU -> {
                playerl("Thanks for the information").also { stage++ }
                setQuestStage(player!!, "Scorpion Catcher", ScorpionCatcher.QUEST_STATE_PEKSA_HELP)
            }
            THANK_YOU + 1 -> npcl ("No problems! Tell Ivor I said hi!").also { stage = END_DIALOGUE }
        }
    }
}
