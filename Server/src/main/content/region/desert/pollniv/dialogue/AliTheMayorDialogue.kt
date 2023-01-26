package content.region.desert.pollniv.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * @author qmqz
 */

@Initializable
class AliTheMayorDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            core.game.dialogue.FacialExpression.FRIENDLY,"Welcome adventurer to the town of Pollnivneach,",
            "the gateway to Menaphos and Al-Kharid. My name is Ali",
            "and I'm the mayor of this town.",
            "I hope you enjoy your stay here.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(
                core.game.dialogue.FacialExpression.FRIENDLY, "Thank you.",
                "That is the warmest welcome I've had anywhere",
                "for a while at least. People generally treat",
                "travelling adventurers with suspicion.").also { stage++ }
            1 -> npc(
                core.game.dialogue.FacialExpression.FRIENDLY,"All are welcome here, such is the way",
                "of things in border regions.",
                "Now more than ever I suppose.").also { stage++ }
            2 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"What do you mean by that?").also { stage++ }
            3 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"There's trouble in town, and a lot of ", "villagers have left as a result.").also { stage++ }
            4 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"I'm looking for someone called Ali.").also { stage++ }
            5 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"I doubt that's easy in Pollnivneach.").also { stage++ }
            6 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"Well can you help me?").also { stage++ }
            7 -> npc(
                core.game.dialogue.FacialExpression.FRIENDLY,"I'm more than a little busy at the moment, ",
                "I'm sure there are plenty of people ", "in town who could help you.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return AliTheMayorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1870)
    }
}