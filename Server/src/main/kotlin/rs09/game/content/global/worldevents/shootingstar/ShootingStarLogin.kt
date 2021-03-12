package rs09.game.content.global.worldevents.shootingstar

import core.game.node.entity.player.Player
import core.plugin.Plugin
import core.plugin.PluginManifest
import core.plugin.PluginType
import rs09.game.content.global.worldevents.WorldEvents

/**
 * A plugin that handles the message a player receives on login pertaining to shooting stars.
 */
@PluginManifest(type = PluginType.LOGIN)
class ShootingStarLogin : Plugin<Player?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Player?): Plugin<Player?>? {
        if (arg is Player) {
            val star = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
            if (star.isSpawned) {
                arg.sendMessage("<img=12><col=CC6600>News: A shooting star (Level " + (star.level.ordinal + 1).toString() + ") has just crashed near the " + star.location + "!")
            }
            return this
        }
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any {
        return Unit
    }
}