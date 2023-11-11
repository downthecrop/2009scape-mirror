import content.global.skill.runecrafting.RuneCraftPulse;
import content.global.skill.runecrafting.Rune;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RunecraftTests {
    fun rollRc(rcLevel: Int, rune: Rune, revision: Int, lo: Double, hi: Double) {
        var total = 0.0
        for(i in 0 until 3000) {
            total += RuneCraftPulse.getMultiplier(rcLevel, rune, revision, false)
        }
        val average = total / 3000.0
        Assertions.assertTrue(lo <= average && average <= hi, "rollRc: ${rcLevel} ${rune.name} ${revision}: ${average}")
    }

    @Test fun testNatureRcMultipliers() {
        rollRc(44, Rune.NATURE, 530, 1.0, 1.0);
        rollRc(44, Rune.NATURE, 573, 1.0, 1.0);
        rollRc(44, Rune.NATURE, 581, 1.0, 1.0);

        rollRc(68, Rune.NATURE, 530, 1.0, 1.0);
        rollRc(68, Rune.NATURE, 573, 1.45, 1.55);
        rollRc(68, Rune.NATURE, 581, 1.45, 1.55);

        rollRc(91, Rune.NATURE, 530, 2.0, 2.0);
        rollRc(91, Rune.NATURE, 573, 2.0, 2.0);
        rollRc(91, Rune.NATURE, 581, 2.0, 2.0);

        rollRc(90, Rune.NATURE, 530, 1.0, 1.0);
        rollRc(90, Rune.NATURE, 573, 1.95, 2.0);
        rollRc(90, Rune.NATURE, 581, 1.95, 2.0);

        rollRc(99, Rune.NATURE, 530, 2.0, 2.0);
        rollRc(99, Rune.NATURE, 573, 2.0, 2.0);
        rollRc(99, Rune.NATURE, 581, 2.04, 2.14);
    }
    @Test fun testLawRcMultipliers() {
        rollRc(54, Rune.LAW, 530, 1.0, 1.0);
        rollRc(54, Rune.LAW, 573, 1.0, 1.0);
        rollRc(54, Rune.LAW, 581, 1.0, 1.0);

        rollRc(77, Rune.LAW, 530, 1.0, 1.0);
        rollRc(77, Rune.LAW, 573, 1.0, 1.0);
        rollRc(77, Rune.LAW, 581, 1.35, 1.45);

        rollRc(99, Rune.LAW, 530, 1.0, 1.0);
        rollRc(99, Rune.LAW, 573, 1.0, 1.0);
        rollRc(99, Rune.LAW, 581, 1.75, 1.85);
    }
}
