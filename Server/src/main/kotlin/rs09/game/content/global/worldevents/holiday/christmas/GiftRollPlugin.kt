package rs09.game.content.global.worldevents.holiday.christmas

import api.Container
import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getInt
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger
import rs09.plugin.CorePluginTypes.XPGainPlugin
import rs09.tools.stringtools.colorize
import java.time.Month
import java.util.*

class GiftRollPlugin : XPGainPlugin() {
    override fun run(player: Player, skill: Int, amount: Double) {
        val numDaily = getDailyGifts(player)

        val cooldown = player.getAttribute("christmas-cooldown", 0L)
        if(System.currentTimeMillis() < cooldown) return
        player.setAttribute("/save:christmas-cooldown", System.currentTimeMillis() + 5000L)

        if(System.currentTimeMillis() > cooldown && numDaily < 10 && RandomFunction.roll(15).also { player.debug("Rolling gift: $it") } && amount > 20) {
            incrementDailyGifts(player)

            addItemOrDrop(player, Items.MYSTERY_BOX_6199)
            sendMessage(player, colorize("%RMerry Christmas! %GYou randomly receive a mystery box while training ${Skills.SKILL_NAME[skill]}!"))
        }
    }

    private fun getDailyGifts(player: Player) : Int {
        return ServerStore.getArchive("daily-xmas-gifts").getInt(player.name)
    }

    fun incrementDailyGifts(player: Player) {
        val start = getDailyGifts(player)
        ServerStore.getArchive("daily-xmas-gifts")[player.name] = start + 1
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }
}

class XMASMboxHandler : InteractionListener() {
    val MBOX = Items.MYSTERY_BOX_6199

    override fun defineListeners() {
        //disable if it's not december.
        //Calendar.MONTH is 0-indexed, Month is 1-indexed. I know. It's dumb. Thanks oracle.
        if(Calendar.getInstance().get(Calendar.MONTH) + 1 != Month.DECEMBER.value){
            SystemLogger.logInfo("${Calendar.getInstance().get(Calendar.MONTH)} - ${Month.DECEMBER.value}")
            return
        }

        on(MBOX, ITEM, "open"){player, used ->
            val item = MBOX_LOOT.roll().first()

            if(removeItem(player, used, Container.INVENTORY)) {
                sendDialogue(player, "You open the gift and find ${item.amount} ${item.name}!")
                addItemOrDrop(player, item.id, item.amount)
            }
            return@on true
        }
    }

    val MBOX_LOOT = WeightBasedTable.create(
        WeightedItem(Items.TOY_HORSEY_2520, 1, 1, 0.025),
        WeightedItem(Items.TOY_HORSEY_2522, 1, 1, 0.025),
        WeightedItem(Items.TOY_HORSEY_2524, 1, 1, 0.025),
        WeightedItem(Items.TOY_HORSEY_2526, 1, 1, 0.025),
        WeightedItem(Items.TOY_KITE_12844, 1, 1, 0.025),
        WeightedItem(Items.COAL_453, 1, 1, 0.025),
        WeightedItem(Items.MOLTEN_GLASS_1776, 25, 100, 0.25),
        WeightedItem(Items.FLAX_1780, 15, 70, 0.25),
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
        WeightedItem(Items.SPIRIT_SHARDS_12183, 1, 100, 0.15),
        WeightedItem(Items.PURPLE_SWEETS_10476, 1, 15, 0.25),
        WeightedItem(Items.COINS_995, 100, 1000, 0.15),
        WeightedItem(Items.COINS_995, 50000, 100000, 0.0015),
        WeightedItem(Items.COINS_995, 1000000, 1000000, 0.0005),
        WeightedItem(Items.NATURE_RUNE_561, 1, 10, 0.15),
        WeightedItem(Items.ABYSSAL_WHIP_4151, 1, 1, 0.00005),
        WeightedItem(Items.SANTA_HAT_1050, 1, 1, 0.00005),

    ).insertEasyClue(0.075).insertMediumClue(0.05).insertHardClue(0.025).insertRDTRoll(0.015)
}