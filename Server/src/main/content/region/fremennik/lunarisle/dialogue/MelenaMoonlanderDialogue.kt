package content.region.fremennik.lunarisle.dialogue

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
class MelenaMoonlanderDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Hi. Welcome to the general store. How might I help you?").also { stage++ }

            1 -> options("What can you sell me?", "I have a question...", "I'm good thanks, bye.").also { stage++ }

            2 -> when (buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> player(core.game.dialogue.FacialExpression.HALF_ASKING, "I have a question...").also { stage = 11 }
                3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "I'm good thanks, bye.").also { stage = 99 }
            }

            11 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "About magic of course.").also { stage++ }
            12 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "Sorry?").also { stage++ }
            13 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I said about magic of course. You know, in response to your question.").also { stage++ }
            14 -> player(core.game.dialogue.FacialExpression.HALF_THINKING, "But I didn't ask anything yet.").also { stage++ }
            15 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Yes, but you were thinking of asking me how I was floating.").also { stage++ }
            16 -> player(core.game.dialogue.FacialExpression.AMAZED, "That's true! How could you possibly know that?").also { stage++ }
            17 -> npc(core.game.dialogue.FacialExpression.HALF_THINKING, "Don't you realise we can read your mind.").also { stage++ }
            18 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "Oh, of course. How do you manage to do that?").also { stage++ }
            19 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "It's quite simple, everyone has a resonance that is responded to by the moon. This resonance changes depending on what we are thinking. You can tune yourself in to listen to this").also { stage++ }
            20 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "resonance with practice - it's a life long quest for the members of the Moon Clan, but its especially easy to read with outsiders like yourself,").also { stage++ }
            21 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "as you are far louder and unguarded.").also { stage++ }
            22 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "I see. I best be careful what I think of then.").also { stage++ }
            23 -> player(core.game.dialogue.FacialExpression.WORRIED, "...").also { stage++ }
            24 -> player(core.game.dialogue.FacialExpression.THINKING, "...").also { stage++ }
            25 -> npc(core.game.dialogue.FacialExpression.DISGUSTED_HEAD_SHAKE, "That's disgusting!").also { stage++ }
            26 -> player(core.game.dialogue.FacialExpression.SAD, "Sorry.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MelenaMoonlanderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MELANA_MOONLANDER_4516)
    }
}
