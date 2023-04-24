package core.game.system.config

import com.moandjiezana.toml.Toml
import core.game.world.map.Location
import core.tools.mysql.Database
import core.ServerConstants
import core.api.log
import core.api.parseEnumEntry
import core.tools.SystemLogger
import core.game.world.GameSettings
import core.game.world.GameWorld
import core.tools.Log
import core.tools.LogLevel
import java.io.File
import java.net.URL
import kotlin.system.exitProcess

/**
 * Class for parsing the server config, I.E default.toml
 * @param path the path to the TOML file to parse.
 * @author Ceikry
 */
object ServerConfigParser {
    var confFile: File? = null
    var tomlData: Toml? = null

    fun parse(path: String){
        confFile = File(parsePath(path))
        parseFromFile(confFile)
    }

    fun parse(path: URL?) {
        confFile = File(path!!.toURI())
        parseFromFile(confFile)
    }

    private fun parseFromFile(confFile: File?) {
        if(!confFile!!.canonicalFile.exists()){
            log(this::class.java, Log.ERR,  "${confFile.canonicalFile} does not exist.")
            exitProcess(0)
        } else {
            try {
                tomlData = Toml().read(confFile)
                parseServerSettings()
                parseGameSettings()
                val jvmString = System.getProperty("java.version")
                if (jvmString.startsWith("1.")) {
                    ServerConstants.JAVA_VERSION = jvmString.substring(2,3).toInt()
                } else if (!jvmString.startsWith("1.")) {
                    ServerConstants.JAVA_VERSION = jvmString.substring(0,2).toInt()
                } else if (!jvmString.contains(".")) {
                    ServerConstants.JAVA_VERSION = jvmString.toInt()
                }
                log(this::class.java, Log.FINE, "It seems we are in a Java ${ServerConstants.JAVA_VERSION} environment.")
            } catch (e: java.lang.IllegalStateException) {
                log(this::class.java, Log.ERR,  "Passed config file is not a TOML file. Path: ${confFile.canonicalPath}")
                log(this::class.java, Log.ERR,  "Exception received: $e")
                exitProcess(0)
            }
        }
    }

    private fun parseGameSettings(){
        tomlData ?: return
        val data = tomlData!!

        GameWorld.settings = GameSettings(
            name = ServerConstants.SERVER_NAME,
            isBeta = data.getBoolean("world.debug"),
            isDevMode = data.getBoolean("world.dev"),
            isGui = data.getBoolean("world.start_gui"),
            worldId = data.getString("world.world_id").toInt(),
            countryIndex = data.getString("world.country_id").toInt(),
            activity = data.getString("world.activity"),
            isMembers = data.getBoolean("world.members"),
            isPvp = data.getBoolean("world.pvp"),
            isQuickChat = false,
            isLootshare = false,
            msAddress = data.getString("server.msip"),
            default_xp_rate = data.getDouble("world.default_xp_rate"),
            allow_slayer_reroll = data.getBoolean("world.allow_slayer_reroll"),
            enable_default_clan = data.getBoolean("world.enable_default_clan"),
            enable_bots = data.getBoolean("world.enable_bots"),
            autostock_ge = data.getBoolean("world.autostock_ge"),
            allow_token_purchase = data.getBoolean("world.allow_token_purchase"),
            skillcape_perks = data.getBoolean("world.skillcape_perks"),
            increased_door_time = data.getBoolean("world.increased_door_time"),
            enabled_botting = data.getBoolean("world.enable_botting"),
            max_adv_bots = data.getLong("world.max_adv_bots").toInt(),
            enable_doubling_money_scammers = data.getBoolean("world.enable_doubling_money_scammers", false),
            wild_pvp_enabled = data.getBoolean("world.wild_pvp_enabled"),
            jad_practice_enabled = data.getBoolean("world.jad_practice_enabled"),
            ge_announcement_limit = data.getLong("world.ge_announcement_limit", 500L).toInt(),
            smartpathfinder_bfs = data.getBoolean("world.smartpathfinder_bfs", false),
            enable_castle_wars = data.getBoolean("world.enable_castle_wars", false),
            message_model = data.getString("world.motw_identifier").toInt(),
            message_string = data.getString("world.motw_text").replace("@name", ServerConstants.SERVER_NAME)
        )
    }

    private fun parseServerSettings(){
        tomlData ?: return
        val data = tomlData!!

        ServerConstants.DATA_PATH = data.getString("paths.data_path")
        ServerConstants.WRITE_LOGS = data.getBoolean("server.write_logs")
        ServerConstants.DATABASE_NAME = data.getString("database.database_name")
        ServerConstants.DATABASE_USER = data.getString("database.database_username")
        ServerConstants.DATABASE_PASS = data.getString("database.database_password")
        ServerConstants.DATABASE_ADDRESS = data.getString("database.database_address")
        ServerConstants.DATABASE_PORT = data.getString("database.database_port")
        ServerConstants.DATABASE = Database(ServerConstants.DATABASE_ADDRESS + ":" + ServerConstants.DATABASE_PORT, ServerConstants.DATABASE_NAME, ServerConstants.DATABASE_USER, ServerConstants.DATABASE_PASS)
        ServerConstants.CACHE_PATH = data.getPath("paths.cache_path")
        ServerConstants.CONFIG_PATH = data.getPath("paths.configs_path")
        ServerConstants.PLAYER_SAVE_PATH = data.getPath("paths.save_path")
        ServerConstants.STORE_PATH = data.getPath("paths.store_path")
        ServerConstants.RDT_DATA_PATH = data.getPath("paths.rare_drop_table_path")
        ServerConstants.OBJECT_PARSER_PATH = data.getPath("paths.object_parser_path")
        ServerConstants.LOGS_PATH = data.getPath("paths.logs_path")
        ServerConstants.SERVER_NAME = data.getPath("world.name")
        ServerConstants.BOT_DATA_PATH = data.getPath("paths.bot_data")
        ServerConstants.MS_SECRET_KEY = data.getString("server.secret_key")
        ServerConstants.HOME_LOCATION = parseLocation(data.getString("world.home_location"))
        ServerConstants.START_LOCATION = parseLocation(data.getString("world.new_player_location"))
        ServerConstants.DAILY_RESTART = data.getBoolean("world.daily_restart")
        ServerConstants.LOG_CUTSCENE = data.getBoolean("world.verbose_cutscene", false)
        ServerConstants.GRAND_EXCHANGE_DATA_PATH = data.getPath("paths.eco_data")
        ServerConstants.CELEDT_DATA_PATH = data.getPath("paths.cele_drop_table_path")
        ServerConstants.USDT_DATA_PATH = data.getPath("paths.uncommon_seed_drop_table_path")
        ServerConstants.HDT_DATA_PATH = data.getPath("paths.herb_drop_table_path")
        ServerConstants.GDT_DATA_PATH = data.getPath("paths.gem_drop_table_path")
        ServerConstants.RSDT_DATA_PATH = data.getPath("paths.rare_seed_drop_table_path")
        ServerConstants.ASDT_DATA_PATH = data.getPath("paths.allotment_seed_drop_table_path")
        ServerConstants.SERVER_GE_NAME = data.getString("world.name_ge") ?: ServerConstants.SERVER_NAME
        ServerConstants.RULES_AND_INFO_ENABLED = data.getBoolean("world.show_rules", true)
        ServerConstants.BOTS_INFLUENCE_PRICE_INDEX = data.getBoolean("world.bots_influence_ge_price", true)
        ServerConstants.PRELOAD_MAP = data.getBoolean("server.preload_map", false)
        ServerConstants.REVENANT_POPULATION = data.getLong("world.revenant_population", 30L).toInt()
        ServerConstants.BANK_BOOTH_QUICK_OPEN = data.getBoolean("world.bank_booth_quick_open", false)
        ServerConstants.BANK_BOOTH_NOTE_ENABLED = data.getBoolean("world.bank_booth_note_enabled", true)
        ServerConstants.BANK_BOOTH_NOTE_UIM = data.getBoolean("world.bank_booth_note_uim", true)
        ServerConstants.DISCORD_GE_WEBHOOK = data.getString("server.discord_webhook", "")
        ServerConstants.WATCHDOG_ENABLED = data.getBoolean("server.watchdog_enabled", true)
        ServerConstants.I_AM_A_CHEATER = data.getBoolean("world.i_want_to_cheat", false)
        ServerConstants.USE_AUTH = data.getBoolean("server.use_auth", true)
        ServerConstants.PERSIST_ACCOUNTS = data.getBoolean("server.persist_accounts", true)
        ServerConstants.DAILY_ACCOUNT_LIMIT = data.getLong("server.daily_accounts_per_ip", 3L).toInt()
        ServerConstants.DISCORD_MOD_WEBHOOK = data.getString("server.moderation_webhook", "")
        ServerConstants.NOAUTH_DEFAULT_ADMIN = data.getBoolean("server.noauth_default_admin", false)
        ServerConstants.DRAGON_AXE_USE_OSRS_SPEC = data.getBoolean("world.dragon_axe_use_osrs_spec", false)
        ServerConstants.DISCORD_OPENRSC_HOOK = data.getString("server.openrsc_integration_webhook", "")
        ServerConstants.ENABLE_GLOBALCHAT = data.getBoolean("world.enable_globalchat", true)
        ServerConstants.MAX_PATHFIND_DISTANCE = data.getLong("server.max_pathfind_dist", 25L).toInt()

        val logLevel = data.getString("server.log_level", "VERBOSE").uppercase()
        ServerConstants.LOG_LEVEL = parseEnumEntry<LogLevel>(logLevel) ?: LogLevel.VERBOSE
    }


    private fun Toml.getPath(key: String): String{
        try {
            return parsePath(getString(key).replace("@data", ServerConstants.DATA_PATH!!))
        } catch (e: Exception){
            log(this::class.java, Log.ERR,  "Error parsing key: $key")
            exitProcess(0)
        }
    }

    /**
     * Parses a location from the format "x,y,z"
     * @author Ceikry
     * @param locString The string to parse
     * @return Location
     */
    fun parseLocation(locString: String): Location {
        val locTokens = locString.split(",").map { it.toInt() }
        return Location(locTokens[0], locTokens[1], locTokens[2])
    }

    /**
     * Parses a path string
     * @author Ceikry
     * @param pathString The string to parse
     * @return a String with the proper file separators for the current OS.
     */
    fun parsePath(pathString: String): String {
        var pathTokens: List<String>? = null
        if(pathString.contains("/"))
            pathTokens = pathString.split("/")
        else if(pathString.contains("\\"))
            pathTokens = pathString.split("\\")

        pathTokens ?: return pathString //return the initial pathString if path does not contain file separators.
        var pathProduct = ""
        for(token in pathTokens){
            if(token != "")
                pathProduct += "$token${File.separator}"
        }

        return pathProduct
    }
}
