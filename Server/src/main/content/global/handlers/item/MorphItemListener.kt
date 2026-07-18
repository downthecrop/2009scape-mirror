package content.global.handlers.item

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * Handles morphing with the Ring of Stone and Easter Ring.
 */

class MorphItemListener : InteractionListener, InterfaceListener {

    // the morphing items
    private val morphItems = intArrayOf(Items.RING_OF_STONE_6583, Items.EASTER_RING_7927)

    // the NPCs to morph into
    private val easterEggs = intArrayOf(
        NPCs.EGG_3689,
        NPCs.EGG_3690,
        NPCs.EGG_3691,
        NPCs.EGG_3692,
        NPCs.EGG_3693,
        NPCs.EGG_3694
    )
    private val stone = intArrayOf(NPCs.ROCKS_2626)

    // the "unmorph" interface
    private val morphIface = 375

    // equipping the morph items
    override fun defineListeners() {
        onEquip(morphItems) { player, item ->
            morph(player, item.id)
            return@onEquip false
        }
    }

    // the interface that appears when you morph
    override fun defineInterfaceListeners() {

        // hitting the 'unmorph' button
        on(morphIface){ player, _, _, buttonID, _, _ ->
            when(buttonID) {
                3 -> closeAllInterfaces(player)
            }
            return@on true
        }

        // after the interface closes
        onClose(morphIface) { player, _ ->
            unmorph(player)
            return@onClose true
        }
    }

    // morphs the player
    private fun morph(player: Player, item: Int) {
        val morphId = when (item) {
            Items.RING_OF_STONE_6583 -> stone.random()
            else -> easterEggs.random()
        }
        playAudio(player, Sounds.EASTER06_HUMAN_INTO_EGG_1520)
        stopWalk(player)
        closeAllInterfaces(player)
        player.appearance.transformNPC(morphId)
        openInterface(player, morphIface)
    }

    // unmorphs the player
    private fun unmorph(player: Player) {
        player.appearance.transformNPC(-1)
    }
}