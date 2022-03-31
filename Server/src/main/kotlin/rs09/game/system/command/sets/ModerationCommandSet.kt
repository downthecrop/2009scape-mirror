package rs09.game.system.command.sets

import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import rs09.game.system.command.Command
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Location
import rs09.game.world.repository.Repository
import core.plugin.Initializable
import rs09.game.system.command.Privilege
import java.util.concurrent.TimeUnit

@Initializable
/**
 * Moderation commands
 * @author Ceikry
 */
class ModerationCommandSet : CommandSet(Privilege.MODERATOR){
    override fun defineCommands() {
        val MAX_JAIL_TIME = 1800 //Max jail time (in seconds)


        /**
         * Kick a player
         * =============================================================================================================
         */
        define("kick", Privilege.ADMIN){ player, args ->
            val playerToKick: Player? = Repository.getPlayerByName(args[1])
            if (playerToKick != null) {
                playerToKick.clear(true)
                notify(player, "Player ${playerToKick.username} was kicked.")
            } else {
                reject(player, "ERROR REMOVING PLAYER.")
            }
        }
        /**
         * =============================================================================================================
         */


        /**
         * Jail a player
         * =============================================================================================================
         */
        define("jail"){player,args ->
            if(args.size < 3) {
                reject(player,"Usage: ::jail <seconds> <player>")
            }

            val timeSeconds = args[1].toIntOrNull()
            if(timeSeconds == null){
                reject(player, "<seconds> Must be a valid integer!")
            }
            if(timeSeconds!! > MAX_JAIL_TIME){
                reject(player, "Maximum jail time is $MAX_JAIL_TIME seconds.")
            }
            val name = args.slice(2 until args.size).joinToString("_")
            val otherPlayer = Repository.getPlayerByName(name)
            if(otherPlayer == null){
                reject(player, "Can not find $name in the player list!")
            }

            if (otherPlayer?.rights == Rights.ADMINISTRATOR){
                reject(player, "You cannot jail $name, they are a god. Nice try though ${player.username}!")
            }

            notify(player, "Jailing ${otherPlayer!!.username} for $timeSeconds seconds.")
            notify(otherPlayer, "${player.username} has jailed you for $timeSeconds seconds.")
            GameWorld.Pulser.submit(object : Pulse(3){
                val originalLoc = otherPlayer.location
                val releaseTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeSeconds.toLong())
                override fun pulse(): Boolean {
                    val done = System.currentTimeMillis() >= releaseTime
                    if(otherPlayer.location != Location.create(3228, 3407, 0)){
                        otherPlayer.properties.teleportLocation = Location.create(3228, 3407, 0)
                    }
                    if(done){
                        otherPlayer.properties.teleportLocation = originalLoc
                    }
                    return done
                }
            })
        }
        /**
         * =============================================================================================================
         */
    }
}