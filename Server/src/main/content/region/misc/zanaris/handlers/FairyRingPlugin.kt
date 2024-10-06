package content.region.misc.zanaris.handlers

import content.global.handlers.iface.FairyRingInterface
import core.api.anyInEquipment
import core.api.hasRequirement
import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import org.rs09.consts.Items

/**
 * Handles interactions with fairy rings
 * @author Ceikry
 */

private val RINGS = intArrayOf(12095, 14058, 14061, 14064, 14067, 14070, 14073, 14076, 14079, 14082, 14085, 14088, 14091, 14094, 14097, 14100, 14103, 14106, 14109, 14112, 14115, 14118, 14121, 14124, 14127, 14130, 14133, 14136, 14139, 14142, 14145, 14148, 14151, 14154, 14157, 14160, 16181, 16184, 23047, 27325, 37727)
private const val MAIN_RING = 12128
private const val ENTRY_RING = 12094
private const val MARKETPLACE_RING = 12003

class FairyRingPlugin : InteractionListener {

    override fun defineListeners() {

        on(RINGS, IntType.SCENERY, "use"){ player, _ ->
            if (!fairyMagic(player)) return@on true
            val mainRingLocation = Location.create(2412, 4434, 0)
            player.teleporter.send(mainRingLocation, TeleportType.FAIRY_RING)
            return@on true
        }
        on(MAIN_RING, IntType.SCENERY, "use"){ player, _ ->
            if (!fairyMagic(player)) return@on true
            openFairyRing(player)
            return@on true
        }
        on(ENTRY_RING, IntType.SCENERY, "use") { player, _ ->
            val lumbridgeSwampShed = Location.create(3203, 3168, 0)
            player.teleporter.send(lumbridgeSwampShed, TeleportType.FAIRY_RING)
            return@on true
        }
        on(MARKETPLACE_RING, IntType.SCENERY, "use"){ player, _ ->
            val alKharidBank = Location.create(3260, 3156, 0)
            player.teleporter.send(alKharidBank, TeleportType.FAIRY_RING)
            return@on true
        }

    }

    private fun fairyMagic(player: Player) : Boolean {
        if (!hasRequirement(player, "Fairytale I - Growing Pains")) { // should be converted to a FTP2 stage requirement once FTP2 is implemented
            player.sendMessage("The fairy ring is inert.")
            return false
        }
        if (!anyInEquipment(player, Items.DRAMEN_STAFF_772, Items.LUNAR_STAFF_9084)) {
            player.sendMessage("The fairy ring only works for those who wield fairy magic.")
            return false
        }
        return true
    }

    private fun openFairyRing(player: Player) {
        openInterface(player, FairyRingInterface.RINGS_IFACE)
  }
}
