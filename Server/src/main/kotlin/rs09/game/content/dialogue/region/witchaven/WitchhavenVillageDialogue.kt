package rs09.game.content.dialogue.region.witchaven

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
class WitchhavenVillageDialogue(player: Player? = null) : DialoguePlugin(player){

    private val conversations = arrayOf (0, 7, 11, 19, 24)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY, "Hello there.").also { stage = conversations.random() }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            0 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            1 -> npc(FacialExpression.NEUTRAL, "Ye mariners all, as ye pass by,").also { stage++ }
            2 -> npc(FacialExpression.NEUTRAL, "Come in and drink if you are dry,").also { stage++ }
            3 -> npc(FacialExpression.NEUTRAL, "Come spend, me lads, your money brisk,").also { stage++ }
            4 -> npc(FacialExpression.NEUTRAL, "And pop your nose in a jug of this.").also { stage++ }
            5 -> player(FacialExpression.NEUTRAL, "You're not fooling anyone you know.").also { stage++ }
            6 -> npc(FacialExpression.NEUTRAL, "We fooled you easily enough.").also { stage = 99 }

            7 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            8 -> npc(FacialExpression.NEUTRAL, "Free. She is free...").also { stage++ }
            9 -> player(FacialExpression.NEUTRAL, "What?").also { stage++ }
            10 -> npc(FacialExpression.NEUTRAL, "The mother is free.").also { stage = 99 }

            11 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            12 -> npc(FacialExpression.NEUTRAL, "You! You did it!").also { stage++ }
            13 -> player(FacialExpression.NEUTRAL, "I didn't mean to!").also { stage++ }
            14 -> npc(FacialExpression.NEUTRAL, "You killed him!").also { stage++ }
            15 -> player(FacialExpression.NEUTRAL, "It was an accide... Killed who?").also { stage++ }
            16 -> npc(FacialExpression.NEUTRAL, "Our Prince, you killed our Prince.").also { stage++ }
            17 -> player(FacialExpression.NEUTRAL, "Oh that, yes I did.").also { stage++ }
            18 -> npc(FacialExpression.NEUTRAL, "Leave us alone.").also { stage = 99 }

            19 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            20 -> npc(FacialExpression.NEUTRAL, "Soon now... So soon...").also { stage++ }
            21 -> npc(FacialExpression.NEUTRAL, "The stars are almost right.").also { stage++ }
            22 -> player(FacialExpression.NEUTRAL, "For what?").also { stage++ }
            23 -> npc(FacialExpression.NEUTRAL, ". . .").also { stage = 99 }

            24 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            25 -> npc(FacialExpression.NEUTRAL, "Ahh, our saviour.").also { stage++ }
            26 -> player(FacialExpression.NEUTRAL, "Please don't remind me.").also { stage++ }
            27 -> npc(FacialExpression.NEUTRAL, "Do not worry, soon your regret will be gone.").also { stage++ }
            28 -> player(FacialExpression.NEUTRAL, "If you think you will get to me...").also { stage++ }
            29 -> npc(FacialExpression.NEUTRAL, "All in good time.").also { stage = 99 }


            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return WitchhavenVillageDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WITCHAVEN_VILLAGER_4883, NPCs.WITCHAVEN_VILLAGER_4884,
            NPCs.WITCHAVEN_VILLAGER_4885, NPCs.WITCHAVEN_VILLAGER_4886,
            NPCs.WITCHAVEN_VILLAGER_4887, NPCs.WITCHAVEN_VILLAGER_4888)
    }
}
