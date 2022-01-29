package rs09.game.content.dialogue.region.lletya

import api.setQuestStage
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class ArchaeologistDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!player.questRepository.hasStarted("Desert Treasure")) {
            if (player.questRepository.isComplete("The Digsite Quest") &&
                player.questRepository.isComplete("The Tourist Trap") &&
                player.questRepository.isComplete("The Temple of Ikov") &&
                player.questRepository.isComplete("Priest In Peril") &&
                player.questRepository.isComplete("Waterfall Quest") &&
                player.questRepository.isComplete("Troll Stronghold") &&
                player.skills.getStaticLevel(Skills.SLAYER) >= 10 &&
                player.skills.getStaticLevel(Skills.FIREMAKING) >= 50 &&
                player.skills.getStaticLevel(Skills.MAGIC) >= 50 &&
                player.skills.getStaticLevel(Skills.THIEVING) >= 53 ) {
                    player(FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
            } else {
                    player(FacialExpression.FRIENDLY,"Hello there.").also { stage = 999 }
            }

        } else {

        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            999 -> sendDialogue("He seems to be lost in his own thoughts...").also { stage = 99 }

            0 -> npc(FacialExpression.FRIENDLY, "Howdy stranger. What brings you out to these parts?").also { stage++ }
            1 -> options("What are you doing here?", "Do you have any quests?", "Who are you?", "Nothing really.").also { stage++ }
            2 -> when (buttonId) {
                //1 -> todo
                2 -> player(FacialExpression.FRIENDLY, "Do you have any quests?", "Call it a hunch, but you look like the type of man who", "might...").also { stage = 20 }
                //3 -> todo
                //4 -> todo
            }

            20 -> npc(FacialExpression.HALF_THINKING, "Well, it's funny you should say that.",
            "I'm not sure if I would really call it a quest as such,",
            "but I found this ancient stone tablet in one of my",
            "excavations, and it would really help me out if you").also { stage++ }
            21 -> npc(FacialExpression.FRIENDLY, "could go and take it back to the digsite for me and get",
                "it examined.").also { stage++ }
            22 -> npc(FacialExpression.NEUTRAL, "It's very old, and I don't recognise and of the",
            "inscriptions on it.").also { stage++ }
            23 -> options("Yes, I'll help you.", "No thanks, I don't want to help.").also { stage++ }
            24 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Sure, I was heading that way anyways.",
                "Any particular person at the digsite you want me to",
                "talk to?").also { stage = 30 }
                // 2 -> todo
            }

            30 -> npc(FacialExpression.NEUTRAL, "His name's Terry Balando. Give it to nobody but him.",
            "I'm sorry, I can't entrust you with the actual tablet I",
            "found, but it is far too valuable to give away, but I took",
            "these etchings - they should be enough for him to make").also { stage++ }
            31 -> npc(FacialExpression.NEUTRAL, "a preliminary translation on.",
            "Come back and let me know what he says, I would hate",
            "to waste my time excavating anything that isn't worth",
            "my time as a world famous archaeologist!").also {
                player.questRepository.getQuest("Desert Treasure").start(player)
                setQuestStage(player, "Desert Treasure", 1)
                stage = 99
            }




            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ArchaeologistDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARCHAEOLOGIST_1918)
    }
}
