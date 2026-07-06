package content.data.consumables.effects

import core.api.*
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import kotlin.math.ceil
import kotlin.math.floor

class PrayerEffect @JvmOverloads constructor(var base: Double, var bonus: Double, private val roundUp: Boolean = false) : ConsumableEffect() {
    override fun activate(player: Player?) {
        if(player == null) return
        val level = getStatLevel(player, Skills.PRAYER)
        var b = bonus
        if(inInventory(player, Items.HOLY_WRENCH_6714))
            b += 0.02 // Leaving this in for futureproofing. Current update is for properly rounding down.
        val rawAmount = base + (level * b)
        val amount = if (roundUp) ceil(rawAmount) else floor(rawAmount)
        modPrayerPoints(player, amount)
    }
}