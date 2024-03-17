package content.region.misthalin.varrock.quest.shieldofarrav

import content.region.desert.quest.thegolem.CuratorHaigHalenGolemDialogue
import content.region.misthalin.digsite.quest.thedigsite.TheDigSite
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class CuratorHaigHalenDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FacialExpression.NEUTRAL, "Welcome to Varrock Museum!").also {
                if (player.getQuestRepository().points >= 50 && !player.achievementDiaryManager.hasCompletedTask(DiaryType.VARROCK, 0, 12)) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 12)
                }
                if (getQuestStage(player, TheDigSite.questName) == 1 && inInventory(player, Items.UNSTAMPED_LETTER_682) ) {
                    stage = 11 // Couldn't do a dialogueFile for digsite as it needs to resume the topic after.
                }
                stage++
            }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Have you any interesting news?", 2),
                    Topic(FacialExpression.FRIENDLY, "Do you know where I could find any treasure?", 8),
                    IfTopic<Any?>(FacialExpression.FRIENDLY, "I've lost the letter of recommendation.", 18,  getQuestStage(player, TheDigSite.questName) == 2 && !inInventory(player, Items.SEALED_LETTER_683)),
                    IfTopic<Any?>("I have the Shield of Arrav", CuratorHaigHalenSOADialogue(),
                            getQuestStage(player, "Shield of Arrav") == 70, false),
                    IfTopic<Any?>("I'm looking for a statuette recovered from the city of Uzer.", CuratorHaigHalenGolemDialogue(),
                            getQuestStage(player, "The Golem") == 3, false)
            )
            2 -> npcl(FacialExpression.FRIENDLY, "Yes, we found a rather interesting island to the north of Morytania. We believe that it may be of archaeological significance.").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "Oh? That sounds interesting.").also { stage++ }
            4 -> npcl(FacialExpression.FRIENDLY, "Indeed. I suspect we'll be looking for qualified archaeologists once we have constructed our canal and barge.").also { stage++ }
            5 -> playerl(FacialExpression.FRIENDLY, "Would I qualify then?").also { stage++ }
            // These three dialogues are based on what he would say depending on what stage of the quest/kudos you have. I'm too lazy to implement this part as the kudos system isn't implemented yet and this is all cosmetic.
            // "Unfortunately, you haven't passed your Earth Science", "exams so you aren't qualified to help us on the dig. If" "you're interested, visit the Exam Centre on the Dig Site", "to the east and talk to the examiners there."
            // "Unfortunately, you haven't earned enough kudos yet to", "help us on the dig. If you're interested in helping us", "out and getting that Kudos, simply help out around the", "Museum. You can find out more at the information"   ANOTHER DIALOGUE "booth."   PLAYER "Ok, thanks.
            // After 150 kudos I think "Yes indeed. You've helped us a great deal around the", "Museum and you have the necessary qualifications from", "the Earth Sciences exams. When the canal is ready,", "we'll let you know."
            6 -> npcl(FacialExpression.FRIENDLY, "You've certainly done a lot to help out Varrock Museum, so we'd be silly not to ask for your expertise.").also { stage++ }
            7 -> playerl(FacialExpression.FRIENDLY, "Thank you. I'll look forward to it!").also {
                stage = END_DIALOGUE
            }
            8 -> npcl(FacialExpression.FRIENDLY, "Look around you! This museum is full of treasures!").also { stage++ }
            9 -> playerl(FacialExpression.FRIENDLY, "No, I meant treasures for ME.").also { stage++ }
            10 -> npcl(FacialExpression.FRIENDLY, "Any treasures this museum knows about it goes to great lengths to acquire.").also {
                stage = END_DIALOGUE
            }
            11 -> player(FacialExpression.FRIENDLY, "I've been given this letter by an examiner at the Dig", "Site. Can you stamp this for me?").also { stage++ }
            12 -> npc(FacialExpression.FRIENDLY, "What have we here? A letter of recommendation", "indeed...").also { stage++ }
            // The next two dialogues are supposed to expand based on the player.username length, but I have no time for this shit.
            13 -> npcl(FacialExpression.FRIENDLY, "The letter here says you name is ${player.username}. Well ${player.username}, I wouldn't normally do this for just anyone, but as you did us such a great service with the Shield of Arrav I don't see why not.").also { stage++ }
            14 -> npcl(FacialExpression.FRIENDLY, "Run this letter back to the Examiner to begin your adventure into the world of Earth Sciences. Enjoy your studies, Student!").also { stage++ }
            15 -> npc(FacialExpression.FRIENDLY, "There you go, good luck student... Be sure to come", "back and show me your certificates. I would like to see", "how you get on.").also {
                if (removeItem(player, Items.UNSTAMPED_LETTER_682)) {
                    addItemOrDrop(player, Items.SEALED_LETTER_683)
                }
                stage++
            }
            16 -> playerl(FacialExpression.FRIENDLY, "Ok, I will. Thanks, see you later.").also {
                if(getQuestStage(player, TheDigSite.questName) == 1) {
                    setQuestStage(player, TheDigSite.questName, 2)
                }
                stage = 1
            }
            18 -> npc(FacialExpression.FRIENDLY, "Yes, I saw you drop it as you walked off last time. Here it is.").also {
                addItemOrDrop(player, Items.SEALED_LETTER_683)
                stage = 1
            }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return CuratorHaigHalenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CURATOR_HAIG_HALEN_646)
    }
}
