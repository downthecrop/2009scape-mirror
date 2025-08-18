package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeNoiseClue
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class PierreDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, PierreDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return PierreDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PIERRE_807)
    }
}

class PierreDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("The Guards told me not to talk to anyone.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 5),
                    IfTopic("Did you hear any suspicious noises at all?", 6, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 11, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )

                3 -> npcl("Honestly? I think it was Carol.").also { stage++ }
                4 -> npcl("I saw her in a huge argument with Lord Sinclair in the library the other day. It was something to do with stolen books. She definitely seemed upset enough to have done it afterwards.").also { stage = END_DIALOGUE }

                5 -> npcl("I was in town at the inn. When I got back the house was swarming with guards who told me what had happened. Sorry.").also { stage = END_DIALOGUE }

                6 -> npcl("Well, like what?").also { stage++ }
                7 -> playerl("Any sounds of a struggle with Lord Sinclair?").also { stage++ }
                8 -> npcl("No, I don't remember hearing anything like that.").also { stage++ }
                9 -> playerl("How about the guard dog barking at all?").also { stage++ }
                10 -> npcl("I hear him bark all the time. It's one of his favourite things to do. I can't say I did the night of the murder though as I wasn't close enough to hear either way.").also { stage = END_DIALOGUE }

                11 -> npcl("Well, I know David said that he was going to do something about the spiders' nest that's between the two servants' quarters upstairs.").also { stage++ }
                12 -> npcl("He made a big deal about it to Mary the Maid, calling her useless and incompetent. I felt quite sorry for her actually. You'd really have to ask him though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}