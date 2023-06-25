package rs09.game.content.activity.castlewars.areas

import content.global.skill.summoning.familiar.BurdenBeast
import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import rs09.game.content.activity.castlewars.CastleWars

abstract class CastleWarsArea : MapArea, LogoutListener, InteractionListener {

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        exitArea(entity as? Player ?: return)
    }

    override fun logout(player: Player) {
        if (!defineAreaBorders().any { it.insideBorder(player.location) }) {
            return // We're not in this area
        }

        // Move the player to the lobby bank area
        // Set location directly here, because we want it to take effect immediately
        player.location = CastleWars.lobbyBankArea.randomWalkableLoc
        exitArea(player)
    }

    open fun exitArea(player: Player) {
        // Remove any transformation
        player.appearance.transformNPC(-1)

        // Give the player their tabs back
        player.interfaceManager.restoreTabs()

        // Let the player run
        player.walkingQueue.isRunDisabled = false

        // If we're not entering another Castle Wars area, remove the Castle Wars items
        if ((CastleWarsGameArea.areaBorders + CastleWarsWaitingArea.areaBorders).none { it.insideBorder(player.location) }) {
            exitCastleWars(player)
        }
    }

    private fun exitCastleWars(player: Player) {
        // Close the overlay interface
        player.interfaceManager.closeOverlay()

        // Remove teleblock
        removeTimer(player, "teleblock")

        // Remove any Castle Wars items
        // Todo Remove any tinderboxes or other castle wars items - See Jan 2018 update: https://oldschool.runescape.wiki/w/Castle_Wars
        val cwarsItems = intArrayOf(CastleWars.saradominTeamHoodedCloak,
            CastleWars.zamorakTeamHoodedCloak,
            CastleWars.saradominFlag,
            CastleWars.zamorakFlag)

        player.equipment.removeAll(cwarsItems)
        player.inventory.removeAll(cwarsItems)
        (player.familiarManager.familiar as? BurdenBeast)?.container?.removeAll(cwarsItems)
    }

    override fun defineListeners() {
        onUnequip(intArrayOf(CastleWars.saradominTeamHoodedCloak, CastleWars.zamorakTeamHoodedCloak)) { player, _ ->
            defineAreaBorders().forEach { border ->
                if (border.insideBorder(player)) {
                    sendMessage(player, "You can't remove your team's colours")
                    // TODO: Equipping a cape or helmet causes issues
                    return@onUnequip false
                }
            }
            return@onUnequip true
        }
    }

}
