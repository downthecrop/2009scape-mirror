package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class AliTheGuard(player: Player? = null) : DialoguePlugin(player){

    override fun newInstance(player: Player?): DialoguePlugin {
        return AliTheGuard(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY,"Hello there!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.ANNOYED,"I'm working. What do you have to say that's so urgent?").also { stage++ }
            1 -> options("What can you tell me about Al Kharid?", "So, what do you do here?", "I hear you work for Ali Morrisane...", "I hear you've been threatening the other shopkeepers.").also {  stage++ }
            2 -> when(buttonId){
                1 -> playerl(FacialExpression.ASKING, "What can you tell me about Al Kharid?").also { stage = 10 }
                2 -> playerl(FacialExpression.ASKING, "So, what do you do here?").also { stage = 20 }
                3 -> playerl(FacialExpression.ASKING, "I hear you work for Ali Morrisane...").also { stage = 30 }
                4 -> playerl(FacialExpression.ASKING, "I hear you've been threatening the other shopkeepers.").also { stage = 40 }
            }
            10 -> npcl(FacialExpression.FRIENDLY,"There's a lot of space here. More open space than back home.").also { stage++ }
            11 -> playerl(FacialExpression.ASKING,"So where is back home?").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY,"Pollnivneach. It's a town to the south of the Shantay Pass.").also { stage = 1 }
            20 -> npcl(FacialExpression.ANNOYED,"I'm on guard duty. Making sure nobody tries to steal anything from the house and tents in the middle of town.").also { stage++ }
            21 -> playerl(FacialExpression.ASKING,"Why are you only guarding those buildings?").also { stage++ }
            22 -> npcl(FacialExpression.SUSPICIOUS,"That's all I've been hired to guard.").also { stage = 1 }
            30 -> npcl(FacialExpression.FRIENDLY,"Yeah, he hired me. He owns this house and these two tents, too.").also { stage++ }
            31 -> playerl(FacialExpression.HALF_ASKING,"Is the work good?").also { stage++ }
            32 -> npcl(FacialExpression.FRIENDLY,"It pays better than back home.").also { stage++ }
            33 -> playerl(FacialExpression.ASKING,"Why, what did you do back home?").also { stage++ }
            34 -> npcl(FacialExpression.SUSPICIOUS,"Never you mind.").also { stage++ }
            35 -> npcl(FacialExpression.FRIENDLY,"But Ali Morrisane pays us well, at least.").also { stage++ }
            36 -> playerl(FacialExpression.ASKING,"Maybe I should talk to him...").also { stage++ }
            37 -> npcl(FacialExpression.FRIENDLY,"Why not? He always likes to meet potential business partners.").also { stage = 1 }
            40 -> npcl(FacialExpression.ANNOYED,"So? They talk too much.").also { stage++ }
            41 -> playerl(FacialExpression.HALF_ASKING,"You're not going to deny it?").also { stage++ }
            42 -> npcl(FacialExpression.LOUDLY_LAUGHING,"Why bother? None of them can fight back, after all.").also { stage = 99 }
            99 -> {
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_GUARD_2823)
    }
}