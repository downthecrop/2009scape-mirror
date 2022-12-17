package rs09.game.content.global.worldevents.holiday.christmas

import api.*
import api.events.EventHook
import api.events.XPGainEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getBoolean
import rs09.ServerStore.Companion.getInt
import rs09.game.Event
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener
import rs09.game.system.command.Privilege
import rs09.game.world.repository.Repository
import rs09.tools.stringtools.colorize

class Giftmas : Commands, StartupListener, LoginListener, InteractionListener {
    override fun startup() {
        if (checkActive())
            init()
    }

    fun checkActive(): Boolean {
        val archive = getArchive()
        return archive.getBoolean("active")
    }

    override fun login(player: Player) {
        if (!checkActive()) return
        player.hook(Event.XpGained, XpGainHook)
    }

    fun init() {
        try {
            on(Items.MYSTERY_BOX_6199, IntType.ITEM, "open") { player, node ->
                val loot = MBOX_LOOT.roll().first()

                if (!removeItem(player, node.asItem())) return@on true

                sendDialogue(player, "You open the mystery box and find ${loot.amount}x ${loot.name.lowercase()}!")
                addItem(player, loot.id, loot.amount)
                return@on true
            }
        } catch (ignored: Exception) {}

        for (player in Repository.players)
            player.hook(Event.XpGained, XpGainHook)
    }

    fun cleanup() {
        for (player in Repository.players)
            player.unhook(XpGainHook)
    }

    override fun defineCommands() {
        define("toggle-giftmas", Privilege.ADMIN, "", "Toggles the giftmas christmas event.") {player, args ->
            val enabled = checkActive()
            getArchive()["active"] = !enabled
            notify(player, "Giftmas is now ${if (enabled) "DISABLED" else "ENABLED"}.")
            if (!enabled) {
                init()
            } else {
                cleanup()
            }
        }
    }

    object XpGainHook : EventHook<XPGainEvent> {
        override fun process(entity: Entity, event: XPGainEvent) {
            val wasCombat = event.skillId in 0..6
            val daily = getDailyGifts(entity.asPlayer(), wasCombat)
            val player = entity.asPlayer()
            val cooldown = entity.getAttribute("christmas-cooldown", 0L)

            if (event.amount < 25.0) return
            if (!RandomFunction.roll(15)) return
            if (daily >= if (wasCombat) DAILY_LIMIT_COMBAT else DAILY_LIMIT_SKILLING) return
            if (System.currentTimeMillis() < cooldown) return
            if (!addItem(player, Items.MYSTERY_BOX_6199)) return

            player.setAttribute("/save:christmas-cooldown", System.currentTimeMillis() + 5000L)
            incrementDailyGifts(player, wasCombat)
            sendMessage(player, MESSAGE_PRESENT_GRANTED)

            if (wasCombat && daily == DAILY_LIMIT_COMBAT - 1)
                sendMessage(player, MESSAGE_DAILYXP_REACHED_COMBAT)
            if (!wasCombat && daily == DAILY_LIMIT_SKILLING - 1)
                sendMessage(player, MESSAGE_DAILYXP_REACHED_SKILLING)
        }

        private fun getDailyGifts(player: Player, wasCombat: Boolean) : Int {
            val archive = if (wasCombat) "daily-xmas-gifts-combat" else "daily-xmas-gifts-skilling"
            return ServerStore.getArchive(archive).getInt(player.name)
        }

        fun incrementDailyGifts(player: Player, wasCombat: Boolean) {
            val start = getDailyGifts(player, wasCombat)
            val archive = if (wasCombat) "daily-xmas-gifts-combat" else "daily-xmas-gifts-skilling"
            ServerStore.getArchive(archive)[player.name] = start + 1
        }
    }

    override fun defineListeners() {}

    companion object {
        private val DAILY_LIMIT_SKILLING = 15
        private val DAILY_LIMIT_COMBAT = 5

        private fun getArchive() : JSONObject {
            val mainArchive = WorldEvents.getArchive()
            if (!mainArchive.containsKey("giftmas"))
                mainArchive["giftmas"] = JSONObject()
            return mainArchive["giftmas"] as JSONObject
        }

        private val MESSAGE_DAILYXP_REACHED_SKILLING = colorize("%RYou have reached your daily limit of presents from skilling!")
        private val MESSAGE_DAILYXP_REACHED_COMBAT = colorize("%RYou have reached your daily limit of presents from combat!")
        private val MESSAGE_PRESENT_GRANTED = colorize("%GYou find a present while training!")
        private val MBOX_LOOT = WeightBasedTable.create(
            WeightedItem(Items.TOY_HORSEY_2520, 1, 1, 0.025),
            WeightedItem(Items.TOY_HORSEY_2522, 1, 1, 0.025),
            WeightedItem(Items.TOY_HORSEY_2524, 1, 1, 0.025),
            WeightedItem(Items.TOY_HORSEY_2526, 1, 1, 0.025),
            WeightedItem(Items.TOY_KITE_12844, 1, 1, 0.025),
            WeightedItem(Items.COAL_453, 1, 1, 0.025),
            WeightedItem(Items.MOLTEN_GLASS_1776, 25, 50, 0.25),
            WeightedItem(Items.FLAX_1780, 15, 70, 0.25),
            WeightedItem(Items.BOW_STRING_1778, 10, 50, 0.15),
            WeightedItem(Items.UNCUT_SAPPHIRE_1624, 1, 5, 0.15),
            WeightedItem(Items.UNCUT_EMERALD_1622, 1, 5, 0.15),
            WeightedItem(Items.UNCUT_RUBY_1620, 1, 5, 0.15),
            WeightedItem(Items.UNCUT_DIAMOND_1618, 1, 5, 0.15),
            WeightedItem(Items.UNCUT_SAPPHIRE_1624, 100, 100, 0.0015),
            WeightedItem(Items.UNCUT_EMERALD_1622, 100, 100, 0.0015),
            WeightedItem(Items.UNCUT_RUBY_1620, 100, 100, 0.0015),
            WeightedItem(Items.UNCUT_DIAMOND_1618, 100, 100, 0.0015),
            WeightedItem(Items.PURE_ESSENCE_7937, 1, 50, 0.15),
            WeightedItem(Items.PURE_ESSENCE_7937, 1000, 1000, 0.0015),
            WeightedItem(Items.RANARR_SEED_5295, 1, 3, 0.065),
            WeightedItem(Items.SNAPDRAGON_SEED_5300, 1, 3, 0.065),
            WeightedItem(Items.GOLD_CHARM_12158, 1, 15, 0.15),
            WeightedItem(Items.CRIMSON_CHARM_12160, 1, 15, 0.15),
            WeightedItem(Items.BLUE_CHARM_12163, 1, 15, 0.15),
            WeightedItem(Items.GREEN_CHARM_12159, 1, 15, 0.15),
            WeightedItem(Items.PURPLE_SWEETS_10476, 1, 15, 0.25),
            WeightedItem(Items.COINS_995, 100, 1000, 0.15),
            WeightedItem(Items.COINS_995, 50000, 100000, 0.0015),
            WeightedItem(Items.COINS_995, 1000000, 1000000, 0.0005),
            WeightedItem(Items.NATURE_RUNE_561, 1, 10, 0.15),
            WeightedItem(Items.ABYSSAL_WHIP_4151, 1, 1, 0.00005),
            WeightedItem(Items.SANTA_HAT_1050, 1, 1, 0.00005),

            ).insertEasyClue(0.015).insertMediumClue(0.010).insertHardClue(0.005).insertRDTRoll(0.015)
    }
}