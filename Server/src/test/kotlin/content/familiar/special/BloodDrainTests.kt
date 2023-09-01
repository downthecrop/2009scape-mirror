package content.familiar.special

import TestUtils
import content.global.skill.summoning.familiar.BloatedLeechNPC
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.ServerConstants
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.system.timer.TimerRegistry
import core.game.system.timer.impl.Disease
import core.game.system.timer.impl.Poison
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class BloodDrainTests {
    init {
        TestUtils.preTestSetup()
    }
    @Test fun bloodDrainShouldNotRestorePrayer() {
        TestUtils.getMockPlayer("bloodDrainPrayer").use { p ->
            p.skills.setStaticLevel(Skills.PRAYER, 99)
            p.skills.prayerPoints = 5.0
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)

            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION
            Assertions.assertEquals(true, npc.executeSpecialMove(FamiliarSpecial(p)))
            Assertions.assertEquals(5.0, p.skills.prayerPoints)
        }
    }

    @Test fun bloodDrainShouldDamageOwner() {
        TestUtils.getMockPlayer("bloodDrainHealth").use {p ->
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)
            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION

            val npcBaseline = npc.skills.lifepoints
            val ownerBaseline = p.skills.lifepoints

            Assertions.assertEquals(true, npc.executeSpecialMove(FamiliarSpecial(p)))
            TestUtils.advanceTicks(1, false)

            Assertions.assertEquals(npcBaseline, npc.skills.lifepoints)
            Assertions.assertEquals(true, p.skills.lifepoints < ownerBaseline)
        }
    }

    @Test fun bloodDrainShouldNotHeal() {
        TestUtils.getMockPlayer("bloodDrainHealth2").use {p ->
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)
            p.skills.lifepoints = 5

            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION

            val ownerBaseline = p.skills.lifepoints

            Assertions.assertEquals(true, npc.executeSpecialMove(FamiliarSpecial(p)))
            TestUtils.advanceTicks(1, false)

            Assertions.assertEquals(true, p.skills.lifepoints < ownerBaseline)
        }
    }

    @Test fun bloodDrainShouldOnlyRestorePercentOfStats() {
        TestUtils.getMockPlayer("bloodDrainStats").use { p ->
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)
            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION

            p.skills.setStaticLevel(Skills.STRENGTH, 99)
            p.skills.setStaticLevel(Skills.FARMING, 99)
            p.skills.setLevel(Skills.STRENGTH, 1)
            p.skills.setLevel(Skills.FARMING, 1)

            Assertions.assertEquals(true,npc.executeSpecialMove(FamiliarSpecial(p)))
            Assertions.assertEquals(21, p.skills.getLevel(Skills.STRENGTH))
            Assertions.assertEquals(21, p.skills.getLevel(Skills.FARMING))
        }
    }

    @Test fun bloodDrainShouldNotBoostStats() {
        TestUtils.getMockPlayer("bloodDrainStats2").use { p ->
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)
            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION

            p.skills.setStaticLevel(Skills.STRENGTH, 99)
            p.skills.setStaticLevel(Skills.FARMING, 99)
            p.skills.setLevel(Skills.STRENGTH, 98)
            p.skills.setLevel(Skills.FARMING, 98)

            Assertions.assertEquals(true,npc.executeSpecialMove(FamiliarSpecial(p)))
            Assertions.assertEquals(99, p.skills.getLevel(Skills.STRENGTH))
            Assertions.assertEquals(99, p.skills.getLevel(Skills.FARMING))
        }
    }

    @Test fun bloodDrainCuresPoisonAndDisease() {
        TestUtils.getMockPlayer("bloodDrainAilments").use { p ->
            addItem(p, Items.BLOOD_DRAIN_SCROLL_12444)
            val npc = BloatedLeechNPC(p, NPCs.BLOATED_LEECH_6843)
            npc.location = ServerConstants.HOME_LOCATION

            applyPoison(p, p, 40)
            Assertions.assertNotNull(getOrStartTimer<Disease>(p, 40))
            Assertions.assertEquals(true, hasTimerActive<Poison>(p))
            Assertions.assertEquals(true, hasTimerActive<Disease>(p))
            Assertions.assertEquals(true, npc.executeSpecialMove(FamiliarSpecial(p)))
            Assertions.assertEquals(false, hasTimerActive<Poison>(p))
            Assertions.assertEquals(false, hasTimerActive<Disease>(p))
        }
    }
}
