package rs09.game.content.dialogue.region.isafdar

import api.addItemOrDrop
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class IlfeenDialogue(player: Player? = null) : DialoguePlugin(player){

    //temp var
    var queststage = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        //todo choose dialogue tree based off of quest stage
        when(queststage) {
            0 -> npcl(FacialExpression.FRIENDLY,"Good day, what are you doing all the way out here?").also { stage = 0 }
            1 -> playerl(FacialExpression.ASKING, "There is something I meant to ask you about. Do you remember Oaknock? He was a gnome inventor.").also { stage = 10 }
            2 -> player(FacialExpression.FRIENDLY, "Hello again. Are you still offering to chant seeds?").also { stage = 20 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.FRIENDLY, "I'm just wandering, it's a lovely day for a walk in the woods.").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY, "It is that.").also { stage++ }
            2 -> player(FacialExpression.FRIENDLY, "Well I shouldn't keep you, see you later.").also { stage++ }
            3 -> npc(FacialExpression.FRIENDLY, "Bye.").also { stage = 99 }

            10 -> npcl(FacialExpression.HAPPY, "Of course! I remember him from when I was living in the eastern lands. He was just a budding engineer then. I rather liked the young gnome: he was rather loud, but he had a good soul.").also { stage++ }
            11 -> npcl(FacialExpression.HAPPY, "He was very interested in elven magic. Last time we spoke, not too long ago, he was very interested in illusion magic and how to dispel it. I gave him some advice and a few pieces of equipment to help him.").also { stage++ }
            12 -> playerl(FacialExpression.THINKING, "I think Oaknock died of old age a few hundred years ago now.").also { stage++ }
            13 -> npcl(FacialExpression.HALF_GUILTY, "I keep forgetting, a hundred years is a long time for you.").also { stage++ }
            14 -> player(FacialExpression.ASKING, "Did you ever give him a seed?").also { stage++ }
            15 -> npcl(FacialExpression.FRIENDLY, "Of course. It grew into a saw. I hope he found it useful.").also { stage++ }
            16 -> player(FacialExpression.FRIENDLY, "I think he did! Thanks.").also { stage++ }
            17 -> sendDialogue("Ilfeen smiles serenely.").also { stage = 99 }

            20 -> npcl(FacialExpression.ASKING, "I am, but you’ll need your own seed. I can also chant your shield or bow back to full charges if you have it with you. Would you like me to enchant anything?").also { stage++ }
            21 -> npcl(FacialExpression.ASKING, "Or I could give you a book about crystal singing?").also { stage++ }
            22 -> options ("Enchant something.", "Crystal singing book.", "Nothing, thanks.").also { stage++ }
            23 -> when (buttonId) {
                //1 -> todo opens interface for enchanting
                2 -> player(FacialExpression.ASKING, "Could I have the book, please?").also { stage = 40 }
                3 -> npc(FacialExpression.FRIENDLY, "Well, good luck.").also { stage = 43 }
            }

            40 -> {
                addItemOrDrop(player, Items.CRYSTAL_OF_SEREN_4313, 1)
                sendDialogue("Ilfeen hands you a book.").also { stage++ }
            }
            41 -> player(FacialExpression.FRIENDLY, "Thanks, I'll read all about it.").also { stage++ }
            42 -> npc(FacialExpression.FRIENDLY, "Well, good luck.").also { stage++ }
            43 -> player(FacialExpression.FRIENDLY, "Goodbye, Ilfeen.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return IlfeenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ILFEEN_1777)
    }
}
