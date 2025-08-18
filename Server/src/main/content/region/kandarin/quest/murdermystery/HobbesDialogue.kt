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
class HobbesDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, HobbesDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return HobbesDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOBBES_808)
    }
}

class HobbesDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("This is private property! Please leave!").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 6),
                    IfTopic("Did you hear any suspicious noises at all?", 7, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 12, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )

                3 -> npcl("Well, in my considered opinion it must be David. The man is nothing more than a bully And I happen to know that poor Lord Sinclair and David had a massive argument in the living").also { stage++ }
                4 -> npcl("room about the way he treats the staff, the other day. I did not intend to overhear their conversation, but they were shouting so loudly I could not help but Overhear it. David definitely used the words").also { stage++ }
                5 -> npcl("'I am going to kill you!' as well. I think he should be the prime suspect. He has a nasty temper that one.").also { stage = END_DIALOGUE }

                6 -> npcl("I was assisting the cook with the evening meal. I gave Mary His Lordships' dinner, and sent her to take it to him, then heard the scream as she found the body.").also { stage = END_DIALOGUE }

                7 -> npcl("How do you mean 'suspicious'?").also { stage++ }
                8 -> playerl("Any sounds of a struggle with Lord Sinclair?").also { stage++ }
                9 -> npcl("No, I definitely didn't hear anything like that.").also { stage++ }
                10 -> playerl("How about the guard dog barking at all?").also { stage++ }
                11 -> npcl("You know, now you come to mention it I don't believe I did. I suppose that is Proof enough that it could not have been an intruder who is responsible.").also { stage = END_DIALOGUE }

                12 -> npcl("Well, I do know that Elizabeth was extremely annoyed by the mosquito nest under the fountain in the garden, and was going to do something about it. I suspect any poison she bought would have been").also { stage++ }
                13 -> npcl ("enough to get rid of it. A Good job, too. I hate mosquitos.").also { stage++ }
                14 -> playerl("Yeah, so do I.").also { stage++ }
                15 -> npcl("You'd really have to ask her though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}

