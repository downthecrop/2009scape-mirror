package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonBob
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class BobDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, BobDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return BobDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOB_815, NPCs.BOB_6193)
    }
}

class BobDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl("I suppose I had better talk to you then.").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you when the murder happened?", 4),
                    IfTopic(FacialExpression.THINKING, "Do you recognise this thread?", 7, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810)),
                    IfTopic("Why'd you buy poison the other day?", 10, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                3 -> npcl("I don't really care as long as no one thinks it's me. Maybe it was that strange poison seller who headed towards the seers village.").also { stage = END_DIALOGUE }

                4 -> npcl("I was walking by myself in the garden.").also { stage++ }
                5 -> playerl("And can anyone vouch for that?").also { stage++ }
                6 -> npcl("No. But I was.").also { stage = END_DIALOGUE }

                7 -> sendDialogue(player!!, "You show him the thread you discovered.")
                    .also { if (inInventory(player!!, Items.CRIMINALS_THREAD_1808)) stage++ else stage = 9 }
                8 -> npcl("It's some red thread. I suppose you think that's some kind of clue? It looks like the material my trousers are made of.").also { stage = END_DIALOGUE }

                9 -> npcl(FacialExpression.THINKING, "It's some thread. Great clue. No, really.").also { stage = END_DIALOGUE }

                10 -> npcl(FacialExpression.THINKING, "What's it to you anyway?").also { stage++ }
                11 -> npcl(FacialExpression.ANGRY, "If you absolutely must know, we had a problem with the beehive in the garden, and as all of our servants are so pathetically useless, I decided I would deal with it myself. So I did.")
                    .also { setAttribute(player!!, attributeAskPoisonBob, true)}
                    .also { stage = END_DIALOGUE }
            }
            100 -> npcl("Apparently you aren't as stupid as you look.").also { stage = END_DIALOGUE }
        }
    }
}