package rs09.game.node.entity.player.link.diary.dialogues

import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import rs09.game.content.dialogue.DialogueFile

class NedDiaryDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val level = 2
        when(stage) {
            0 -> {
                if (AchievementDiary.canClaimLevelRewards(player, DiaryType.LUMBRIDGE, level)) {
                    player("I've done all the medium tasks in my Lumbridge", "Achievement Diary.")
                    stage = 50
                }
                else if (AchievementDiary.canReplaceReward(player, DiaryType.LUMBRIDGE, level)) {
                    player("I've seemed to have lost my explorer's ring...")
                    stage = 60
                }
                else {
                    options(
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later."
                    )
                    stage = 1
                }
            }
            1 -> when (buttonID) {
                1 -> {
                player("What is the Achievement Diary?")
                stage = 10
            }
                2 -> {
                player("What are the rewards?")
                stage = 20
            }
                3 -> {
                player("How do I claim the rewards?")
                stage = 30
            }
                4 -> {
                player("See you later!")
                stage = 40
                }
            }
            10 -> {
                npc(
                    "Ah, well it's a diary that helps you keep track of",
                    "particular achievements you've made in the world of",
                    "2009Scape. In Lumbridge and Draynor i can help you",
                    "discover some very useful things indeed."
                )
                stage++
            }
            11 -> {
                npc("Eventually with enough exploration you will be", "rewarded for your explorative efforts.")
                stage++
            }
            12 -> {
                npc(
                    "You can access your Achievement Diary by going to",
                    "the Quest Journal. When you've opened the Quest",
                    "Journal click on the green star icon on the top right",
                    "hand corner. This will open the diary."
                )
                stage = 0
            }
            20 -> {
                npc(
                    "Ah, well there are different rewards for each",
                    "Achievement Diary. For completing the Lumbridge and",
                    "Draynor diary you are presented with an explorer's",
                    "ring."
                )
                stage++
            }
            21 -> {
                npc("This ring will become increasingly useful with each", "section of the diary that you complete.")
                stage = 0
            }
            30 -> {
                npc(
                    "You need to complete the task so that they're all ticked",
                    "off then you can claim your reward. Most of them are",
                    "straightforward although you might find some required",
                    "quests to be started, if not finished."
                )
                stage++
            }
            31 -> {
                npc(
                    "To claim the explorer's ring speak to Explorer Jack in ",
                    "Lumbridge, Bob in Bob's Axes in Lumbridge, or myself."
                )
                stage = 0
            }
            40 -> end()
            50 -> {
                npc("Yes I see that, you'll be wanting your", "reward then I assume?")
                stage++
            }
            51 -> {
                player("Yes please.")
                stage++
            }
            52 -> {
                AchievementDiary.flagRewarded(player, DiaryType.LUMBRIDGE, level)
                npc("This ring is a representation of the adventures you", "went on to complete your tasks.")
                stage++
            }
            53 -> {
                player("Wow, thanks!")
                stage = 0
            }
            60 -> {
                AchievementDiary.grantReplacement(player, DiaryType.LUMBRIDGE, level);
                npc("You better be more careful this time.")
                stage = 0
            }
        }
    }
}