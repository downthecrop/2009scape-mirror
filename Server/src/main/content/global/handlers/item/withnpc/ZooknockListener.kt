package content.global.handlers.item.withnpc

import content.region.misc.apeatoll.dialogue.dungeon.ZooknockDialogueFile
import core.api.openDialogue
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

open class ZooknockListener() : InteractionListener {
    val goldBar = Items.GOLD_BAR_2357
    val monkeyAmuletMould = Items.MAMULET_MOULD_4020
    val monkeyDentures = Items.MONKEY_DENTURES_4006


    val items = intArrayOf(goldBar, monkeyDentures, monkeyAmuletMould)

    val zooknock = NPCs.ZOOKNOCK_1425

    val lol = arrayOf(Items)

    override fun defineListeners() {
        onUseWith(IntType.NPC, items, zooknock) {
                player, used, with -> openDialogue(player, ZooknockDialogueFile(used.id))
            return@onUseWith false
        }
    }
}