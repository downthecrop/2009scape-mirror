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
class LouisaDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LouisaDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return LouisaDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOUISA_809)
    }
}

class LouisaDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("I'm far too upset to talk to random people right now.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 6),
                    IfTopic("Did you hear any suspicious noises at all?", 7, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 12, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )

                3 -> npcl("Elizabeth.").also { stage ++ }
                4 -> npcl("Her father confronted her about her constant petty thieving, and was devastated to find she had stolen a silver needle which had meant a lot to him.").also { stage++ }
                5 -> npcl("You could hear their argument from Lumbridge!").also { stage = END_DIALOGUE }

                6 -> npcl("I was right here with Hobbes and Mary. You can't suspect me surely!").also { stage = END_DIALOGUE }

                7 -> npcl("Suspicious? What do you mean suspicious?").also { stage++ }
                8 -> playerl("Any sounds of a struggle with an intruder for example?").also { stage++ }
                9 -> npcl("No, I'm sure I don't recall any such thing.").also { stage++ }
                10 -> playerl("How about the guard dog barking at an intruder?").also { stage++ }
                11 -> npcl("No, I didn't. If you don't have anything else to ask can You go and leave me alone now? I have a lot of cooking to do for this evening.").also { stage = END_DIALOGUE }

                12 -> npcl("I told Carol to buy some from that strange poison salesman and clean the drains before they began to smell any worse. She was the one who blocked them in the first place with a load").also { stage++ }
                13 -> npcl("of beans that she bought for some reason. There were far too many to eat, and they were almost rotten when she bought them anyway! You'd really have to ask her though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}