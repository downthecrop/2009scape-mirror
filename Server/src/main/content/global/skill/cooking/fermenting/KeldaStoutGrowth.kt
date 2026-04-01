package content.global.skill.cooking.fermenting

import core.api.getVarbit
import core.api.sendMessage
import core.game.node.entity.Entity
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer

/**
 * Independent brewing timer and functions exclusively for Kelda Stout
 * Varbit progression: 68 (mixed) -> 69 (brewing1) -> 70 (brewing2) -> 71 (done)
 */

class KeldaStoutGrowth : PersistTimer( 5 /*minutes*/ * 100 /*ticks per minute*/, "cooking:brewing-kelda", isSoft = true) {
    lateinit var player: Player

    override fun run(entity: Entity): Boolean {
        player = entity as Player
        when (val currentVarbit = getVarbit(player, 736)) {
            69 -> {
                setVarbit(player, 736, currentVarbit + 1)
                return true
            }
            70 -> {
                setVarbit(player, 736, currentVarbit + 1)
                sendMessage(player, "Perhaps I should have a look and see if my Kelda Stout has brewed...")
                return false
            }
            else -> {
                return false
            }
        }
    }

    override fun getTimer(vararg args: Any): RSTimer {
        return KeldaStoutGrowth()
    }
}


object KeldaBypasses {

    fun keldaAddHops(player: Player) {
        queueScript(player, 0) {
            stopWalk(player)
            if (!removeItem(player, Item(Items.KELDA_HOPS_6113, 1))) { return@queueScript stopExecuting(player) }
            sendMessage(player, "You add some Kelda hops to the vat.")
            lock(player, Animation(2295).duration + 1)
            animate(player, Animation(2295))
            setVarbit(player, 736, getVarbit(player, 736) + 66)
            return@queueScript stopExecuting(player)
        }
    }

    fun keldaAddYeast(player: Player) {
        //addYeast() override
        queueScript(player, 0) {

            if (!removeItem(player, Item(Items.ALE_YEAST_5767, 1))) { return@queueScript stopExecuting(player) }
            addItem(player, Items.EMPTY_POT_1931)

            lock(player, Animation(2283).duration)
            animate(player, Animation(2295))

            sendMessage(player, "You add a pot of ale yeast to the vat.")
            sendMessage(player, "The contents of the vat have begun to ferment.")

            setVarbit(player, 736, getVarbit(player, 736) + 1)
            registerTimer(player, KeldaStoutGrowth())
            return@queueScript true
        }
    }

    fun keldaTurnValve(player: Player) {
        queueScript(player, 0, QueueStrength.SOFT) {

            lock(player, Animation(780).duration)
            animate(player, Animation(780))

            sendMessage(player, "You turn the valve.")

            delayScript(player, 2)

            // Fill the barrel, then empty the vat
            setVarbit(player, 738, 3, true)
            setVarbit(player, 736, 0, true)

            sendMessage(player, "The barrel now contains 1 pint of Kelda Stout.")
            return@queueScript stopExecuting(player)
        }
    }

    fun keldaIsBrewing(player: Player): Boolean {
        val vatVarbit = getVarbit(player, 736)
        return vatVarbit in 69..70
    }

    fun keldaIsComplete(player: Player): Boolean {
        val vatVarbit = getVarbit(player, 736)
        return vatVarbit == 71
    }
}
