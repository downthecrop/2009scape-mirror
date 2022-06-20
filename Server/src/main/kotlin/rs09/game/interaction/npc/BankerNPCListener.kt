package rs09.game.interaction.npc

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.NPCs
import rs09.game.ge.GrandExchangeRecords
import rs09.game.interaction.InteractionListener

/**
 * Allows the user to interact with banker NPC options.
 *
 * @author vddCore
 */
class BankerNPCListener : InteractionListener {
    companion object {
        val BANKER_NPCS = intArrayOf(
            NPCs.BANKER_44, NPCs.BANKER_45, NPCs.BANKER_494, NPCs.BANKER_495, NPCs.BANKER_496, NPCs.BANKER_497,
            NPCs.BANKER_498, NPCs.BANKER_499, NPCs.BANKER_1036, NPCs.BANKER_1360, NPCs.BANKER_2163, NPCs.BANKER_2164,
            NPCs.BANKER_2354, NPCs.BANKER_2355, NPCs.BANKER_2568, NPCs.BANKER_2569, NPCs.BANKER_2570, NPCs.BANKER_3198,
            NPCs.BANKER_3199, NPCs.BANKER_5258, NPCs.BANKER_5259, NPCs.BANKER_5260, NPCs.BANKER_5261, NPCs.BANKER_5776,
            NPCs.BANKER_5777, NPCs.BANKER_5912, NPCs.BANKER_5913, NPCs.BANKER_6200, NPCs.BANKER_6532, NPCs.BANKER_6533,
            NPCs.BANKER_6534, NPCs.BANKER_6535, NPCs.BANKER_6538, NPCs.BANKER_7445, NPCs.BANKER_7446, NPCs.BANKER_7605,

            NPCs.BANK_TUTOR_4907,

            NPCs.GHOST_BANKER_1702, NPCs.GNOME_BANKER_166, NPCs.NARDAH_BANKER_3046,
            NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050, NPCs.SIRSAL_BANKER_4519,
            NPCs.FADLI_958,
        )

        fun provideDestinationOverride(entity: Entity, node: Node): Location {
            val npc = node as NPC

            if (npc.getAttribute("facing_booth", false)) {
                return npc.location.transform(
                    npc.direction.stepX shl 1,
                    npc.direction.stepY shl 1,
                    0
                )
            }

            return when(npc.id) {
                NPCs.BANKER_6533, NPCs.BANKER_6534 -> npc.location.transform(Direction.EAST, 1)
                NPCs.BANKER_6535, NPCs.BANKER_6532 -> npc.location.transform(Direction.WEST, 1)
                NPCs.BANK_TUTOR_4907 -> npc.location.transform(Direction.SOUTH, 2)
                else -> npc.location
            }
        }
    }

    override fun defineListeners() {
        on(BANKER_NPCS, NPC, "bank") { player, node ->
            (node as NPC).faceLocation(null)
            player.bank.open(); true
        }

        on(BANKER_NPCS, NPC, "collect") { player, node ->
            (node as NPC).faceLocation(null)
            GrandExchangeRecords.getInstance(player).openCollectionBox(); true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(NPC, BANKER_NPCS, "bank", "collect", "talk-to", handler = ::provideDestinationOverride)
    }
}