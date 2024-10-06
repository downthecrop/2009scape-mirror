package content

import TestUtils
import content.global.handlers.item.equipment.special.ChinchompaSwingHandler
import core.api.EquipmentSlot
import core.game.container.impl.EquipmentContainer.updateBonuses
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.entity.combat.MagicSwingHandler
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.combat.RangeSwingHandler
import core.game.node.entity.combat.SwingHandlerFlag
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class CombatTests {
    init {
        TestUtils.preTestSetup()
    }

    @Test
    fun swingHandlersFlaggedAsIgnoreStatsShouldNotInfluenceDamage() {
        val testRangeHandler = RangeSwingHandler(SwingHandlerFlag.IGNORE_STAT_BOOSTS_DAMAGE)
        val testMeleeHandler = MeleeSwingHandler(SwingHandlerFlag.IGNORE_STAT_BOOSTS_DAMAGE)

        TestUtils.getMockPlayer("ignorestatdamage").use { p ->
            p.skills.setLevel(Skills.RANGE, 99)
            val baselineRanged = testRangeHandler.calculateHit(p, p, 1.0)
            p.properties.bonuses[14] = 250
            Assertions.assertEquals(baselineRanged, testRangeHandler.calculateHit(p, p, 1.0))

            p.skills.setLevel(Skills.STRENGTH, 99)
            val baselineMelee = testMeleeHandler.calculateHit(p, p, 1.0)
            p.properties.bonuses[11] = 250
            Assertions.assertEquals(baselineMelee, testMeleeHandler.calculateHit(p, p, 1.0))
        }
    }

    @Test
    fun swingHandlersFlaggedAsIgnoreStatsShouldNotInfluenceAccuracy() {
        val testRangeHandler = RangeSwingHandler(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY)
        val testMeleeHandler = MeleeSwingHandler(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY)
        val testMagicHandler = MagicSwingHandler(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY)

        TestUtils.getMockPlayer("ignorestataccuracy").use { p ->
            p.properties.attackStyle = WeaponInterface.AttackStyle(0, WeaponInterface.BONUS_RANGE)
            p.skills.setLevel(Skills.RANGE, 99)
            val baselineRanged = testRangeHandler.calculateAccuracy(p)
            p.properties.bonuses[WeaponInterface.BONUS_RANGE] = 250
            Assertions.assertEquals(baselineRanged, testRangeHandler.calculateAccuracy(p))

            p.properties.attackStyle = WeaponInterface.AttackStyle(0, WeaponInterface.BONUS_STAB)
            p.skills.setLevel(Skills.STRENGTH, 99)
            val baselineMelee = testMeleeHandler.calculateAccuracy(p)
            p.properties.bonuses[WeaponInterface.BONUS_STAB] = 250
            Assertions.assertEquals(baselineMelee, testMeleeHandler.calculateAccuracy(p))

            p.properties.attackStyle = WeaponInterface.AttackStyle(0, WeaponInterface.BONUS_MAGIC)
            p.skills.setLevel(Skills.MAGIC, 99)
            val baselineMagic = testMagicHandler.calculateAccuracy(p)
            p.properties.bonuses[WeaponInterface.BONUS_MAGIC] = 250
            Assertions.assertEquals(baselineMagic, testMagicHandler.calculateAccuracy(p))
        }
    }

    @Test
    fun swingHandlersFlaggedAsIgnorePrayerShouldNotInfluenceDamage() {
        val testRangeHandler = RangeSwingHandler(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE)
        val testMeleeHandler = MeleeSwingHandler(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE)

        TestUtils.getMockPlayer("ignoreprayerdamage").use { p ->
            p.skills.setStaticLevel(Skills.PRAYER, 99)
            p.skills.prayerPoints = 1000.0

            p.skills.setLevel(Skills.RANGE, 99)
            val baselineRanged = testRangeHandler.calculateHit(p, p, 1.0)
            p.prayer.toggle(PrayerType.EAGLE_EYE)
            Assertions.assertEquals(baselineRanged, testRangeHandler.calculateHit(p, p, 1.0))
            p.prayer.toggle(PrayerType.EAGLE_EYE)

            p.skills.setLevel(Skills.STRENGTH, 99)
            val baselineMelee = testMeleeHandler.calculateHit(p, p, 1.0)
            p.prayer.toggle(PrayerType.SUPERHUMAN_STRENGTH)
            Assertions.assertEquals(baselineMelee, testMeleeHandler.calculateHit(p, p, 1.0))
            p.prayer.toggle(PrayerType.SUPERHUMAN_STRENGTH)
        }
    }

    @Test
    fun swingHandlersFlaggedAsIgnorePrayerShouldNotInfluenceAccuracy() {
        val testRangeHandler = RangeSwingHandler(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY)
        val testMeleeHandler = MeleeSwingHandler(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY)
        val testMagicHandler = MagicSwingHandler(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY)

        TestUtils.getMockPlayer("ignoreprayeraccuracy").use { p ->
            p.skills.setStaticLevel(Skills.PRAYER, 99)
            p.skills.prayerPoints = 1000.0

            p.skills.setLevel(Skills.RANGE, 99)
            val baselineRanged = testRangeHandler.calculateAccuracy(p)
            p.prayer.toggle(PrayerType.EAGLE_EYE)
            Assertions.assertEquals(baselineRanged, testRangeHandler.calculateAccuracy(p))
            p.prayer.toggle(PrayerType.EAGLE_EYE)

            p.skills.setLevel(Skills.ATTACK, 99)
            val baselineMelee = testMeleeHandler.calculateAccuracy(p)
            p.prayer.toggle(PrayerType.INCREDIBLE_REFLEXES)
            Assertions.assertEquals(baselineMelee, testMeleeHandler.calculateAccuracy(p))
            p.prayer.toggle(PrayerType.INCREDIBLE_REFLEXES)

            p.skills.setLevel(Skills.MAGIC, 99)
            val baselineMagic = testMagicHandler.calculateAccuracy(p)
            p.prayer.toggle(PrayerType.MYSTIC_MIGHT)
            Assertions.assertEquals(baselineMagic, testMagicHandler.calculateAccuracy(p))
            p.prayer.toggle(PrayerType.MYSTIC_MIGHT)
        }
    }

    @Test fun chinchompaSwingHandlerIgnoresAmmoSlotForDamage() {
        val handler = ChinchompaSwingHandler()

        TestUtils.getMockPlayer("chinchompaStatTest").use { p ->
            p.skills.staticLevels[Skills.RANGE] = 99
            p.skills.dynamicLevels[Skills.RANGE] = 99
            p.equipment.replace(Item(Items.CHINCHOMPA_10033), EquipmentSlot.WEAPON.ordinal)
            updateBonuses(p)
            val damageBaseline = handler.calculateHit(p, p, 1.0)
            p.equipment.replace(Item(Items.DRAGON_ARROW_11212), EquipmentSlot.AMMO.ordinal)
            updateBonuses(p)
            Assertions.assertEquals(damageBaseline, handler.calculateHit(p, p, 1.0))
        }
    }
}