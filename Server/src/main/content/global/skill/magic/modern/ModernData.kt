package content.global.skill.magic.modern

import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

enum class ChargeOrbData(
    val obelisk: Int,
    val requiredRunes: Array<Item>,
    val level: Int,
    val experience: Double,
    val graphics: Graphics,
    val sound: Int,
    val chargedOrb: Int
) {
    CHARGE_WATER_ORB(
        Scenery.OBELISK_OF_WATER_2151,
        arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.WATER_RUNE_555, 30), Item(Items.UNPOWERED_ORB_567)),
        56,
        66.0,
        Graphics(149, 90),
        Sounds.CHARGE_WATER_ORB_118,
        Items.WATER_ORB_571
    ),
    CHARGE_EARTH_ORB(
        Scenery.OBELISK_OF_EARTH_29415,
        arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.EARTH_RUNE_557, 30), Item(Items.UNPOWERED_ORB_567)),
        60,
        70.0,
        Graphics(151, 90),
        Sounds.CHARGE_EARTH_ORB_115,
        Items.EARTH_ORB_575
    ),
    CHARGE_FIRE_ORB(
        Scenery.OBELISK_OF_FIRE_2153,
        arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.FIRE_RUNE_554, 30), Item(Items.UNPOWERED_ORB_567)),
        63,
        73.0,
        Graphics(152, 90),
        Sounds.CHARGE_FIRE_ORB_117,
        Items.FIRE_ORB_569
    ),
    CHARGE_AIR_ORB(
        Scenery.OBELISK_OF_AIR_2152,
        arrayOf(Item(Items.COSMIC_RUNE_564, 3), Item(Items.AIR_RUNE_556, 30), Item(Items.UNPOWERED_ORB_567)),
        66,
        76.0,
        Graphics(150, 90),
        Sounds.CHARGE_AIR_ORB_116,
        Items.AIR_ORB_573
    );
    companion object{
        val spellMap = HashMap<Int, ChargeOrbData>()

        init {
            for (spell in ChargeOrbData.values()) {
                spellMap[spell.obelisk] = spell
            }
        }
    }
}