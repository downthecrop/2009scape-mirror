package rs09.game.content.quest.members.priestinperil

import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE


class KingRoaldPIPDialogue(val questStage: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if(stage == START_DIALOGUE){
            npc("Well, hello there. What do you want?").also { stage++ }
            return
        }

        if(questStage == 0){
            when(stage){
                1 -> player("I am looking for a quest!").also { stage++ }
                2 -> npc("A quest you say? Hmm, what an odd request to make", "of the king. It's funny you should mention it though, as", "there is something you can do for me.").also { stage++ }
                3 -> npc("Are you aware of the temple east of here? It stands on", "the river Salve and guards the entrance to the lands of", "Morytania?").also { stage++ }
                4 -> player("No, I don't think I know it...").also { stage++ }
                5 -> npc("Hmm, how strange that you don't. Well anyway, it has", "been some days since last I heard from Drezel, the", "priest who lives there.").also { stage++ }
                6 -> npc("Be a sport and go make sure that nothing untoward", "has happened to the silly old codger for me, would you?").also { stage++ }
                7 -> options("Sure.", "No, that sounds boring.").also { stage++ }
                8 -> when(buttonID){
                    1 -> {
                        player("Sure. I don't have anything better to do right now.")
                        player!!.questRepository.getQuest("Priest in Peril").start(player)
                        stage++
                    }
                    2 -> {
                        npc("Yes, I dare say it does. I wouldn't even have", "mentioned it had you not seemed to be looking for", "something to do anyway.")
                        stage = END_DIALOGUE
                    }
                }
                9 -> npc("Many thanks adventurer! I would have sent one of my", "squires but they wanted payment for it!").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage <= 11){
            when(stage){
                1 -> npc("You have news of Drezel for me?").also { stage++ }
                2 -> player("No, not yet.").also { stage++ }
                3 -> npc("I would wish you would go check on my dear friend.").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage == 12){
            when(stage){
                1 -> npc("You have news of Drezel for me?").also { stage++ }
                2 -> player("Yeah, I spoke to the guys at the temple and they said", "they were being bothered by that dog in the crypt, so I", "went and killed it for them. No problem.").also { stage++ }
                3 -> npc("YOU DID WHAT???").also { stage++ }
                4 -> npc("Are you mentally deficient??? That guard dog was", "protecting the route to Morytania! Without it we could", "be in severe peril of attack!").also { stage++ }
                5 -> player("Did I make a mistake?").also { stage++ }
                6 -> npc("YES YOU DID!!!!! You need to get there right now", "and find out what is happening! Before it is too late for", "us all!").also { stage++ }
                7 -> player("B-but Drezel TOLD me to...!").also { stage++ }
                8 -> npc("No, you absolute cretin! Obviously some fiend has done", "something to Drezel and tricked your feeble intellect", "into helping them kill that guard dog!").also { stage++ }
                9 -> npc("You get back there and do whatever is necessary to", "safeguard my kingdom from attack, or I will see you", "beheaded for high treason!").also { stage++ }
                10 -> {
                    player("Y-yes your Highness.")
                    player!!.questRepository.getQuest("Priest in Peril").setStage(player,13)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage == 13){
            when(stage){
                1 -> npc("AND MORE IMPORTANTLY, WHY HAVEN'T", "YOU ENSURED THE BORDER TO", "MORYTANIA IS SECURE YET?").also { stage++ }
                2 -> player("Okay, okay, I'm going, I'm going.... There's no need to", "shout...").also { stage++ }
                3 -> npc("NO NEED TO SHOUT?!").also { stage++ }
                4 -> npc("Listen, and listen well, and see if your puny mind can", "comprehend this; if the border is not protected, then we", "are at the mercy of the evil beings").also { stage++ }
                5 -> npc("that live in Morytania. Given the most of the", "inhabitants consider humans to be nothing more than", "over talkative snack foods, I would").also { stage++ }
                6 -> npc("say that me shouting at you for your incompetence is", "the LEAST of your worries right now, NOW GO!").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage == 14){
            when(stage){
                1 -> player("I'm looking for a key to unlock Drezel!").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage == 15 || questStage == 16 || questStage == 17){
            when(stage){
                1 -> player("I'm helping Drezel!").also { stage = END_DIALOGUE }
            }
        }

        else {
            abandonFile()
        }

    }
}