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
class StanfordDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StanfordDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return StanfordDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STANFORD_811)
    }
}

class StanfordDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("Have you no shame? We are all grieving at the moment.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 5),
                    IfTopic("Did you hear any suspicious noises at all?", 6, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 11, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )

                3 -> npcl("It was Anna. She is seriously unbalanced. She trashed the garden once then tried to blame it on me! I bet it was her. It's just the kind of thing she'd do!").also { stage++ }
                4 -> npcl("She really hates me and was arguing with Lord Sinclair about trashing the garden a few days ago.").also { stage = END_DIALOGUE }

                5 -> npcl("Right here, by my little shed. It's very cosy to sit and think in.").also { stage = END_DIALOGUE }

                6 -> npcl("Not that I remember.").also { stage++ }
                7 -> playerl("So no sounds of a struggle between Lord Sinclair and an intruder?").also { stage++ }
                8 -> npcl("Not to the best of my recollection.").also { stage++ }
                9 -> playerl("How about the guard dog barking?").also { stage++ }
                10 -> npcl("Not that I can recall.").also { stage = END_DIALOGUE }

                11 -> npcl("Well, Bob mentioned to me the other day he wanted to get rid of the bees in that hive over there. I think I saw him buying poison").also { stage++ }
                12 -> npcl("from that poison salesman the other day. I assume it was to sort out those bees. You'd really have to ask him though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}