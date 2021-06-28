package rs09.game.content.global.worldevents.shootingstar

import core.game.node.entity.player.Player
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.interaction.InteractionListener

/**
 * Option handlers for the various shooting star objects.
 */
class ShootingStarOptionHandler : InteractionListener() {

    val SHOOTING_STARS = ShootingStarType.values().map(ShootingStarType::objectId).toIntArray()

    override fun defineListeners() {
        on(SHOOTING_STARS,SCENERY,"mine"){ player, _ ->
            val star = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
            star.mine(player)
            return@on true
        }

        on(SHOOTING_STARS,SCENERY,"prospect"){ player, _ ->
            val star = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
            star.prospect(player)
            return@on true
        }
    }

    companion object {
        const val STAR_DUST = 13727
        /**
         * Gets the star dust amount for a player.
         * @param player The player.
         * @return The stardust amount.
         */
        fun getStarDust(player: Player): Int {
            return player.inventory.getAmount(STAR_DUST) + player.bank.getAmount(STAR_DUST)
        }
    }
}