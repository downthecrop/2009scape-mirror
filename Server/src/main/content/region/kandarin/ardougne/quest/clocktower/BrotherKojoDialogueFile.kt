package content.region.kandarin.ardougne.quest.clocktower

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class BrotherKojoDialogueFile : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        if (getAttribute(player!!, ClockTower.attributeAskKojoAboutRats, false) && !getAttribute(player!!, ClockTower.attributeRatsPoisoned, false)) {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "I've found the white cog! But it's locked behind a gate. There's rats everywhere!").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Rats again?! Kill them! Kill them all!").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "But how do I open the gate? Do you have a key?").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Get rid of the rats! I'm sure I left some rat poison down there!").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "I guess I better go and deal with the rat problem...").also {
                    stage = END_DIALOGUE
                }
            }
            return
        }
        when (getQuestStage(player!!, ClockTower.questName)) {
            0 -> {
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello monk.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello adventurer. My name is Brother Kojo. Do you happen to know the time?").also { stage++ }
                    2 -> player(FacialExpression.SAD, "No, sorry, I don't.").also { stage++ }
                    3 -> npcl(FacialExpression.NEUTRAL, "Exactly! This clock tower has recently broken down, and without it nobody can tell the correct time. I must fix it before the town people become too angry!").also { stage++ }
                    4 -> npcl(FacialExpression.ASKING, "I don't suppose you could assist me in the repairs? I'll pay you for your help.").also { stage++ }
                    5 -> showTopics(
                            Topic(FacialExpression.FRIENDLY, "Ok old monk, what can I do?", 30),
                            Topic(FacialExpression.ASKING, "So... how much reward are we talking then?", 10),
                            Topic(FacialExpression.FRIENDLY, "Not now old monk.", 20),
                    )

                    10 -> npcl(FacialExpression.STRUGGLE, "Well, I'm only a monk so I'm not exactly rich, but I assure you I will give you a fair reward for the time spent assisting me in repairing the clock.").also { stage = 4 }

                    20 -> npcl(FacialExpression.FRIENDLY, "OK then. Come back and let me know if you change your mind.").also { stage = END_DIALOGUE }

                    30 -> npc(FacialExpression.HAPPY, "Oh, thank you kind ${if (player!!.isMale) "sir" else "madam"}!",
                            "In the cellar below, you'll find four cogs.",
                            "They're too heavy for me, but you should be able to",
                            "carry them one at a time.").also { stage++ }

                    31 -> npcl(FacialExpression.THINKING, "I know one goes on each floor... but I can't exactly remember which goes where specifically. Oh well, I'm sure you can figure it out fairly easily.").also { stage++ }
                    32 -> player(FacialExpression.FRIENDLY, "Well, I'll do my best.").also { stage++ }
                    33 -> npcl(FacialExpression.HAPPY, "Thank you again! And remember to be careful, the cellar is full of strange beasts!").also {
                        stage = END_DIALOGUE
                        setQuestStage(player!!, ClockTower.questName, 1)
                    }
                }
            }
            1 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.ASKING, "Oh hello, are you having trouble? The cogs are in four rooms below us. Place one cog on a pole on each of the four tower levels.").also { stage++ }
                    1 -> showTopics(
                            Topic(FacialExpression.FRIENDLY, "Can I have a hint?", 2),
                            Topic(FacialExpression.ASKING, "I'll carry on looking.", END_DIALOGUE),
                    )
                    2 -> showTopics(
                            Topic(FacialExpression.FRIENDLY, "Have you any idea where I might find the red cog?", 3),
                            Topic(FacialExpression.FRIENDLY, "Have you any idea where I might find the blue cog?", 3),
                            Topic(FacialExpression.FRIENDLY, "Have you any idea where I might find the black cog?", 3),
                            Topic(FacialExpression.FRIENDLY, "Have you any idea where I might find the white cog?", 3),
                    )
                    3 -> npcl(FacialExpression.FRIENDLY, "If I knew, I wouldn't ask you to find it. It's in the basement somewhere.").also { stage = 1 }
                }
            }
            2 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "I've placed a cog!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "That's great. Come see me when you've done the other three.").also { stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Two down!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Two to go.").also { stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "One left.").also { stage = END_DIALOGUE }
                }
            }
            in 5 .. 10 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "I have replaced all the cogs!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Really..? Wait, listen! Well done, well done! Yes yes yes, you've done it! You ARE clever!").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "The townsfolk will all be able to know the correct time now! Thank you so much for all of your help! And as promised, here is your reward!").also { stage++ }
                    3 -> {
                        end()
                        finishQuest(player!!, ClockTower.questName)
                    }
                }
            }
            100 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Oh hello there traveller. You've done a grand job with the clock. It's just like new.").also { stage = END_DIALOGUE }
                }
            }
        }
    }

}