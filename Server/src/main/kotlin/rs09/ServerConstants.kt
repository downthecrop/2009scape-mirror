package rs09

import core.game.system.SystemShutdownHook
import core.game.world.map.Location
import core.tools.mysql.Database
import rs09.tools.secondsToTicks
import java.math.BigInteger

/**
 * A class holding various variables for the server.
 * @author Ceikry
 */
class ServerConstants {
	companion object {
		var NOAUTH_DEFAULT_ADMIN: Boolean = true

        @JvmField
		var DAILY_ACCOUNT_LIMIT = 3

        @JvmField
		var REVENANT_POPULATION: Int = 30

        @JvmField
		var BOTS_INFLUENCE_PRICE_INDEX = true

        @JvmField
		var SHUTDOWN_HOOK: Thread = Thread(SystemShutdownHook())

		@JvmField
		var ALLOW_GUI: Boolean = false

        @JvmField
		var DATA_PATH: String? = null

		//path to the cache
		@JvmField
		var CACHE_PATH: String? = null

		//path for the server store
		@JvmField
		var STORE_PATH: String? = null

		//path for player saves
		@JvmField
		var PLAYER_SAVE_PATH: String? = null

		@JvmField
		var PLAYER_ATTRIBUTE_PATH = "ish";

		//path to the various config files, such as npc_spawns.json
		var CONFIG_PATH: String? = null

		@JvmField
		var GRAND_EXCHANGE_DATA_PATH: String? = null

		@JvmField
		var RDT_DATA_PATH: String? = null

		@JvmField
		var OBJECT_PARSER_PATH: String? = null

		@JvmField
		var SCRIPTS_PATH: String? = null

		@JvmField
		var DIALOGUE_SCRIPTS_PATH: String? = null

		@JvmField
		var LOGS_PATH: String? = null

		@JvmField
		var BOT_DATA_PATH: String? = null

		@JvmField
		var CELEDT_DATA_PATH: String? = null

		@JvmField
		var USDT_DATA_PATH: String? = null

		@JvmField
		var HDT_DATA_PATH: String? = null

		@JvmField
		var GDT_DATA_PATH: String? = null

		@JvmField
		var RSDT_DATA_PATH: String? = null

		@JvmField
		var ASDT_DATA_PATH: String? = null

		//the max number of players.
		@JvmField
		var MAX_PLAYERS = 2000

		//the max number of NPCs
		@JvmField
		var MAX_NPCS = 32000

		//the location where new players are placed on login.
		@JvmField
		var START_LOCATION: Location? = null

		//Location for all home teleports/respawn location
		@JvmField
		var HOME_LOCATION: Location? = null

		//the name for the database
		@JvmField
		var DATABASE_NAME: String? = null

		//username for the database
		@JvmField
		var DATABASE_USER: String? = null

		//password for the database
		@JvmField
		var DATABASE_PASS: String? = null

		//address for the database
		@JvmField
		var DATABASE_ADDRESS: String? = null

		@JvmField
		var DATABASE_PORT: String? = null

		@JvmField
		var WRITE_LOGS: Boolean = false

		@JvmField
		var BANK_SIZE: Int = 496

		@JvmField
		var BANK_BOOTH_QUICK_OPEN: Boolean = false

		@JvmField
		var BANK_BOOTH_NOTE_ENABLED: Boolean = true

		@JvmField
		var BANK_BOOTH_NOTE_UIM: Boolean = true

		@JvmField
		var GE_AUTOSAVE_FREQUENCY = secondsToTicks(3600) //1 hour

		@JvmField
		var GE_AUTOSTOCK_ENABLED = false

		@JvmField
		var MS_SECRET_KEY = ""

		@JvmField
		var LOG_CUTSCENE = true

		@JvmField
		var RULES_AND_INFO_ENABLED = true

		@JvmField
		var WATCHDOG_ENABLED = true

		@JvmField
		var I_AM_A_CHEATER = false

		//location names for the ::to command.
		val TELEPORT_DESTINATIONS = arrayOf(
			arrayOf(Location.create(2974, 4383, 2), "corp", "corporal", "corporeal"),
			arrayOf(Location.create(2659, 2649, 0), "pc", "pest control", "pest"),
			arrayOf(Location.create(3293, 3184, 0), "al kharid", "alkharid", "kharid"),
			arrayOf(Location.create(3222, 3217, 0), "lumbridge", "lumby"),
			arrayOf(Location.create(3110, 3168, 0), "wizard tower", "wizards tower", "tower", "wizards"),
			arrayOf(Location.create(3083, 3249, 0), "draynor", "draynor village"),
			arrayOf(Location.create(3019, 3244, 0), "port sarim", "sarim"),
			arrayOf(Location.create(2956, 3209, 0), "rimmington"),
			arrayOf(Location.create(2965, 3380, 0), "fally", "falador"),
			arrayOf(Location.create(2895, 3436, 0), "taverley"),
			arrayOf(Location.create(3080, 3423, 0), "barbarian village", "barb"),
			arrayOf(Location.create(3213, 3428, 0), "varrock"),
			arrayOf(Location.create(3164, 3485, 0), "grand exchange", "ge"),
			arrayOf(Location.create(2917, 3175, 0), "karamja"),
			arrayOf(Location.create(2450, 5165, 0), "tzhaar"),
			arrayOf(Location.create(2795, 3177, 0), "brimhaven"),
			arrayOf(Location.create(2849, 2961, 0), "shilo village", "shilo"),
			arrayOf(Location.create(2605, 3093, 0), "yanille"),
			arrayOf(Location.create(2663, 3305, 0), "ardougne", "ardy"),
			arrayOf(Location.create(2450, 3422, 0), "gnome stronghold", "gnome"),
			arrayOf(Location.create(2730, 3485, 0), "camelot", "cammy", "seers"),
			arrayOf(Location.create(2805, 3435, 0), "catherby"),
			arrayOf(Location.create(2659, 3657, 0), "rellekka"),
			arrayOf(Location.create(2890, 3676, 0), "trollheim"),
			arrayOf(Location.create(2914, 3746, 0), "godwars", "gwd", "god wars"),
			arrayOf(Location.create(3180, 3684, 0), "bounty hunter", "bh"),
			arrayOf(Location.create(3272, 3687, 0), "clan wars", "clw"),
			arrayOf(Location.create(3090, 3957, 0), "mage arena", "mage", "magearena", "arena"),
			arrayOf(Location.create(3069, 10257, 0), "king black dragon", "kbd"),
			arrayOf(Location.create(3359, 3416, 0), "digsite"),
			arrayOf(Location.create(3488, 3489, 0), "canifis"),
			arrayOf(Location.create(3428, 3526, 0), "slayer tower", "slayer"),
			arrayOf(Location.create(3502, 9483, 2), "kalphite queen", "kq", "kalphite hive", "kalphite"),
			arrayOf(Location.create(3233, 2913, 0), "pyramid"),
			arrayOf(Location.create(3419, 2917, 0), "nardah"),
			arrayOf(Location.create(3482, 3090, 0), "uzer"),
			arrayOf(Location.create(3358, 2970, 0), "pollnivneach", "poln"),
			arrayOf(Location.create(3305, 2788, 0), "sophanem"),
			arrayOf(Location.create(2898, 3544, 0), "burthorpe", "burthorp"),
			arrayOf(Location.create(3088, 3491, 0), "edge", "edgeville"),
			arrayOf(Location.create(3169, 3034, 0), "bedabin"),
			arrayOf(Location.create(3565, 3289, 0), "barrows"),
			arrayOf(Location.create(3016, 3513, 0), "bkf", "black knights fortress"),
			arrayOf(Location.create(3052, 3481, 0), "monastery"),
			arrayOf(Location.create(1945, 4959, 0), "blast furnace", "blast"),
			arrayOf(Location.create(2408, 4449, 0), "zanaris"),
			arrayOf(Location.create(3656, 3517, 0), "ectofuntus", "ecto"),
			arrayOf(Location.create(2408, 4449, 0), "tower of life lower"),
			arrayOf(Location.create(2894, 4756, 0), "test area"),
			arrayOf(Location.create(2722, 4886, 0), "quest the golem 1"),
			arrayOf(Location.create(2704, 5349, 0), "dorgeshuun", "dorg"),
			arrayOf(Location.create(2711, 10132, 0), "brine rats"),
			arrayOf(Location.create(2328, 3677, 0), "piscatoris"),
			arrayOf(Location.create(2660, 3158, 0), "fishing trawler", "trawler"),
			arrayOf(Location.create(2800, 3667, 0), "mountain camp"),
			arrayOf(Location.create(2575, 3250, 0), "clocktower")
		)
		//arrayOf(Location.create(0, 0, 0), ""),

		@JvmField
		var DATABASE: Database? = null

		//if SQL is enabled
		@JvmField
		var MYSQL = true

		//the server name
		@JvmField
		var SERVER_NAME: String = ""

		//the server's grand exchange name
		@JvmField
		var SERVER_GE_NAME: String = ""

		//The RSA_KEY for the server.
		@JvmField
		var EXPONENT = BigInteger("52317200263721308660411803146360972546561037484450290559823448967617618536819222494429186211525706853703641369936136465589036631055945454547936148730495933263344792588795811788941129493188907621550836988152620502378278134421731002382361670176785306598134280732756356458964850508114958769985438054979422820241")

		//The MODULUS for the server.
		@JvmField
		var MODULUS = BigInteger("96982303379631821170939875058071478695026608406924780574168393250855797534862289546229721580153879336741968220328805101128831071152160922518190059946555203865621183480223212969502122536662721687753974815205744569357388338433981424032996046420057284324856368815997832596174397728134370577184183004453899764051")

		@JvmField
		var DAILY_RESTART = false

		@JvmField
		var DISCORD_GE_WEBHOOK = ""

		@JvmField
		var DISCORD_MOD_WEBHOOK = ""

		@JvmField
		var PRELOAD_MAP = false

		@JvmField
		var USE_AUTH = false

		@JvmField
		var PERSIST_ACCOUNTS = false
	}
}
