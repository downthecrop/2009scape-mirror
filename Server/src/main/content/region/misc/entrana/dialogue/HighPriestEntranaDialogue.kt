package content.region.misc.entrana.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class HighPriestEntranaDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            // Ice gloves were NOT reclaimable till 2017.
            START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Many greetings. Welcome to our fair island.").also {
                if (inInventory(player, Items.SILVER_POT_4658) ||
                        inInventory(player, Items.SILVER_POT_4660) ||
                        inInventory(player, Items.SILVER_POT_4662) ||
                        inInventory(player, Items.SILVER_POT_4664) ||
                        inInventory(player, Items.SILVER_POT_4666)) {
                    stage = 6
                } else {
                    stage = 1
                }
            }
            1 -> npcl(FacialExpression.FRIENDLY, "You are standing on the holy island of Entrana. It was here that Saradomin first stepped upon Gielinor.").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "In homage to Saradomin's first arrival, we have built a great church, and devoted the island to those who wish peace for the world.").also { stage++ }
            3 -> npcl(FacialExpression.FRIENDLY, "The inhabitants of this island are mostly monks who spend their time meditating on Saradomin's ways.").also { stage++ }
            4 -> npcl(FacialExpression.FRIENDLY, "Of course, there are now more pilgrims to this holy site, since Saradomin defeated Zamorak in the battle of Lumbridge.").also { stage++ }
            5 -> npcl(FacialExpression.FRIENDLY, "It is good that so many see Saradomin's true glory!").also { stage = END_DIALOGUE }

            6 -> playerl(FacialExpression.FRIENDLY, "Hi, I was wondering, can you quickly bless this for me?").also { stage++ }
            7 -> npc(FacialExpression.FRIENDLY, "A somewhat strange request, but I see no harm in it.", "There you go.", "May Saradomin walk with you.").also {
                if (inInventory(player, Items.SILVER_POT_4658)) {
                    if (removeItem(player, Items.SILVER_POT_4658)) {
                        addItemOrDrop(player, Items.BLESSED_POT_4659)
                    }
                } else if (inInventory(player, Items.SILVER_POT_4660)) {
                    if (removeItem(player, Items.SILVER_POT_4660)) {
                        addItemOrDrop(player, Items.BLESSED_POT_4661)
                    }
                } else if (inInventory(player, Items.SILVER_POT_4662)) {
                    if (removeItem(player, Items.SILVER_POT_4662)) {
                        addItemOrDrop(player, Items.BLESSED_POT_4663)
                    }
                } else if (inInventory(player, Items.SILVER_POT_4664)) {
                    if (removeItem(player, Items.SILVER_POT_4664)) {
                        addItemOrDrop(player, Items.BLESSED_POT_4665)
                    }
                } else if (inInventory(player, Items.SILVER_POT_4666)) {
                    if (removeItem(player, Items.SILVER_POT_4666)) {
                        addItemOrDrop(player, Items.BLESSED_POT_4667)
                    }
                }
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HighPriestEntranaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HIGH_PRIEST_216)
    }
}