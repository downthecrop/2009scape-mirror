package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonFrank
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class FrankDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, FrankDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return FrankDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRANK_819, NPCs.FRANK_6197)
    }
}

class FrankDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl("Good for you. Now what do you want?").also { stage++ }
                2 -> npcl(FacialExpression.SAD, "...And can you spare me any money? I'm a little short...").also { stage++ }
                3 -> showTopics(
                    Topic("Who do you think is responsible?", 4),
                    Topic( "Where were you when the murder happened?", 5),
                    IfTopic("Do you recognise this thread?", 6, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810)),
                    IfTopic("Why'd you buy poison the other day?", 9, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                4 -> npcl("I don't know. You don't know how long it takes an inheritance to come through do you? I could really use that money pretty soon...").also { stage = END_DIALOGUE }

                5 -> npcl("I don't know, somewhere around here probably. Could you spare me a few coins? I'll be able to pay you back double tomorrow it's just there's this poker night tonight in town...").also { stage = END_DIALOGUE }

                6 -> sendDialogue(player!!, "Frank examines the thread from the crime scene.")
                    .also { if (inInventory(player!!, Items.CRIMINALS_THREAD_1810)) stage++ else stage = 8 }
                7 -> npcl("It kind of looks like the same material as my trousers are made of... same colour anyway. Think it's worth anything? Can I have it? Or just some money?").also { stage = END_DIALOGUE }

                8 -> npcl("It looks like thread to me, but I'm not exactly an expert. Is it worth something? Can I have it? Actually, can you spare me a few gold?").also { stage = END_DIALOGUE }

                9 -> npcl("Would you like to buy some? I'm kind of strapped for cash right now. I'll sell it to you cheap. It's hardly been used at all.").also { stage++ }
                10 -> npcl("I just used a bit to clean that family crest outside up a bit. Do you think I could get much money for the family crest, actually? It's cleaned up a bit now.")
                    .also { setAttribute(player!!, attributeAskPoisonFrank, true)}
                    .also { stage = END_DIALOGUE }
            }
            100 -> npcl("Apparently you aren't as stupid as you look.").also { stage = END_DIALOGUE }
        }
    }
}