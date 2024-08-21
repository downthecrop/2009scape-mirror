package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.*

class SirAmikVarzeDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.onQuestStages(RecruitmentDrive.questName, 0)
                .npcl(FacialExpression.FRIENDLY,"Hello, friend!")
                .playerl(FacialExpression.THINKING, "Do you have any other quests for me to do?")
                .branch { player -> if(isQuestComplete(player, "Black Knights' Fortress") && isQuestComplete(player, "Druidic Ritual")) { 1 } else { 0 } }
                .let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                            .npcl(FacialExpression.THINKING, "A quest? Alas I do not have any quests I can offer you at this time.")
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .npc("Quests, eh?", "Well, I don't have anything on the go at the moment,", "but there is an organisation that is always looking for", "capable adventurers to assist them.")
                .npc(FacialExpression.FRIENDLY,"Your excellent work sorting out those Black Knights", "means I will happily write you a letter of", "recommendation.")
                .npc("Would you like me to put your name forwards to", "them?")
                .options().let { optionBuilder ->
                    optionBuilder.option ("Yes please")
                            .playerl("Sure thing Sir Amik, sign me up!")
                            .npc(FacialExpression.SUSPICIOUS,"Erm, well, this is a little embarrassing, I already HAVE", "put you forward as a potential member.")
                            .npc("They are the Temple Knights, and you are to", "meet Sir Tiffy Cashien in Falador park for testing", "immediately.")
                            .playerl("Okey dokey, I'll go do that then.")
                            .endWith { _, player ->
                                if(getQuestStage(player, RecruitmentDrive.questName) == 0) {
                                    setAttribute(player, RecruitmentDrive.attributeOriginalGender, player.isMale)
                                    setQuestStage(player, RecruitmentDrive.questName, 1)
                                }
                            }
                    optionBuilder.option_playerl("No thanks")
                            .end()
                    optionBuilder.option("Tell me about this organization...")
                            .npc(FacialExpression.SUSPICIOUS,"I cannot tell you much...", "They are called the Temple Knights, and are an", "organisation that was founded by Saradomin personally", "many centuries ago.")
                            .npc("There are many rumours and fables about their works and", "actions, but official records of their presence are non-", "existent.")
                            .npc("It is a secret organisation of extraordinary power and", "resourcefulness...")
                            .npc("Let me put it this way:", "Should you decide to take them up on their generous", "offer to join, you will find yourself in an advantageous", "position that many in this world would envy, and that few")
                            .npc("are called to occupy.")
                            .playerl("Well, that wasn't quite as helpful as I thought it would be...but thanks anyway, I guess.")
                            .end()
                }

        b.onQuestStages(RecruitmentDrive.questName, 1,2,3,4)
                .npcl(FacialExpression.FRIENDLY,"Hello, friend!")
                .playerl(FacialExpression.THINKING, "Can I just skip the test to become a Temple Knight?")
                .npcl("No, I'm afraid not. I suggest you go meet Sir Tiffy in Falador Park, he will be expecting you.")
                .end()

        // This should be after the Wanted Quest, but is the placeholder until that quest is implemented.
        b.onQuestStages(RecruitmentDrive.questName, 100)
                .npcl(FacialExpression.FRIENDLY,"Hello, friend!")
                .npcl(FacialExpression.FRIENDLY,"Well @name, now that you are a White Knight, I expect you should be out there hunting Black Knights for us!")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("Can you explain the White Knight honour system again?")
                            .npcl("Sadly we are not as rich as we once were, and there are many White Knights who foolishly lose their combat equipment.")
                            .npcl("We do not think it fair to make a profit from our brethren, so we will sell you equipment at cost, and rebuy it at the same cost, but we will only sell equipment to those we consider responsible enough to")
                            .npcl("wield it correctly.")
                            .npcl("By killing Black Knights, you will increase your reputation with us, by killing White Knights we will obviously think less of you.")
                            .npcl("You can check your White Knight reputation level by looking at your quest journal for the Wanted! Quest, or Sir Vyvin will let you know what level you are at when you go to purchase equipment.")
                            .npcl("Sir Vyvin can be found in Falador Castle, and he will sell you any equipment appropriate to your reputation level.")
                            .npcl("Have fun, and go kill some Black Knights for me!")
                            .playerl("Okay Amik, thanks for explaining!")
                            .end()

                    optionBuilder.option("Okay, bye!")
                            .playerl("Okay, 'bye then Amik!")
                            .end()
                }
    }
}