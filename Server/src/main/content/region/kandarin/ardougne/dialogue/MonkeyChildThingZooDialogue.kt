package content.region.kandarin.ardougne.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class MonkeyChildThingZooDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"").also { stage = 0 }
        if (!player.equipment.contains(Items.MSPEAK_AMULET_4021, 1)) {
            npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Eeekeek ookeek!").also { stage = 99 }
        } else {
            var a = 1..5
            when(a.random()) {
                1 -> npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Arr!").also { stage = 10 }
                2 -> npcl(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Let me go, can't ye hear them? Howlin' in the dark...").also { stage = 20 }
                3 -> npcl(core.game.dialogue.FacialExpression.OLD_DEFAULT, "I'm not goin' back in that brewery, not fer all the Bitternuts I can carry!").also { stage = 99 }
                4 -> npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Are ye here for...the stuff?").also { stage = 30 }
                5 -> npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "Arr! Yer messin with me monkey plunder!").also { stage = 40 }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            10 -> player(core.game.dialogue.FacialExpression.JOLLY, "Arr!").also { stage++ }
            11 -> npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Arr!").also { stage++ }
            12 -> player(core.game.dialogue.FacialExpression.JOLLY, "Arr!").also { stage++ }
            13 -> npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Arr!").also { stage++ }
            14 -> player(core.game.dialogue.FacialExpression.JOLLY, "Arr!").also { stage++ }
            15 -> npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Arr!").also { stage++ }
            16 -> player(core.game.dialogue.FacialExpression.JOLLY, "Arr!").also { stage++ }
            17 -> npc(core.game.dialogue.FacialExpression.OLD_LAUGH1, "Bored now...").also { stage = 99 }

            20 -> player(core.game.dialogue.FacialExpression.ASKING, "What do you mean?").also { stage++ }
            21 -> npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "I'm not hangin' around te be killed!").also { stage++ }
            22 -> npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "The Horrors, the Horrors!").also { stage = 99 }

            30 -> player(core.game.dialogue.FacialExpression.ASKING, "What?").also { stage++ }
            31 -> npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "You know...the 'special' bananas?").also { stage++ }
            32 -> player(core.game.dialogue.FacialExpression.ASKING, "No... why do you ask?").also { stage++ }
            33 -> npc(core.game.dialogue.FacialExpression.OLD_SAD, "No reason. Have a nice day.").also { stage = 99 }

            40 -> player(core.game.dialogue.FacialExpression.ASKING, "What?").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MonkeyChildThingZooDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MONKEY_4363)
    }
}
