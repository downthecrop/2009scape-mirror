package rs09.game.content.dialogue.region.jatizso

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners
import rs09.tools.END_DIALOGUE

@Initializable
class TowerGuardDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TowerGuardDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.HALF_ASKING, "What are you doing here?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.NEUTRAL, "I'M ON SHOUTING DUTY!").also { stage++ }
            1 -> playerl(FacialExpression.HALF_THINKING, "No need to shout.").also { stage++ }
            2 -> npc(FacialExpression.NEUTRAL, "I'M SORRY I'VE BEEN SHOUTING","INSULTS SO LONG I CAN'T HELP IT!").also { stage++ }
            3 -> playerl(FacialExpression.HALF_THINKING, "Who are you insulting?").also { stage++ }
            4 -> npc(FacialExpression.NEUTRAL, "THE TOWER IN ${if(npc.id == NPCs.GUARD_5489) "NEITIZNOT" else "JATIZSO"}.","THEY SHOUT INSULTS BACK.").also { stage++ }
            5 -> playerl(FacialExpression.ASKING, "Err, why?" ).also { stage++ }
            6 -> npc(FacialExpression.NEUTRAL, "THE ${if(npc.id == NPCs.GUARD_5489) "KING" else "BURGHER"} TELLS US TO.").also { stage++ }
            7 -> playerl(FacialExpression.HALF_THINKING, "Your ${if(npc.id == NPCs.GUARD_5489) "King" else "Burgher"} is a strange person.").also { stage++ }
            8 -> options("Can I watch? I'm curious.", "Oh well, I'd better get going.").also { stage++ }
            9 -> when(buttonId){
                1 -> playerl(FacialExpression.ASKING, "Can I watch? I'm curious.").also { stage++ }
                2 -> playerl(FacialExpression.HALF_THINKING, "Oh well, I'd better get going.").also { stage = END_DIALOGUE }
            }
            10 -> npcl(FacialExpression.NEUTRAL,  "IF YOU LIKE!").also {
                stage = END_DIALOGUE
                InteractionListeners.run(NPCs.GUARD_5489, InteractionListener.NPC, "watch-shouting", player, npc)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_5489, NPCs.GUARD_5490)
    }

}