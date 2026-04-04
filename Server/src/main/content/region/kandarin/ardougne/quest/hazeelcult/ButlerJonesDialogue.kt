package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import core.api.getQuestStage
import core.api.inEquipmentOrInventory
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class ButlerJonesDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "Hello, adventurer. Such a terrible shame about Scruffy. I wonder if the family will ever fully recover.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Anyway, I hear your quest is going well.").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "Really?").also { stage++ }
                4 -> npcl(FacialExpression.ANNOYED, "Oh yes. Do keep up the good work.").also { stage = END_DIALOGUE }
            }

            // stage 4 - alomone either fought or he tells you he needs scroll
            (questStage == 4 && mahjarratArc(player) && inEquipmentOrInventory(player, Items.HAZEELS_MARK_2406)) -> when (stage) {
                0 -> npcl(FacialExpression.NEUTRAL, "Hello again friend. I see you have the mark now; you should keep it covered up.").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "That? Oh, it's nothing. Just an old amulet.").also { stage++ }
                2 -> npcl(FacialExpression.NEUTRAL, "You don't have to pretend with me, friend! Our cause is one and the same; the sooner Lord Hazeel is avenged the better for this city... and for us!").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "So do you have any idea where the scroll with the enchantment is?").also { stage++ }
                4 -> npcl(FacialExpression.ANNOYED, "No idea I'm afraid. I KNOW it's somewhere in this house, but for the life of me I can't find it anywhere. I've searched high and low for it!").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "And Sir Ceril doesn't suspect a thing?").also { stage++ }
                6 -> npcl(FacialExpression.ANNOYED, "Ha! That silly old fool? He can't see the forest for the trees.").also { stage++ }
                7 -> playerl(FacialExpression.FRIENDLY, "I'll keep on looking then...").also { stage = END_DIALOGUE }
            }

            // stage 0 - unstarted
            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 5 - either returning the armour, or finding the scroll
            // also stage 4 if you are carnillean side
            (questStage in 0..5) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Hello. How are you today?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Good thank you, and yourself?").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Very well, thank you.").also { stage = END_DIALOGUE }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello stranger.").also { stage = 1 }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 6 }
                    }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "It's an honour to be in your presence again, adventurer. I hope things are well?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not bad, thanks. Yourself?").also { stage++ }
                3 -> npcl(FacialExpression.ANNOYED, "Unfortunately, I am still forced to deal with this insufferable family. Many generations have passed, but they are still the enemy. As such, they must be kept a close eye on.").also { stage++ }
                4 -> npcl(FacialExpression.ANNOYED, "Still, I have no doubt that the time will soon come for me to leave this place. Our lord will certainly have need of me elsewhere once his current work is complete.").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "I see. Well good luck with it all.").also { stage = END_DIALOGUE }

                6 -> npcl(FacialExpression.ANNOYED, "Why hello there.").also { stage++ }
                7 -> playerl(FacialExpression.FRIENDLY, "I take it you're the new butler...?").also { stage++ }
                8 -> npcl(FacialExpression.ANNOYED, "That's right. I hear that they had some problems with the last one.").also { stage++ }
                9 -> playerl(FacialExpression.FRIENDLY, "Yes, you could say that...").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BUTLER_JONES_890)
}
