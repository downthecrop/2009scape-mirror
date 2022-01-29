package rs09.game.interaction.item.withnpc

import api.openDialogue
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.region.examcentre.ArchaeologistcalExpertUsedOnDialogueFile
import rs09.game.content.dialogue.region.examcentre.ZooknockDialogueFile
import rs09.game.interaction.InteractionListener

open class ZooknockListener() : InteractionListener() {
    val goldBar = Items.GOLD_BAR_2357
    val monkeyAmuletMould = Items.MAMULET_MOULD_4020
    val monkeyDentures = Items.MONKEY_DENTURES_4006


    val items = intArrayOf(goldBar, monkeyDentures, monkeyAmuletMould)

    val zooknock = NPCs.ZOOKNOCK_1425

    val lol = arrayOf(Items)

    override fun defineListeners() {
        onUseWith(NPC, items, zooknock) {
                player, used, with -> openDialogue(player, ZooknockDialogueFile(used.id))
            return@onUseWith false
        }
    }
}