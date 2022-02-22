package rs09.game.content.dialogue.region.examcentre

import api.addItemOrDrop
import api.sendDoubleItemDialogue
import api.sendItemDialogue
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.item.withnpc.ZooknockListener

/**
 * @author qmqz
 * todo will need to check quest stages for what's been handed over and whatnot
 */

class ZooknockDialogueFile(val it: Int) : DialogueFile() {

    var i = ZooknockListener()
    var n = NPC(i.zooknock)
    var itemUsed = it
    var hasGivenGoldBar = false
    var hasGivenDentures = false
    var hasGivenMould = false

    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = n

        when (stage) {
            0 -> when (itemUsed) {
                i.goldBar -> {
                    if (!hasGivenGoldBar) {
                        player(FacialExpression.ASKING, "What do you think of this?").also { stage = 10 }
                        hasGivenGoldBar = true
                    }
                }

                i.monkeyAmuletMould -> {
                    if (!hasGivenMould) {
                        player(FacialExpression.ASKING, "What do you think of this?").also { stage = 20 }
                        hasGivenMould = true
                    }
                }

                i.monkeyDentures -> {
                    if (!hasGivenDentures) {
                        player(FacialExpression.ASKING, "What do you think of this?").also { stage = 30 }
                    }
                }
            }

            10 -> sendItemDialogue(player!!, Items.GOLD_BAR_2357, "You hand Zooknock the gold bar.").also { stage++ }
            11 -> npc(FacialExpression.OLD_CALM_TALK1, "Nicely done.").also { stage++ }
            12 -> {
                if (!hasGivenDentures && !hasGivenMould && hasGivenGoldBar) {
                    npcl(FacialExpression.OLD_CALM_TALK1, "We still need the monkey amulet mould and something to do with monkey speech.").also { stage = 99 }
                }
            }

            20 -> sendItemDialogue(player!!, Items.MAMULET_MOULD_4020, "You hand Zooknock the monkey amulet mould.").also { stage++ }
            21 -> npc(FacialExpression.OLD_CALM_TALK1, "Good work.").also { stage++ }
            22 -> {
                if (!hasGivenDentures && hasGivenMould && hasGivenGoldBar) {
                    npcl(FacialExpression.OLD_CALM_TALK1, "We still need something to do with monkey speech.").also { stage = 99 }
                }
            }

            30 -> sendItemDialogue(player!!, Items.MSPEAK_AMULET_4021, "You hand Zooknock the magical monkey dentures.").also { stage++ }
            31 -> npc(FacialExpression.OLD_CALM_TALK1, "Good work.").also { stage++ }
            32 -> {
                if (hasGivenDentures && hasGivenMould && hasGivenGoldBar) {
                    npcl(FacialExpression.OLD_CALM_TALK1, "Now listen closely, human. I will cast a spell to enchant this gold bar with the power contained in these monkey dentures.").also { stage = 40 }
                }
            }

            40 -> npcl(FacialExpression.OLD_CALM_TALK1, "You must then smith the gold using the monkey amulet mould. However, unless you do this in a place of religious significance to the monkeys, the spirits").also { stage++ }
            41 -> npcl(FacialExpression.OLD_CALM_TALK2, "contained within will likely depart.").also { stage++ }
            42 -> playerl(FacialExpression.FRIENDLY, "Where do I find a place of religious significance to monkeys?").also { stage++ }
            43 -> npcl(FacialExpression.OLD_CALM_TALK1, "Somewhere in the village. It out to be obvious. Now give me a moment.").also { stage++ }
            44 -> {
                //zooknock does animation, bla bla bla, waits a few seconds, opens up new next dialog
            }
            45 -> {
                addItemOrDrop(player!!, Items.ENCHANTED_BAR_4007, 1)
                addItemOrDrop(player!!, Items.MAMULET_MOULD_4020, 1)
                sendDoubleItemDialogue(player!!, Items.ENCHANTED_BAR_4007, Items.MAMULET_MOULD_4020, "Zooknock hands you back the gold bar and the monkey amulet mould.").also { stage++ }
            }


            99 -> end()
        }
    }
}
