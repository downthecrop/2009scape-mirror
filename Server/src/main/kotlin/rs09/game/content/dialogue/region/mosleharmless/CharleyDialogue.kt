package rs09.game.content.dialogue.region.mosleharmless

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
class CharleyDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (player.inventory.contains(Items.BOOK_O_PIRACY_7144, 1)) {
            npc(FacialExpression.FRIENDLY, "I got fish, you got gold?").also { stage = 10 }
        } else {
            player(FacialExpression.FRIENDLY, "Hello!").also { stage = 0 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.FRIENDLY, "Arr? Be ye wantin' te go on account with our gang o' fillibusters?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "The powder monkey be takin' a caulk after gettin' rowdy on bumboo, so there be plenty of room for ye.").also { stage++ }
            2 -> player(FacialExpression.FRIENDLY, "Riiiiight...").also { stage++ }
            3 -> player(FacialExpression.FRIENDLY, "I'll just be over here if you need me.").also { stage = 99 }

            10 -> options("Yes.", "Yes, but I don't want your fish.", "What happened to your legs?").also { stage++ }

            11 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Yes.").also { stage = 80 }
                2 -> player(FacialExpression.FRIENDLY, "Yes, but I don't want your fish.").also { stage = 20 }
                3 -> player(FacialExpression.HALF_ASKING, "What happened to your legs?").also { stage = 30 }
            }

            20 -> npcl(FacialExpression.FRIENDLY, "Then what are ye doin' in a fish shop? Looking fer work?").also { stage++ }
            21 -> player(FacialExpression.FRIENDLY, "Possibly, do you have any quests?").also { stage++ }
            22 -> npcl(FacialExpression.HALF_ASKING, "I dunno, I haven't gone through the last catch yet. What sort of a fish is it?").also { stage++ }
            23 -> player(FacialExpression.FRIENDLY, "A quest isn't a type of fish!").also { stage++ }
            24 -> npcl(FacialExpression.FRIENDLY, "Then I don't have any, and yer wastin' my time!").also { stage = 99 }

            30 -> npcl(FacialExpression.FRIENDLY, "Ye wanna know what happened to my legs?").also { stage++ }
            31 -> npcl(FacialExpression.FRIENDLY, "Yer too much of a lilly-livered, hat wearin' landlubber to know what happened to my legs!").also { stage++ }
            32 -> playerl(FacialExpression.FRIENDLY, "No I'm not! I've seen some freaky stuff! I can take it!").also { stage++ }
            33 -> npcl(FacialExpression.FRIENDLY, "All right, lad, since yer so insistent, I'll tell ye.").also { stage++ }
            34 -> npcl(FacialExpression.FRIENDLY, "See, I was clingin' onto a barrel, me ship havin' just had an encounter with this albatross.").also { stage++ }
            35 -> npcl(FacialExpression.FRIENDLY, "The sea was thrashin' and wild, but not so wild that I didn't see the fins of some sharks closin' in on me.").also { stage++ }
            36 -> npcl(FacialExpression.FRIENDLY, "I managed to yank a sliver of wood from the barrel just as one of them grabbed me from below, but I slipped down the things throat by about two feet before I managed te kill it.").also { stage++ }
            37 -> player(FacialExpression.FRIENDLY, "How did you survive?").also { stage++ }
            38 -> npcl(FacialExpression.FRIENDLY, "A passin' ship saw the sharks and knew there would be survivors in the water. They sent a longboat and picked me up, but not before the sharks had taken off my legs.").also { stage++ }
            39 -> npcl(FacialExpression.FRIENDLY, "And that lad is why they call me two feet Charley, because they found me jammed two feet down a shark's throat.").also { stage++ }
            40 -> player(FacialExpression.DISGUSTED, "I think I'm gonna be sick...").also { stage++ }
            41 -> npcl(FacialExpression.FRIENDLY, "I knew ye couldn't handle the truth!").also { stage = 99 }

            80 -> end().also { npc.openShop(player) }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CharleyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHARLEY_3161)
    }
}
