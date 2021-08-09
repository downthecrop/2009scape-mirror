package rs09.game.content.quest.members.naturespirit

import api.ContentAPI
import core.game.content.dialogue.FacialExpression
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class NSDrezelDialogue : DialogueFile() {
    var questStage = 0
    override fun handle(componentID: Int, buttonID: Int) {
        questStage = player!!.questRepository.getStage("Nature Spirit")

        if(questStage <= 5){
            when(stage){
                0 -> options("Sorry, not interested...", "Well, what is it, I may be able to help?").also { stage++ }
                1 -> when(buttonID){
                    1 -> playerl(FacialExpression.NEUTRAL, "Sorry, not interested.").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.FRIENDLY, "Well, what is it, I may be able to help?").also { stage++ }
                }

                2 -> npcl(FacialExpression.HALF_THINKING, "There's a man called Filliman who lives in Mort Myre, I wonder if you could look for him? The swamps of Mort Myre are dangerous though, they're infested with Ghasts!").also { stage++ }
                3 -> options("Who is this Filliman?", "Where's Mort Myre?", "What's a Ghast?", "Yes, I'll go and look for him.", "Sorry, I don't think I can help.").also { stage++ }
                4 -> when(buttonID){
                    1 -> npcl(FacialExpression.NEUTRAL, "Filliman Tarlock is his full name and he's a Druid. He lives in Mort Myre much like a hermit, but there's many a traveller who he's helped.").also { stage-- }
                    2 -> npcl(FacialExpression.NEUTRAL, "Mort Myre is a decayed and dangerous swamp to the south. It was once a beautiful forest but has since become filled with vile emanations from within Morytania.").also { stage = 6 }
                    3 -> npcl(FacialExpression.NEUTRAL, "A Ghast is a poor soul who died in Mort Myre. They're undead of a special class, they're untouchable as far as I'm aware!").also { stage = 5 }
                    4 -> playerl(FacialExpression.FRIENDLY, "Yes, I'll go and look for him.").also { stage = 10 }
                    5 -> playerl(FacialExpression.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                }
                5 -> npcl(FacialExpression.NEUTRAL, "Filliman knew how to tackle them, but I've not heard from him in a long time. Ghasts, when they attack, will devour any food you have. If you have no food, they'll draw their nourishment from you!").also { stage = 3 }
                6 -> npcl(FacialExpression.NEUTRAL, " We put a fence around it to stop unwary travellers going in. Anyone who dies in the swamp is forever cursed to haunt it as a Ghast. Ghasts attack travellers, turning food to rotten filth.").also { stage = 3 }

                10 -> npcl(FacialExpression.NEUTRAL, "That's great, but it is very dangerous. Are you sure you want to do this?").also { stage++ }
                11 -> options("Yes, I'm sure.", "Sorry, I don't think I can help.").also { stage++ }
                12 -> when(buttonID){
                    1 -> playerl(FacialExpression.FRIENDLY, "Yes, I'm sure.").also { stage = 20 }
                    2 -> playerl(FacialExpression.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                }

                20 -> npcl(FacialExpression.NEUTRAL, "That's great! Many Thanks! Now then, please be aware of the Ghasts, you cannot attack them, only Filliman knew how to take them on.").also { stage++ }
                21 -> npcl(FacialExpression.NEUTRAL, "Just run from them if you can. If you start to get lost, try to make your way back to the temple.").also { stage++ }
                22 -> {
                    ContentAPI.sendDoubleItemDialogue(player!!, Items.MEAT_PIE_2327, Items.APPLE_PIE_2323, "The cleric hands you some food.")
                    if(questStage == 0){
                        ContentAPI.addItemOrDrop(player!!, Items.MEAT_PIE_2327, 3)
                        ContentAPI.addItemOrDrop(player!!, Items.APPLE_PIE_2323, 3)
                        player!!.questRepository.getQuest("Nature Spirit").setStage(player!!, 5)
                    }
                    stage++
                }
                23 -> npcl(FacialExpression.NEUTRAL, "Please take this food to Filliman, he'll probably appreciate a bit of cooked food. Now, he's never revealed where he lives in the swamps but I guess he'd be to the south, search for him won't you?").also { stage++ }
                24 -> playerl(FacialExpression.FRIENDLY, "I'll do my very best, don't worry, if he's in there and he's still alive I'll definitely find him.").also { stage = END_DIALOGUE; player!!.questRepository.getQuest("Nature Spirit").start(player!!) }
            }
        }


    }

}