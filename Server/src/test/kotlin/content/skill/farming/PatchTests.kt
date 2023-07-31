package content.skill.farming

import TestUtils
import content.global.skill.farming.FarmingPatch
import content.global.skill.farming.Plantable
import content.global.skill.farming.timers.CropGrowth
import core.cache.def.impl.SceneryDefinition
import core.game.node.entity.skill.Skills
import core.game.system.timer.TimerRegistry
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PatchTests {
    init { TestUtils.preTestSetup() }
    @Test fun patchGettingDiseasedOrDyingShouldNotGoIntoInvalidState() {
        TimerRegistry.registerTimer(CropGrowth())
        //Dead/Watered (state | 0x40) and Diseased (state | 0x80) are both invalid stages for limpwurt when fully grown (stage 4/state 32), so we use this as our testcase
        //specifically, limpwurt has an issue when the patch is ONLY watered (state | 0x40) OR diseased (state | 0x80), having both (state | 0x40 | 0x80) is fine.
        TestUtils.getMockPlayer("invalidPatchState").use {p ->
            p.skills.setStaticLevel(Skills.FARMING, 99)
            p.skills.setLevel(Skills.FARMING, 99)

            val patch = FarmingPatch.S_FALADOR_FLOWER_C.getPatchFor(p)
            patch.plant(Plantable.LIMPWURT_SEED)

            for (i in 0 until 4) { //grow to full growth while making sure we don't die/get diseased
                patch.currentGrowthStage = 4
                patch.update()
            }

            Assertions.assertEquals(32, patch.getCurrentState())

            patch.isWatered = true
            patch.update()
            Assertions.assertEquals(false, patch.isDiseased)
            Assertions.assertEquals(false, patch.isDead)
            Assertions.assertEquals(false, patch.isWatered)
            Assertions.assertEquals(32, patch.getCurrentState())

            patch.isDiseased = true
            patch.updateBit()
            Assertions.assertEquals(false, patch.isDiseased)
            Assertions.assertEquals(false, patch.isDead)
            Assertions.assertEquals(false, patch.isWatered)
            Assertions.assertEquals(32, patch.getCurrentState())
        }
    }
}