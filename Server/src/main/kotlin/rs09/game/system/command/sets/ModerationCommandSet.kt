package rs09.game.system.command.sets

import api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs09.ServerConstants
import rs09.ServerStore
import rs09.ServerStore.Companion.addToList
import rs09.auth.UserAccountInfo
import rs09.game.system.command.CommandMapping
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.io.File
import java.io.FileReader
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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
        define("kick", Privilege.MODERATOR){ player, args ->
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
         * Ban a player
         * =============================================================================================================
         */
        define("ban", Privilege.ADMIN, "::ban <lt>USERNAME<gt> <lt>TIME<gt>", "Bans the user. Time format: <lt>INT<gt>d/s/m/h ex: 30d for 30 days."){ player, args ->
            val name = args[1]
            if(!GameWorld.accountStorage.checkUsernameTaken(name)) {
                reject(player, "Invalid username: $name")
            }
            val playerToKick: Player? = Repository.getPlayerByName(name)
            val durationString = args[2]
            val durationTokens = durationString.toCharArray()
            var intToken = ""
            var durationMillis = 0L
            var durationUnit: TimeUnit = TimeUnit.NANOSECONDS
            for(token in durationTokens){
                if(token.toString().toIntOrNull() != null) intToken += token
                else {
                    val durationInt: Int = (intToken.toIntOrNull() ?: -1).also { if(it == -1) reject(player, "Invalid duration: $intToken") }
                    durationUnit = when(token) {
                        'd' -> TimeUnit.DAYS
                        's' -> TimeUnit.SECONDS
                        'm' -> TimeUnit.MINUTES
                        'h' -> TimeUnit.HOURS
                        else -> TimeUnit.SECONDS
                    }
                    durationMillis = durationUnit.toMillis(durationInt.toLong())
                }
            }

            playerToKick?.details?.accountInfo?.banEndTime = System.currentTimeMillis() + durationMillis
            playerToKick?.clear(true)
            GameWorld.Pulser.submit(object : Pulse(2) {
                override fun pulse(): Boolean {
                    val info = GameWorld.accountStorage.getAccountInfo(name)
                    info.banEndTime = System.currentTimeMillis() + durationMillis
                    GameWorld.accountStorage.update(info)
                    return true
                }
            })

            notify(player, "Banned user $name for $intToken ${durationUnit.name.toLowerCase()}.")
        }
        /**
         * =============================================================================================================
         */

        /**
         * Ban all players on a given IP
         * =============================================================================================================
         */
        define("ipban", Privilege.ADMIN, "::ipban <lt>IP<gt> <lt>TIME<gt>", "Bans all players on the given ip. Time format: <lt>INT<gt>d/s/m/h ex: 30d for 30 days."){ player, args ->
            val ip = args[1]
            val durationString = args[2]
            val durationTokens = durationString.toCharArray()
            var intToken = ""
            var durationMillis = 0L
            var durationUnit: TimeUnit = TimeUnit.NANOSECONDS
            for(token in durationTokens){
                if(token.toString().toIntOrNull() != null) intToken += token
                else {
                    val durationInt: Int = (intToken.toIntOrNull() ?: -1).also { if(it == -1) reject(player, "Invalid duration: $intToken") }
                    durationUnit = when(token) {
                        'd' -> TimeUnit.DAYS
                        's' -> TimeUnit.SECONDS
                        'm' -> TimeUnit.MINUTES
                        'h' -> TimeUnit.HOURS
                        else -> TimeUnit.SECONDS
                    }
                    durationMillis = durationUnit.toMillis(durationInt.toLong())
                }
            }

            val playersToBan = GameWorld.accountStorage.getUsernamesWithIP(ip)
            if (playersToBan.isEmpty()) {
                reject(player, "No accounts found on IP $ip")
            }

            for (p in playersToBan) {
                val playerToKick = Repository.getPlayerByName(p)
                playerToKick?.details?.accountInfo?.banEndTime = System.currentTimeMillis() + durationMillis
                playerToKick?.clear(true)
                GameWorld.Pulser.submit(object : Pulse(2) {
                    override fun pulse(): Boolean {
                        val info = GameWorld.accountStorage.getAccountInfo(p)
                        info.banEndTime = System.currentTimeMillis() + durationMillis
                        GameWorld.accountStorage.update(info)
                        return true
                    }
                })
            }

            ServerStore.getArchive("flagged-ips").addToList("ips", ip)

            notify(player, "Banned all accounts on $ip for $intToken ${durationUnit.name.toLowerCase()}.")
        }
        /**
         * =============================================================================================================
         */

        /**
         * Mute a player
         * =============================================================================================================
         */
        define("mute", Privilege.MODERATOR, "::mute <lt>USERNAME<gt> <lt>TIME<gt>", "Mutes the user. Time format: <lt>INT<gt>d/s/m/h ex: 30d for 30 days."){ player, args ->
            val name = args[1]
            if(!GameWorld.accountStorage.checkUsernameTaken(name)) {
                reject(player, "Invalid username: $name")
            }
            val playerToMute: Player? = Repository.getPlayerByName(name)
            val durationString = args[2]
            val durationTokens = durationString.toCharArray()
            var intToken = ""
            var durationMillis = 0L
            var durationUnit: TimeUnit = TimeUnit.NANOSECONDS
            for(token in durationTokens){
                if(token.toString().toIntOrNull() != null) intToken += token
                else {
                    val durationInt: Int = (intToken.toIntOrNull() ?: -1).also { if(it == -1) reject(player, "Invalid duration: $intToken") }
                    durationUnit = when(token) {
                        'd' -> TimeUnit.DAYS
                        's' -> TimeUnit.SECONDS
                        'm' -> TimeUnit.MINUTES
                        'h' -> TimeUnit.HOURS
                        else -> TimeUnit.SECONDS
                    }
                    durationMillis = durationUnit.toMillis(durationInt.toLong())
                }
            }

            playerToMute?.details?.accountInfo?.muteEndTime = System.currentTimeMillis() + durationMillis
            if(playerToMute == null) { //Player was offline at the time
                GameWorld.Pulser.submit(object : Pulse(2) {
                    override fun pulse(): Boolean {
                        val info = GameWorld.accountStorage.getAccountInfo(name)
                        info.muteEndTime = System.currentTimeMillis() + durationMillis
                        GameWorld.accountStorage.update(info)
                        return true
                    }
                })
            }

            notify(player, "Muted user $name for $intToken ${durationUnit.name.toLowerCase()}.")
        }
        /**
         * =============================================================================================================
         */

        /**
         * Jail a player
         * =============================================================================================================
         */
        define("jail", Privilege.MODERATOR, "::jail <lt>SECONDS<gt> <lt>USERNAME<gt>", "Sends the player to the jail cells in Varrock."){player,args ->
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

        define("modcr", Privilege.MODERATOR, "::modcr <lt>user_name<gt> <lt>amount<gt>", "Modifies user_name's credits by the given amount.") {player, args ->
            val username = (args.getOrNull(1) ?: "").lowercase()
            val amount = args.getOrNull(2)?.toIntOrNull() ?: Integer.MIN_VALUE

            if (username.isEmpty()) {
                reject(player, "Usage: ::modcr user_name amount")
                return@define
            }

            if (amount == Integer.MIN_VALUE) {
                reject(player, "Usage ::modcr user_name amount")
                return@define
            }

            val p = Repository.getPlayerByName(username)
            val info: UserAccountInfo = if (p == null)
                GameWorld.accountStorage.getAccountInfo(username)
            else
                p.details.accountInfo

            if (info.isDefault()) {
                reject(player, "The user you specified ($username) does not exist.")
                return@define
            }

            info.credits += amount
            if (p == null)
                GameWorld.accountStorage.update(info)
            else
                sendMessage(p, "You have been ${if (amount > 0) "granted" else "penalized"} ${abs(amount)} credits.")

            notify(player, "Updated $username's credits to ${info.credits} by ${if (amount > 0) "adding" else "removing"} ${abs(amount)}.")
        }

        define("csvmodcr", Privilege.ADMIN, "::csvmodcr filename", "Awards credits based on a csv list from a file. Relative to data dir.") {player, args ->
            val filename = args.getOrNull(1) ?: ""

            if (filename.isEmpty()) {
                reject(player, "Usage: ::csvmodcr filename")
                return@define
            }

            GlobalScope.launch {
                val f = File(ServerConstants.DATA_PATH + File.separator + filename)
                if (!f.exists()) {
                    notify(player, "Unable to locate file $filename.")
                    notify(player, "Note: The file must be in ${ServerConstants.DATA_PATH}")
                    return@launch
                }
                val lines: List<String>
                withContext(Dispatchers.IO) {
                    FileReader(f).use {
                        lines = f.readLines()
                    }
                }
                for (line in lines) {
                    if (line.startsWith("#")) continue
                    val tokens = line.split(",")
                    val username = tokens[0].lowercase().trim().replace(" ", "_")
                    val amount = tokens[1].trim()
                    CommandMapping.get("modcr")?.handle?.invoke(player, arrayOf("", username, amount))
                }
            }
        }

        define("getattribute", Privilege.ADMIN, "::getattribute <lt>username<gt> <lt>attribute<gt>", "Gets the value of an attribute for a player.") {player, args ->
            val username = args.getOrNull(1) ?: ""
            val attribute = args.getOrNull(2) ?: ""
            if (username.isEmpty() || attribute.isEmpty()) {
                reject(player, "Usage: ::getattribute username attribute")
                return@define
            }
            val p = Repository.getPlayerByName(username)
            if (p == null) {
                reject(player, "Unable to find player $username.")
                return@define
            }
            val value = p.getAttribute<Any>(attribute)
            if (value == null) {
                reject(player, "Unable to find attribute $attribute for player $username.")
                return@define
            }
            notify(player, "Attribute $attribute for player $username is $value.")
        }
    }
}
