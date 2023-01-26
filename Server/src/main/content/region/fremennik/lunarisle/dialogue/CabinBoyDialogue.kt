package content.region.fremennik.lunarisle.dialogue

import core.api.addItemOrDrop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Must have Lunar Diplomacy completed for the emerald lens
 */

@Initializable
class CabinBoyDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (player.inventory.contains(Items.EMERALD_LENS_9066, 1) || player.bank.contains(Items.EMERALD_LENS_9066, 1)) {
            playerl(core.game.dialogue.FacialExpression.FRIENDLY, "So you've plucked up the courage to come and confront that girl!").also { stage = 0 }
        } else {
            player(core.game.dialogue.FacialExpression.FRIENDLY, "Hi.").also { stage = 20 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.CHILD_FRIENDLY, "That I did, that I did!").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.ASKING, "And?").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.CHILD_FRIENDLY, "She turned out to be really nice! She's joining us to become a pirate!").also { stage++ }
            3 -> playerl(core.game.dialogue.FacialExpression.HALF_WORRIED, "Really? And you're not sceptical about this? You know, after what she did last time?").also { stage++ }
            4 -> npc(core.game.dialogue.FacialExpression.CHILD_SUSPICIOUS, "She---has---no---spell---on---me.").also { stage++ }
            5 -> player(core.game.dialogue.FacialExpression.HALF_THINKING, "Huh? Why are you talking like that?").also { stage++ }
            6 -> npc(core.game.dialogue.FacialExpression.CHILD_THINKING, "Like what?").also { stage++ }
            7 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "Like someone else was talking for you. Are you sure she hasn't put a spell on you again? Did she wave a watch in front of your face? You know, tell you to look deep into her eyes?").also { stage++ }
            8 -> npc(core.game.dialogue.FacialExpression.CHILD_SUSPICIOUS, "She---has---no---spell---on---me.").also { stage++ }
            9 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING, "I think you've been hypnotised. I wonder what happens if I click my fingers?").also {
                player.sendChat("*click*").also { stage++ }
            }
            10 -> npc(core.game.dialogue.FacialExpression.CHILD_SAD, "*Cluck* *cluck* *bwaarrk*").also { stage++ }
            11 -> npc(core.game.dialogue.FacialExpression.CHILD_SAD, "").also { stage++ }
            12 -> player(core.game.dialogue.FacialExpression.WORRIED, "Oh dear. Oh well, I'm sure you'll learn one day.").also { stage = 99 }

            20 -> npc(core.game.dialogue.FacialExpression.CHILD_FRIENDLY, "I bet you're after another lens!").also { stage++ }
            21 -> player(core.game.dialogue.FacialExpression.HALF_THINKING, "How could you possibly know that?").also { stage++ }
            22 -> npcl(core.game.dialogue.FacialExpression.CHILD_FRIENDLY, "Hey, I think I'm learning a thing or two from these Moon Clan ladies.").also { stage++ }
            23 -> options("Please", "No thanks").also { stage++ }
            24 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.HALF_ASKING, "Oi! I need another lens, boy.").also { stage = 25}
                2 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "I think I'll pass. But thanks..").also { stage = 99 }
            }
            25 -> npc(core.game.dialogue.FacialExpression.CHILD_FRIENDLY, "Huh, oh ok, I suppose I owe you one.").also { addItemOrDrop(player, Items.EMERALD_LENS_9066, 1) }.also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return CabinBoyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CABIN_BOY_4539)
    }
}
