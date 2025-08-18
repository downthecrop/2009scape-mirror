package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonAnna
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class AnnaDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, AnnaDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return AnnaDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ANNA_814, NPCs.ANNA_6192)
    }
}

class AnnaDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "Oh really? What do you want to know then?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you when the murder happened?", 6),
                    IfTopic("Do you recognise this thread?", 7, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810)),
                    IfTopic("Why'd you buy poison the other day?", 10, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                3 -> npcl("It was clearly an intruder.").also { stage++ }
                4 -> playerl("Well, I don't think it was.").also { stage++ }
                5 -> npcl("It was one of our lazy servants then.").also { stage = END_DIALOGUE }

                6 -> npcl("In the library. No one else was there so you'll just have to take my word for it.").also { stage = END_DIALOGUE }

                7 -> sendDialogue(player!!, "You show Anna the thread from the study.")
                        .also { if (inInventory(player!!, Items.CRIMINALS_THREAD_1809)) stage++ else stage = 9 }
                8 -> npcl("It's some Green thread. It's not exactly uncommon is it? My trousers are made of the same material.").also { stage = END_DIALOGUE }

                9 -> npcl("Not really, no. Thread is fairly common.").also { stage = END_DIALOGUE }

                10 -> npcl(FacialExpression.ANNOYED, "That useless Gardener Stanford let his compost heap fester. It's an eyesore to the garden! So I bought some poison from a travelling salesman so that I could kill off some of the wildlife living in it.")
                    .also { setAttribute(player!!, attributeAskPoisonAnna, true)}
                    .also { stage = END_DIALOGUE }
            }
            100 -> npcl("Apparently you aren't as stupid as you look.").also { stage = END_DIALOGUE }
        }
    }
}