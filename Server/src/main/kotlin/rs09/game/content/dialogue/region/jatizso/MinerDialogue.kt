package rs09.game.content.dialogue.region.jatizso

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

@Initializable
class MinerDialogue(player: Player? = null) : DialoguePlugin(player) {
    val stages = intArrayOf(0, 100, 200, 300, 400, 500)
    override fun newInstance(player: Player?): DialoguePlugin {
        return MinerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        stage = stages.random()
        handle(0,0)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.SAD, "I've been here for two days straight. When I close my eyes, I see rocks.").also { stage++ }
            1 -> playerl(FacialExpression.HALF_ASKING, "Why would anyone stay here for so long?").also { stage++ }
            2 -> npcl(FacialExpression.NEUTRAL, "I fear the outside. I fear the big light.").also { stage++ }
            3 -> playerl(FacialExpression.HALF_GUILTY, "Oh dear. Being underground for so long may have driven you mad.").also { stage = END_DIALOGUE }

            100 -> npcl(FacialExpression.NEUTRAL, "My back is killing me!").also { stage++ }
            101 -> playerl(FacialExpression.HALF_GUILTY, "Could be worse, it could be the trolls killing you." ).also { stage++ }
            102 -> npcl(FacialExpression.NEUTRAL, "How very droll.").also { stage++ }
            103 -> playerl(FacialExpression.NEUTRAL, "No, troll.").also { stage++ }
            104 -> npcl(FacialExpression.SAD, "Bah! I resist your attempts at jollity.").also { stage = END_DIALOGUE }

            200 -> npcl(FacialExpression.SCARED, "Gah! Trolls everywhere. There's no escape!").also { stage++ }
            201 -> playerl(FacialExpression.HALF_THINKING, "You could just leave the mine.").also { stage++ }
            202 -> npcl(FacialExpression.NEUTRAL, "Oh, I'd never thought of that.").also { stage = END_DIALOGUE }

            300 -> playerl(FacialExpression.HALF_ASKING, "How's your prospecting going?").also { stage++ }
            301 -> npcl(FacialExpression.LAUGH, "Mine's been going pretty well.").also { stage++ }
            302 -> playerl(FacialExpression.HALF_THINKING, "...").also { stage++ }
            303 -> npcl(FacialExpression.LAUGH, "Mine? Mine...you get it?").also { stage++ }
            304 -> playerl(FacialExpression.HALF_THINKING, "Oh, I got it. That doesn't make it funny though.").also { stage++ }
            305 -> npcl(FacialExpression.NEUTRAL, "Suit yourself.").also { stage = END_DIALOGUE }

            400 -> npcl(FacialExpression.NEUTRAL, "High hoe, High hoe!").also { stage++ }
            401 -> playerl(FacialExpression.ASKING, "Why are you singing about farming implements at altitude?").also { stage++ }
            402 -> npcl(FacialExpression.NEUTRAL, "I don't know, I've never thought about it. Ask my Dad, he taught me the song.").also { stage = END_DIALOGUE }

            500 -> npcl(FacialExpression.NEUTRAL, "This place rocks!").also { stage++ }
            501 -> playerl(FacialExpression.HALF_THINKING,  "No it doesn't, it stays perfectly still.").also { stage++ }
            502 -> npcl(FacialExpression.HALF_THINKING, "Bah! Be quiet, pedant." ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MINER_5497, NPCs.MINER_5498)
    }
}