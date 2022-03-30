package rs09.game.world

import org.json.simple.JSONObject
import rs09.ServerConstants
import java.io.FileInputStream
import java.io.IOException
import java.util.*
/**
 * Represents the game settings used for this game instance.
 * @author Vexia
 */
class GameSettings
/**
 * Constructs a new `GameSettings` `Object`.
 * @param name the name.
 * @param beta the beta.
 * @param type the game type.
 * @param gui if gui is enabled.
 * @param worldId the world id.
 * @param countryIndex The country index.
 * @param members If the world is members only.
 * @param msAddress The address of the Management server.
 */ internal constructor(
        /**
         * The name of the namme.
         */
        var name: String,
        /**
         * If the game is in beta mode.
         */
        var isBeta: Boolean,
        /**
         * If the game is in developer mode.
         */
        var isDevMode: Boolean,
        /**
         * If the gui is enabled.
         */
        var isGui: Boolean,
        /**
         * The world id of the server.
         */
        var worldId: Int,
        /**
         * The country index.
         */
        var countryIndex: Int,
        /**
         * The activity.
         */
        var activity: String,
        /**
         * If the world is members only.
         */
        var isMembers: Boolean,
        /**
         * If the world is a pvp world.
         */
        var isPvp: Boolean,
        /**
         * If only quick chat can be used on the world.
         */
        var isQuickChat: Boolean,
        /**
         * If lootshare option is enabled on this world.
         */
        var isLootshare: Boolean,
        /**
         * The address of the Management server.
         */
        var msAddress: String,
        var default_xp_rate: Double,
        var allow_slayer_reroll: Boolean,
        var enable_default_clan: Boolean,
        var enable_bots: Boolean,
        var autostock_ge: Boolean,
        var allow_token_purchase: Boolean,
        var skillcape_perks: Boolean,
        var increased_door_time: Boolean,
        var enabled_botting : Boolean,
        var max_adv_bots: Int,
        var wild_pvp_enabled: Boolean,
        var jad_practice_enabled: Boolean,

        /**"Lobby" interface
         * The message of the week models to display
         * 15 & 22 = keys & lock || 16 = fly swat || 17 = person with question marks || 18 & 447 = wise old man
         * 19 = man & woman with mouth closed || 20 = man & lock & key || 21 = closed chests
         * 23 = snowmen || 405 = Construction houses || 622 = Two sets of 3 people range, mage, melee
         * 623 = Woodcutting || 679 = Summoning || 715 = Easter || 800 = Halloween
         * Any value that isn't one listed above = random selection
         */
        var message_model: Int,

        /**"Lobby" interface
         * The message of the week text
         * The "child" for writing text to these interfaces is located inside of LoginConfiguration.java
         * method: getMessageChild
         */
        var message_string: String
        ) {
    val isHosted: Boolean
        get() = !isDevMode

    override fun toString(): String {
        return "GameSettings [name=$name, debug=$isBeta, devMode=$isDevMode, gui=$isGui, worldId=$worldId]"
    }

    companion object {
        /**
         * Parses a JSONObject and creates a new GameSettings object from it.
         * @param data the JSONObject to parse.
         * @return the settings object.
         * @author Ceikry
         */
        fun parse(data: JSONObject): GameSettings? {
            val name = ServerConstants.SERVER_NAME
            val debug = data["debug"] as Boolean
            val dev = data["dev"] as Boolean
            val startGui = data["startGui"] as Boolean
            val worldId = data["worldID"].toString().toInt()
            val countryId = data["countryID"].toString().toInt()
            val activity = data["activity"].toString()
            val pvpWorld = data["pvpWorld"] as Boolean
            val msip = data["msip"].toString()
            val default_xp_rate = data["default_xp_rate"].toString().toDouble()
            val allow_slayer_reroll = data["allow_slayer_reroll"] as Boolean
            val enable_default_clan = data["enable_default_clan"] as Boolean
            val enable_bots = data["enable_bots"] as Boolean
            val autostock_ge = data["autostock_ge"] as Boolean
            val skillcape_perks = if(data.containsKey("skillcape_perks")) data["skillcape_perks"] as Boolean else false
            val increased_door_time = if(data.containsKey("increased_door_time")) data["increased_door_time"] as Boolean else false
            val enable_botting = if(data.containsKey("botting_enabled")) data["botting_enabled"] as Boolean else false
            val max_adv_bots = if(data.containsKey("max_adv_bots")) data["max_adv_bots"].toString().toInt() else 100
            val wild_pvp_enabled = if(data.containsKey("wild_pvp_enabled")) data["wild_pvp_enabled"] as Boolean else true
            val jad_practice_enabled = if(data.containsKey("jad_practice_enabled")) data["jad_practice_enabled"] as Boolean else true
            val allow_token_purchase = data["allow_token_purchase"] as Boolean
            val message_of_the_week_identifier = data["message_of_the_week_identifier"].toString().toInt()
            val message_of_the_week_text = data["message_of_the_week_text"].toString()
            return GameSettings(
                    name,
                    debug,
                    dev,
                    startGui,
                    worldId,
                    countryId,
                    activity,
                    true,
                    pvpWorld,
                    false,
                    false,
                    msip,
                    default_xp_rate,
                    allow_slayer_reroll,
                    enable_default_clan,
                    enable_bots,
                    autostock_ge,
                    allow_token_purchase,
                    skillcape_perks,
                    increased_door_time,
                    enable_botting,
                    max_adv_bots,
                    wild_pvp_enabled,
                    jad_practice_enabled,
                    message_of_the_week_identifier,
                    message_of_the_week_text
            )
        }

        /**
         * Gets the properties.
         * @param path the path.
         * @return the properties.
         */
        private fun getProperties(path: String): Properties {
            val file: FileInputStream
            val properties = Properties()
            try {
                file = FileInputStream(path)
                properties.load(file)
            } catch (e: IOException) {
                println("Warning: Could not find file in " + System.getProperty("user.dir") + path)
                e.printStackTrace()
            }
            return properties
        }
    }
}