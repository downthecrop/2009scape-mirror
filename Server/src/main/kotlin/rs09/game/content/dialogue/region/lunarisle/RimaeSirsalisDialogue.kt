package rs09.game.content.dialogue.region.lunarisle

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
class RimaeSirsalisDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("What can you sell me?", "Do you have any use for suqah teeth or hides?", "It's a very interesting island you have here.", "I'm good thanks, bye.").also { stage++ }

            1 -> when (buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> player(FacialExpression.HALF_ASKING, "Do you have any use for suqah teeth or hides?").also { stage = 5 }
                3 -> npcl(FacialExpression.FRIENDLY, "Why thank you. It's been our haven for a great many generations.").also { stage = 10 }
                4 -> player(FacialExpression.FRIENDLY, "I'm good thanks, bye.").also { stage = 99 }
            }

            5 -> npcl(FacialExpression.FRIENDLY, "Most certainly! The hides are great for making clothes and the teeth are particularly useful for broaches, necklaces and the like. But I won't accept them from anyone.").also { stage = 99 }

            10 -> playerl(FacialExpression.FRIENDLY, "Everything seems to have a magical feel to it.").also { stage++ }
            11 -> npcl(FacialExpression.FRIENDLY, "Of course. We integrate magic into all areas of our lives. It is a part of everyone, so why deny it? It's best to make the most of this innate gift we have all been given.").also { stage++ }
            12 -> player(FacialExpression.FRIENDLY, "What sort of things do you use your magic for?").also { stage++ }
            13 -> npcl(FacialExpression.FRIENDLY, "Take a look around. We use it in our day to day lives, from making a cup of tea to travelling around the island.").also { stage++ }
            14 -> npcl(FacialExpression.FRIENDLY, "You see our ancestors were the ones that found the first rune essence and put it to use! The various factions were eventually created,").also { stage++ }
            15 -> npcl(FacialExpression.FRIENDLY, "with people having different ideas on how the essence should be used (or not in some cases!)").also { stage++ }
            16 -> player(FacialExpression.FRIENDLY, "What has kept you so secluded on this island?").also { stage++ }
            17 -> npcl(FacialExpression.FRIENDLY, "It may be a bit beyond you, but although magic comes from within, we are all strongly linked to the moon and the").also { stage++ }
            18 -> npc(FacialExpression.FRIENDLY, "effects it has on us cannot be denied!").also { stage++ }
            19 -> player(FacialExpression.FRIENDLY, "It can't?").also { stage++ }
            20 -> npcl(FacialExpression.FRIENDLY, "Of course not. This very island has a great link to our moon, which helps us understand ourselves - especially our dreams,").also { stage++ }
            21 -> npc(FacialExpression.FRIENDLY, "which is the path to understanding magic.").also { stage++ }
            22 -> playerl(FacialExpression.FRIENDLY, "I think I will just have to take your word on that.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return RimaeSirsalisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RIMAE_SIRSALIS_4518)
    }
}
