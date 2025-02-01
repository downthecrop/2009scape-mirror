package content.region.kandarin.witchhaven.quest.seaslug

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.skill.Skills

class CarolineDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(SeaSlug.questName, 0)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.SAD, "Is there any chance you could help me?")
                .playerl(FacialExpression.THINKING, "What's wrong?")
                .npcl("It's my husband, he works on a fishing platform. Once a month he takes our son, Kennith, out with him.")
                .npcl(FacialExpression.THINKING, "They usually write to me regularly, but I've heard nothing all week. It's very strange.")
                .playerl("Maybe the post was lost!")
                .npcl(FacialExpression.THINKING, "Maybe, but no-one's heard from the other fishermen on the platform. Their families are becoming quite concerned.")
                .branch { player ->
                    return@branch if (hasLevelStat(player, Skills.FIREMAKING, 30)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .npcl("However, I don't think you are ready to visit the platform.")
                            .line("You need Level 30 Firemaking to start the Sea Slug quest.")
                            .end()
                    return@let branch.onValue(1)
                }
                .npcl(FacialExpression.HALF_THINKING, "Is there any chance you could visit the platform and find out what's going on?")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("I suppose so, how do I get there?")
                            .npcl("That's very good of you @name. My friend Holgart will take you there.")
                            .playerl("Ok, I'll go and see if they're ok.")
                            .npcl("I'll reward you for your time. It'll give me peace of mind to know Kennith and my husband, Kent, are safe.")
                            .endWith() { df, player ->
                                if(getQuestStage(player, SeaSlug.questName) == 0) {
                                    setQuestStage(player, SeaSlug.questName, 1)
                                }
                            }
                    optionBuilder.option_playerl("I'm sorry, I'm too busy.")
                            .npcl(FacialExpression.SAD, "That's a shame.")
                            .playerl("Bye.")
                            .npcl("Bye.")
                            .end()
                }

        b.onQuestStages(SeaSlug.questName, 1,2,3,4,5,6,7,8,9,10)
                .playerl("Hello Caroline.")
                .npcl("Brave @name, have you any news about my son and his father?")
                .playerl("I'm working on it now Caroline.")
                .npcl("Please bring them back safe and sound.")
                .playerl("I'll do my best.")
                .end()

        b.onQuestStages(SeaSlug.questName, 11)
                .playerl("Hello.")
                .npcl("Brave @name, you've returned!")
                .npcl("Kennith told me about the strange goings-on at the platform. I had no idea it was so serious.")
                .npcl("I could have lost my son and my husband if it wasn't for you.")
                .playerl("We found Kent stranded on an island.")
                .npcl("Yes. Holgart told me and sent a rescue party out. Kent's back home now, resting with Kennith. I don't think he'll be doing any fishing for a while.")
                .npcl("Here, take these Oyster pearls as a reward. They're worth quite a bit and can be used to make lethal crossbow bolts.")
                .playerl(FacialExpression.FRIENDLY, "Thanks!")
                .npcl(FacialExpression.FRIENDLY, "Thank you. Take care of yourself @name.")
                .endWith() { df, player ->
                    if(getQuestStage(player, SeaSlug.questName) == 11) {
                        finishQuest(player, SeaSlug.questName)
                    }
                }
    }
}
