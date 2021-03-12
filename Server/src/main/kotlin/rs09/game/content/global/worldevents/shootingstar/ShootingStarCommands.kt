package rs09.game.content.global.worldevents.shootingstar

import core.game.node.entity.player.Player
import core.game.system.command.CommandSet
import core.plugin.Plugin
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.system.command.CommandPlugin
import java.util.concurrent.TimeUnit

/**
 * A few assorted commands for shooting stars.
 */
class ShootingStarCommands : CommandPlugin(){
    override fun newInstance(arg: Any?): Plugin<Any?>{
        link(CommandSet.DEVELOPER)
        return this
    }

    override fun parse(player: Player?, name: String?, args: Array<String?>?): Boolean {
        val star = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
        when (name) {
            "tostar" -> {
                player!!.teleport(star.starObject.location.transform(1, 1, 0))
            }
            "submit" -> {
                star.fire()
            }
            "resettime" -> player!!.savedData.globalData.starSpriteDelay = 0L
            "stardust" -> {
                val dust = 8
                println("Cosmic Runes: " + 0.76 * dust)
                println("Astral runes: " + 0.26 * dust)
                println("Gold ores: " + 0.1 * dust)
                println("GP: " + 250.1 * dust)
            }
            "setsprite" -> player!!.savedData.globalData.starSpriteDelay = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
        }
        return true
    }
}