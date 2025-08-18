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
class MaryDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MaryDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return MaryDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MARY_810)
    }
}

class MaryDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> sendDialogue(player!!, "They are ignoring you.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl(FacialExpression.ASKING, "How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 5),
                    IfTopic("Did you hear any suspicious noises at all?", 7, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 12, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )

                3 -> npcl("Oh I don't know... Frank was acting kind of funny... After that big argument him and the Lord had the other day by the beehive... so").also { stage ++ }
                4 -> npcl("I guess maybe him... but it's really scary to think someone here might have been responsible. I actually hope it was a burglar...").also { stage = END_DIALOGUE }

                5 -> npcl("I was with Hobbes and Louisa in the Kitchen helping to prepare Lord Sinclair's meal, and then when I took it to his study... I saw... oh, it was horrible... he was...").also { stage++ }
                6 -> sendDialogue(player!!, "She seems to be on the verge of crying. You decide not to push her anymore for details.").also { stage = END_DIALOGUE}

                7 -> npcl("I don't really remember hearing anything out of the ordinary.").also { stage++ }
                8 -> playerl("No sounds of a struggle then?").also { stage++ }
                9 -> npcl("No, I don't remember hearing anything like that.").also { stage++ }
                10 -> playerl("How about the guard dog barking?").also { stage++ }
                11 -> npcl("Oh that horrible dog is always barking at noting but I don't think I did...").also { stage = END_DIALOGUE }

                12 -> npcl("I overheard Anna saying to Stanford that if he didn't do something about the state of his compost heap, she was going to.").also { stage++ }
                13 -> npcl("She really doesn't get on well with Stanford, I really have no idea why. You'd really have to ask her though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}