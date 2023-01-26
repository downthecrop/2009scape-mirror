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
class AliTheHagDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Good day, old hag.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.LAUGH,"Old hag indeed! I have a name you know!").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.THINKING,"Let me guess it wouldn't be Ali would it?").also { stage++ }
            2 -> npc(core.game.dialogue.FacialExpression.ANNOYED,"Well how else would you abbreviate Alice then?", "And no, you can't call me Al!").also { stage++ }
            3 -> npc(
                core.game.dialogue.FacialExpression.ASKING,"Now what do you want from the Old hag of Pollnivneach?",
                "To hex someone? Power, beauty, eternal youth", "or something else drab like that?").also { stage++ }
            4 -> player(core.game.dialogue.FacialExpression.HALF_GUILTY,"Actually none of those, I'm new in town and", "I just wanted to get to know the locals.").also { stage++ }
            5 -> npc(
                core.game.dialogue.FacialExpression.ANNOYED,"I'm busy brewing potions, so if you ",
                "disturb me again without reason,","I will turn you into a frog!").also { stage++ }
            6 -> player(core.game.dialogue.FacialExpression.SCARED,"Oh I'm sorry I won't let it happen again.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return AliTheHagDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1871)
    }
}