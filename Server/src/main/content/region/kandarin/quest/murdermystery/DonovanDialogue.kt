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
class DonovanDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DonovanDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return DonovanDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONOVAN_THE_FAMILY_HANDYMAN_806)
    }
}

class DonovanDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("I have no interest in talking to gawkers.").also { stage = END_DIALOGUE }
            1 -> when (stage) {
                0 -> playerl("I'm here to help the guards with their investigation.").also { stage++ }
                1 -> npcl("How can I help?").also { stage++ }
                2 -> showTopics(
                    Topic("Who do you think is responsible?", 3),
                    Topic( "Where were you at the time of the murder?", 5),
                    IfTopic("Did you hear any suspicious noises at all?", 6, getAttribute(player!!, attributeNoiseClue, false)),
                    IfTopic("Do you know why so much poison was bought recently?", 9, getAttribute(player!!, attributePoisonClue, 0) > 0)
                )
                3 -> npcl("Oh... I really couldn't say. I wouldn't really want to point any fingers at anybody. If I had to make a guess I'd have to say it was probably Bob though.").also { stage++ }
                4 -> npcl("I saw him arguing with Lord Sinclair about some missing silverware from the Kitchen. It was a very heated argument.").also { stage = END_DIALOGUE }

                5 -> npcl("Me? I was sound asleep here in the servants Quarters. It's very hard work as a handyman around here. There's always something to do!").also { stage = END_DIALOGUE }

                6 -> npcl("Hmmm... No, I didn't, but I sleep very soundly at night.").also { stage++ }
                7 -> playerl("So you didn't hear any sounds of a struggle or any barking from the guard dog next to his study window?").also { stage++ }
                8 -> npcl("Now you mention it, no. It is odd I didn't hear anything like that. But I do sleep very soundly as I said and wouldn't necessarily have heard it if there was any such noise.").also { stage = END_DIALOGUE }

                9 -> npcl("Well, I do know Frank bought some poison recently to clean the family crest that's outside.").also { stage++ }
                10 -> npcl("It's very old and rusty, and I couldn't clean it myself, so he said he would buy some cleaner and clean it himself. He probably just got some from that Poison Salesman who came to the door the other day...").also { stage++ }
                11 -> npcl("You'd really have to ask him though.").also { stage = END_DIALOGUE }
            }
            100 -> npcl("Thank you for all your help in solving the murder.").also { stage = END_DIALOGUE }
        }
    }
}

