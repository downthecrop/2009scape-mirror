package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonCarol
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class CarolDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, CarolDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return CarolDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROL_816, NPCs.CAROL_6194)
    }
}

class CarolDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl("Well, ask what you want to know then.").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you when the murder happened?", 4),
                    IfTopic("Do you recognise this thread?", 5, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810)),
                    IfTopic("Why'd you buy poison the other day?", 8, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                3 -> npcl("I don't know. I think it's very convenient that you have arrived here so soon after it happened. Maybe it was you.").also { stage = END_DIALOGUE }

                4 -> npcl("Why? Are you accusing me of something? You seem to have a very high opinion of yourself. I was in my room if you must know, alone.").also { stage = END_DIALOGUE }

                5 -> sendDialogue(player!!, "You show Carol the thread found at the crime scene.")
                    .also { if (inInventory(player!!, Items.CRIMINALS_THREAD_1808)) stage++ else stage = 7 }
                6 -> npcl("It's some red thread... it kind of looks like the Same material as my trousers. But obviously it's not.").also { stage = END_DIALOGUE }

                7 -> npcl("It's some thread. Sorry, do you have a point here? Or do you just enjoy wasting peoples time?").also { stage = END_DIALOGUE }

                8 -> npcl(FacialExpression.THINKING, "I don't see what on earth it has to do with you, but the drain outside was").also { stage++ }
                9 -> npcl(FacialExpression.ANNOYED, "blocked, and as nobody else here has the intelligence to even unblock a simple drain I felt I had to do it myself.")
                    .also { setAttribute(player!!, attributeAskPoisonCarol, true)}
                    .also { stage = END_DIALOGUE }
            }
            100 -> npcl("Apparently you aren't as stupid as you look.").also { stage = END_DIALOGUE }
        }
    }
}