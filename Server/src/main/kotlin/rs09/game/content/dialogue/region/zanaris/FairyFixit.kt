package rs09.game.content.dialogue.region.zanaris

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class FairyFixit(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        //tempvar
        //post quest dialogue is missing for rs3 and osrs is too modern, so i'm just gonna use osrs as a placeholder
        var completedFairyQueen = false
        if (completedFairyQueen) {
            npc(FacialExpression.OLD_CALM_TALK1, "Pssst! Human! I've got something for you.").also { stage = 20 }
        } else {
            npc(FacialExpression.OLD_DISTRESSED, "What is it, human? Busy busy busy!").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Why are you carrying that toolbox?", "I'm okay, thanks.").also { stage++ }
            1 -> when (buttonId){
                1 -> player(FacialExpression.ASKING,"Why are you carrying that toolbox?").also { stage = 10 }
                2 -> player(FacialExpression.FRIENDLY, "I'm okay, thanks.").also { stage = 99 }
            }


            10 -> npc(FacialExpression.OLD_DEFAULT, "It's the fizgog! It's picking up cable again!").also { stage++ }
            11 -> playerl(FacialExpression.ASKING, "Uh, right. So is it safe to use the fairy rings then?").also { stage++ }
            12 -> npcl(FacialExpression.OLD_CALM_TALK1, "Sure, as long as you have been given permission to use them. You should just be aware that using the fairy rings sometimes has strange results - the locations that you have been to may").also { stage++ }
            13 -> npcl(FacialExpression.OLD_CALM_TALK2, "affect the locations you are trying to reach. I could fix it by replacing the fizgog and the whosprangit; I've put in a request for some new parts, but they're").also { stage++ }
            14 -> npc(FacialExpression.OLD_CALM_TALK1, "pretty hard to get hold of it seems.").also { stage = 99 }

            20 -> options("What have you got for me?", "Why are you carrying that toolbox?", "Not interested, thanks.").also { stage++ }
            21 -> when (buttonId) {
                1 -> player(FacialExpression.ASKING, "What have you got for me?").also { stage = 30 }
                2 -> player(FacialExpression.ASKING,"Why are you carrying that toolbox?").also { stage = 10 }
                3 -> player(FacialExpression.NEUTRAL, "Not interested, thanks.").also { stage = 99 }
            }

            30 -> npcl(FacialExpression.OLD_CALM_TALK1, "They said you'd helped cure our Queen. I haven't got a lot of rewards to offer, but my enchantment scrolls might help if you're working with fairy rings in your home.").also { stage++ }
            31 -> {
                end()
                npc.openShop(player)
            }


            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FairyFixit(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_FIXIT_4455)
    }
}
