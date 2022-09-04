package rs09.game.node.entity.player.link.diary.dialogues

import api.addItemOrDrop
import api.getAttribute
import api.setAttribute
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.IfTopic
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE

class RatBurgissDiaryDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val easyDiaryComplete = AchievementDiary.hasCompletedLevel(player, DiaryType.VARROCK, 0)
        val alternateTeleport = getAttribute(player!!, "diaries:varrock:alttele", false)
        when(stage) {
            0 -> {
                if (AchievementDiary.canClaimLevelRewards(player, DiaryType.VARROCK, 0)) {
                    playerl(FacialExpression.FRIENDLY, "I think I've finished all of the tasks in my Varrock Achievement Diary.")
                    stage = 40
                    return
                }
                else if (AchievementDiary.canReplaceReward(player, DiaryType.VARROCK, 0)) {
                    playerl(FacialExpression.ANNOYED, "I seem to have lost my armor.")
                    stage = 50
                    return
                }

                showTopics(
                    Topic("What is the Achievement Diary?", 10),
                    IfTopic("Can I change my Varrock Teleport point?", 100, easyDiaryComplete),
                    Topic("What are the rewards?", 20),
                    Topic("How do I claim the rewards?", 30),
                    Topic(FacialExpression.NEUTRAL, "See you later.", END_DIALOGUE)
                )
            }

            10 -> npcl(FacialExpression.FRIENDLY, "It's a diary that helps you keep track of particular achievements. Here in Varrock it can help you discover some quite useful things. Eventually, with enough exploration, the people of Varrock will reward").also { stage++ }
            11 -> npcl(FacialExpression.FRIENDLY, "you. You can see what tasks you have listed by clicking on the green button in the Quest List.").also { stage = 0 }

            20 -> npcl(FacialExpression.FRIENDLY, "Well, there's three different levels of Varrock Armour, which match up with the three levels of difficulty. Each has the same rewards as the previous level, and an additional one too... but I").also { stage++ }
            21 -> npcl(FacialExpression.FRIENDLY, "won't spoil your surprise. Rest assured, the people of Varrock are happy to see you visiting the land.").also { stage = 0 }

            30 -> npcl(FacialExpression.FRIENDLY, "Just complete the tasks so they're all ticked off, then you can claim your reward. Most of them are straightforward; you might find some require quests to be started, if not finished.").also { stage++ }
            31 -> npcl(FacialExpression.FRIENDLY, "To claim the different Varrock Armour, speak to Vannaka, Reldo, and myself.").also { stage = 0 }

            40 -> npcl(FacialExpression.FRIENDLY, "You have? Excellent! Well done.").also { stage++ }
            41 -> playerl(FacialExpression.FRIENDLY, "Thank you. Uh... can I have the reward?").also { stage++ }
            42 -> npcl(FacialExpression.FRIENDLY, "Reward? Ah yes! Of course. Your reward, it's right here.").also { stage++ }
            43 -> {
                AchievementDiary.flagRewarded(player, DiaryType.VARROCK, 0)
                npcl(FacialExpression.FRIENDLY, "Now, this body armour is magically enhanced to help you with your Smithing and Mining. There is a furnace, not far from here, in Edgeville. Use this armour there and, when smelting ores up to and")
                stage++
            }
            44 -> npcl(FacialExpression.FRIENDLY, "including steel, you will have a chance of making an extra bar every time. Also, when you mine with this armour on, you will have a chance of Mining extra ores from rocks up to and including coal.").also { stage++ }
            45 -> npcl(FacialExpression.FRIENDLY, "Bear in mind that you will need to be wearing the armour for either of these to work. I will speak to the shopkeepers around Varrock who sell armour and weapons to get you better prices when you are").also { stage++ }
            46 -> npcl(FacialExpression.FRIENDLY, "wearing it. I can also change your Varrock Teleport spell so that it takes you to the Grand Exchange, if you'd find that more convenient.").also { stage++ }
            47 -> npcl(FacialExpression.FRIENDLY, "As an extra reward, you can also have this old magical lamp to help you with your skills. I was going to use it myself, but I don't really need it.").also { stage++ }
            48 -> playerl(FacialExpression.FRIENDLY, "Wow, thanks!").also { stage++ }
            49 -> npcl(FacialExpression.FRIENDLY, "If you should lose this armour, come back and see me for another set.").also { stage = 0 }

            50 -> {
                AchievementDiary.grantReplacement(player, DiaryType.VARROCK, 0)
                npcl(FacialExpression.ANNOYED, "You better be more careful this time.").also { stage = END_DIALOGUE }
            }

            100 -> showTopics(
                IfTopic("I'd like to teleport to the Grand Exchange.", 101, easyDiaryComplete && !alternateTeleport),
                IfTopic("I'd like to teleport to the city square.", 102, easyDiaryComplete && alternateTeleport),
                Topic("Nevermind.", END_DIALOGUE)
            )

            101 -> {
                npcl(FacialExpression.FRIENDLY, "There you are, your Varrock teleport will now take you to the Grand Exchange.")
                setAttribute(player!!, "/save:diaries:varrock:alttele", true)
                stage = 0
            }
            102 -> {
                npcl(FacialExpression.FRIENDLY, "There you are, your Varrock teleport will now take you to the city square.")
                setAttribute(player!!, "/save:diaries:varrock:alttele", false)
                stage = 0
            }
        }
    }
}