package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

import core.api.*

/**
 * Represents the ali morrisane dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
class AliMorrisaneDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return AliMorrisaneDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.FRIENDLY, "Good day and welcome back to Al Kharid.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FacialExpression.FRIENDLY, "Hello to you too.").also { stage = 1 }
            1 -> npcl(FacialExpression.ASKING, "My name is Ali Morrisane - the greatest salesman in the world.").also { stage = 2 }
            2 -> options("If you are, then why are you still selling goods from a stall?", "So what are you selling then?").also { stage = 3 }
            3 -> when (buttonId) {
                1 -> playerl(FacialExpression.ASKING, "If you are, then why are you still selling goods from a stall?").also { stage = 10 }
                2 -> {
                    end()
                    if (!hasRequirement(player, "The Feud"))
                        return true
                    npc.openShop(player)
                }
            }
            10 -> npcl(FacialExpression.FRIENDLY, "Well one can only do and sell so much. If I had more staff I'd be able to sell more").also { stage = 11 }
            11 -> npcl(FacialExpression.FRIENDLY,"rather than wasting my time on menial things I could get on with selling sand to the Bedabin and useless tourist trinkets to everyone.").also { stage = 12 }
            12 -> options("I'm far too busy - adventuring is a full time job you know.", "I'd like to help you but....").also { stage = 13 }
            13 -> when (buttonId) {
                1 -> playerl(FacialExpression.FRIENDLY, "I'm far too busy - adventuring is a full time job you know.").also { stage = 16 }
                2 -> playerl(FacialExpression.FRIENDLY, "I'd like to help you but.....").also { stage = 14 }
            }
            14 -> npcl(FacialExpression.FRIENDLY, "Yes I know, I know - the life of a shop keeper isn't slaying dragon and wooing damsels but it has its charms").also { stage = 15 }
            15 -> end()
            16 -> npcl(FacialExpression.FRIENDLY,"No problem my friend, perhaps another time.").also { stage = 17 }
            17 -> npcl(FacialExpression.FRIENDLY, "Anyway, have a look at my wares.").also { stage = 18 }
            18 -> options("No, I'm really too busy.", "Okay.").also { stage = 19 }
            19 -> when (buttonId) {
                1 -> {
                    end()
                }
                2 -> {
                    end()
                    if (!hasRequirement(player, "The Feud"))
                        return true
                    npc.openShop(player)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_MORRISANE_1862)
    }
}

