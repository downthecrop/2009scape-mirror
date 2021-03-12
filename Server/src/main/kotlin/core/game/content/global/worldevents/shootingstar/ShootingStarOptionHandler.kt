package core.game.content.global.worldevents.shootingstar

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Plugin
import core.game.content.global.worldevents.WorldEvents
import java.util.concurrent.TimeUnit

/**
 * Option handlers for the various shooting star objects.
 */
class ShootingStarOptionHandler : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any>? {
        for (star in ShootingStarType.values()) {
            ObjectDefinition.forId(star.objectId).handlers["option:mine"] = this
            ObjectDefinition.forId(star.objectId).handlers["option:prospect"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val star: ShootingStar? = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
        star ?: return false
        when (option) {
            "mine" -> {
                star.mine(player)
            }
            "prospect" -> {
                star.prospect(player)
            }
            "observe" -> if (star.isSpawned) {
                player.dialogueInterpreter.sendDialogue("A shooting star (Level " + (star.level.ordinal + 1).toString() + ")", "is currently crashed near the " + star.location + ".")
            } else {
                val tickDiff = (if (GameWorld.settings?.isDevMode == true) 200 else 7200) - star.ticks
                val seconds = Math.ceil((tickDiff.toFloat() / 1000.toFloat()) * 600.toDouble()).toInt()
                val hours = TimeUnit.SECONDS.toHours(seconds.toLong()).toInt()
                val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()).toInt()
                var time = ""
                time += if (hours > 0) {
                    "$hours hour"
                } else {
                    if (minutes > 0) {
                        "$minutes minute(s)"
                    } else {
                        "$seconds second(s)"
                    }
                }
                player.dialogueInterpreter.sendDialogue("The next star will crash in approximately:", time)
            }
        }
        return true
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