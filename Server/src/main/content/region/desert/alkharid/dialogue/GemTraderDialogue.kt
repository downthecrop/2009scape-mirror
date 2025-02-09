package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import content.data.Quests
import core.api.getQuestStage
import core.api.setQuestStage

/**
 * Represents the gem trader Dialogue plugin
 * @author plex
 * @version 2.0
 */

@Initializable
class GemTraderDialogue (player: Player? = null): DialoguePlugin(player){
    override fun newInstance(player: Player?): DialoguePlugin {
        return  GemTraderDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        npc("Good day to you, traveller.", "Would you be interested in buying some gems?")
        stage = if (getQuestStage(player, Quests.FAMILY_CREST) == 12) 1 else 2
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> options("Yes, please.", "No, thank you.", "I'm in search of a man named Avan Fitzharmon").also{
                stage = 10
            }

            2 -> options("Yes, please.", "No, thank you.").also{
                stage = 20
            }

            10 -> when(buttonId){
                1 -> npc.openShop(player).also { stage = 1000 }
                2 -> player("No, thank you.").also{stage = 1000}
                3 -> npc("Fitzharmon, eh? Hmmm... If I'm not mistaken, ",
                        "that's the family name of a member ",
                        "of the Varrockian nobility.").also{stage = 100}
            }

            20 -> when(buttonId){
                1 -> npc.openShop(player).also { stage = 1000 }
                2 -> player("No, thank you.").also{stage = 1000}
            }

            100 -> npc("You know, I HAVE seen someone of that" ,
                    " persuasion around here recently... " ,
                    "wearing a 'poncey' yellow cape, he was.").also{stage++}

            101 -> npc("Came in here all la-di-dah, high and mighty,",
                    "asking for jewellery made from 'perfect gold' - " ,
                    "whatever that is - like 'normal' gold just isn't " ,
                    "good enough for 'little lord fancy pants' there!").also{stage ++}

            102 -> npc("I told him to head to the desert 'cos " ,
                    "I know there's gold out there, in them there sand dunes. " ,
                    "And if it's not up to his lordship's " ,
                    "high standards of 'gold perfection', then...").also{stage++}
            103 ->  npc("Well, maybe we'll all get lucky ",
                    "and the scorpions will deal with him.").also{
                        stage = 1000
                setQuestStage(player, Quests.FAMILY_CREST, 13)
            }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GEM_TRADER_540)
    }


}