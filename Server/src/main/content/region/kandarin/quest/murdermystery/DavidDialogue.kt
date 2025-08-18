package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonDavid
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class DavidDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DavidDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return DavidDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAVID_817, NPCs.DAVID_6195)
    }
}

class DavidDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl("And? Make this quick. I have better things to do than be interrogated by halfwits all day.").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you when the murder happened?", 5),
                    IfTopic("Do you recognise this thread?", 6, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810)),
                    IfTopic("Why'd you buy poison the other day?", 9, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                3 -> npcl("I don't really know or care. Frankly, the old man deserved to die.").also { stage++ }
                4 -> npcl("There was a suspicious red headed man who came to the house the other day selling poison now I think about it. Last I saw he was headed towards the tavern in the Seers village.").also { stage = END_DIALOGUE }

                5 -> npcl("That is none of your business. Are we finished now, or are you just going to stand there irritating me with your idiotic questions all day?").also { stage = END_DIALOGUE }

                6 -> sendDialogue(player!!, "You show him the thread you found on the study window.")
                    .also { if (inInventory(player!!, Items.CRIMINALS_THREAD_1809)) stage++ else stage = 8 }
                7 -> npcl("It's some Green thread, like my trousers are made of. Are you finished? I'm not sure which I dislike more bout you, your face or your general bad odour.").also { stage = END_DIALOGUE }

                8 -> npcl("No. Can I go yet? Your face irritates me.").also { stage = END_DIALOGUE }

                9 -> npcl(FacialExpression.ANGRY, "There was a nest of spiders upstairs between the two servants' quarters. Obviously I had to kill them before our pathetic servants whined at my father some more.").also { stage++ }
                10 -> npcl(FacialExpression.THINKING, "Honestly, it's like they expect to be treated like royalty! If I had my way I would fire the whole workshy lot of them!")
                    .also { setAttribute(player!!, attributeAskPoisonDavid, true)}
                    .also { stage = END_DIALOGUE }
            }
            100 -> npcl("Apparently you aren't as stupid as you look.").also { stage = END_DIALOGUE }
        }
    }
}