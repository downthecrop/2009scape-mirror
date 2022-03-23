package rs09.game.content.quest.members.thelosttribe

import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE


class MistagLTDialogue(val isGreeting: Boolean, val questStage: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if(isGreeting){
            when(stage){
                //Greeting dialogue
                START_DIALOGUE -> npc("Perhaps you are a friend after all!").also { stage++ }
                1 -> npc("Greetings, friend. I am sorry I panicked when I saw you.").also { stage++ }
                2 -> npc("Our legends tell of the surface as a place of horror and","violence, where the gods forced us to fight in terrible","battles.").also { stage++ }
                3 -> npc("When I saw a surface-dweller appear I was afraid it","was a return to the old days!").also { stage++ }
                4 -> player("Did you break in to the castle cellar?").also { stage++ }
                5 -> npc("It was an accident. We were following a seam of iron","and suddenly we found ourselves in a room!").also { stage++ }
                6 -> npc("We blocked up our tunnel behind us and ran back","here. Then we did what cave goblins always do when","there is a problem: we hid and hoped it would go away.").also { stage++ }
                7 -> npc("We meant no harm! Please tell the ruler of the above","people that we want to make peace.").also { stage = END_DIALOGUE; player!!.questRepository.getQuest("Lost Tribe").setStage(player,46) }
            }
        }

        else{
            if(questStage == 45){
                when(stage){
                    START_DIALOGUE -> npc("Our legends tell of the surface as a place of horror and","violence, where the gods forced us to fight in terrible","battles.").also { stage++ }
                    1 -> npc("When I saw a surface-dweller appear I was afraid it","was a return to the old days!").also { stage++ }
                    2 -> player("Did you break in to the castle cellar?").also { stage++ }
                    3 -> npc("It was an accident. We were following a seam of iron","and suddenly we found ourselves in a room!").also { stage++ }
                    4 -> npc("We blocked up our tunnel behind us and ran back","here. Then we did what cave goblins always do when","there is a problem: we hid and hoped it would go away.").also { stage++ }
                    5 -> npc("We meant no harm! Please tell the ruler of the above","people that we want to make peace.").also { stage = END_DIALOGUE; player!!.questRepository.getQuest("Lost Tribe").setStage(player,46) }
                }
            }

            if(questStage == 50){
                when(stage){
                    START_DIALOGUE -> player("I have a peace treaty from the Duke of Lumbridge.").also { stage++ }
                    1 -> npc("A peace treaty? Then you will not invade?").also { stage++ }
                    2 -> player("No. As long as you stick to the terms of this treaty","there will be no conflict. The Duke of Lumbridge wants","to meet your ruler to sign it.").also { stage++ }
                    3 -> npc("I will summon Ur-tag, our headman, at once.").also { stage++ }
                    4 -> {
                        end()
                        LostTribeCutscene(player!!).start()
                        //ActivityManager.start(player,"Lost Tribe Cutscene",false)
                    }
                }
            }
        }
    }

    override fun npc(vararg messages: String?): Component? {
        return npc(FacialExpression.OLD_NORMAL,*messages)
    }
}