package content.region.morytania.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import kotlin.random.Random


/**
 * The area for swamp decay
 */
class SwampDecayArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            ZoneBorders(3392, 3328, 3519, 3455)
        )
    }

    /**
     * Sources:
     * General mechanic: https://runescape.wiki/w/Swamp_decay
     * Historic damage: https://runescape.wiki/w/Mort_Myre_Swamp?oldid=1961846
     * Precise area: https://oldschool.runescape.wiki/w/Swamp_decay
     * Video: https://www.youtube.com/watch?v=gkw959x8Q6s
     * Video: https://www.youtube.com/watch?v=N86RywmEyEU
     */

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            getOrStartTimer<SwampDecayTimer>(entity)
        }
        super.areaEnter(entity)
    }

}

/**
 * The timer for swamp decay
 */
class SwampDecayTimer : PersistTimer (200, "swampdecay", isSoft = true, flags = arrayOf(TimerFlag.ClearOnDeath)) {
    companion object {
        // Silver sickle, Rod of Ivandis, or Ivandis Flail in equip or inv will protect the player.
        private val PROTECTIVE_ITEMS = intArrayOf(
            Items.SILVER_SICKLEB_2963,
            Items.ROD_OF_IVANDIS10_7639,
            Items.ROD_OF_IVANDIS9_7640,
            Items.ROD_OF_IVANDIS8_7641,
            Items.ROD_OF_IVANDIS7_7642,
            Items.ROD_OF_IVANDIS6_7643,
            Items.ROD_OF_IVANDIS5_7644,
            Items.ROD_OF_IVANDIS4_7645,
            Items.ROD_OF_IVANDIS3_7646,
            Items.ROD_OF_IVANDIS2_7647,
            Items.ROD_OF_IVANDIS1_7648,
            Items.IVANDIS_FLAIL_30_13117,
            Items.IVANDIS_FLAIL_29_13118,
            Items.IVANDIS_FLAIL28_13119,
            Items.IVANDIS_FLAIL_27_13120,
            Items.IVANDIS_FLAIL_26_13121,
            Items.IVANDIS_FLAIL_25_13122,
            Items.IVANDIS_FLAIL_24_13123,
            Items.IVANDIS_FLAIL_23_13124,
            Items.IVANDIS_FLAIL_22_13125,
            Items.IVANDIS_FLAIL_21_13126,
            Items.IVANDIS_FLAIL_20_13127,
            Items.IVANDIS_FLAIL_19_13128,
            Items.IVANDIS_FLAIL_18_13129,
            Items.IVANDIS_FLAIL_17_13130,
            Items.IVANDIS_FLAIL_16_13131,
            Items.IVANDIS_FLAIL_15_13132,
            Items.IVANDIS_FLAIL_14_13133,
            Items.IVANDIS_FLAIL_13_13134,
            Items.IVANDIS_FLAIL_12_13135,
            Items.IVANDIS_FLAIL_11_13136,
            Items.IVANDIS_FLAIL_10_13137,
            Items.IVANDIS_FLAIL_9_13138,
            Items.IVANDIS_FLAIL_8_13139,
            Items.IVANDIS_FLAIL_7_13140,
            Items.IVANDIS_FLAIL_6_13141,
            Items.IVANDIS_FLAIL_5_13142,
            Items.IVANDIS_FLAIL_4_13143,
            Items.IVANDIS_FLAIL_3_13144,
            Items.IVANDIS_FLAIL_2_13145,
            Items.IVANDIS_FLAIL_1_13146
        )
    }

    // returning false stops the timer. returning true keeps it running.
    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false

        // player is outside the zone
        if (entity.location.x !in 3392..3519 || entity.location.y !in 3328..3455) {
            sendMessage(entity, "The swamp decay effect is now over.")
            return false
        }

        // the nature spirit camp zone is safe
        if (entity.location.x in 3435..3445 && entity.location.y in 3331..3442) {
            sendMessage(entity, "The aura of Filliman's camp protects you from the swamp.")
            return true
        }

        // protected
        if (hasProtection(entity)) {
            return true
        }

        // decayed (2-4 random damage)
        sendMessage(entity, "The swamp decays you!")
        impact(entity, Random.nextInt(2, 5))
        visualize(entity, -1, 255)
        return true
    }

    override fun onRegister(entity: Entity) {
        if (entity !is Player) return
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val t = SwampDecayTimer()
        t.runInterval = args.getOrNull(0) as? Int ?: 200
        return t
    }

    // always remember your protection
    private fun hasProtection(player: Player): Boolean {
        for (itemId in PROTECTIVE_ITEMS) {
            if (inEquipmentOrInventory(player, itemId)) {
                val prot = when(itemId) {
                    2963 -> "blessed silver sickle"
                    in 7639..7648 -> "rod of Ivandis"
                    in 13117..13146 -> "Ivandis flail"
                    else -> "item" // fallback
                }
                sendMessage(player, "The power of the $prot prevents the swamp decay.")
                visualize(player, -1, 259)
                return true
            }
        }
        return false
    }
}