package content

import TestUtils
import content.global.handlers.npc.ChromaticDragonBehavior
import core.ServerConstants
import core.api.utils.NPCDropTable
import core.api.utils.WeightedItem
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class DropTableTests {
    init {
        TestUtils.preTestSetup()
    }
    @Test fun chromaticDragonsShouldDropEggsOnlyAfter99Summoning() {
        val npc = NPC.create(NPCs.BLACK_DRAGON_54, ServerConstants.HOME_LOCATION)
        val oldTable = npc.definition.dropTables.table

        val testTable = NPCDropTable() //manufacture a fake table that's guaranteed to give an egg, for testing safety.
        testTable.add(WeightedItem(Items.BLACK_DRAGON_EGG_12480, 1, 1, 1.0))

        npc.definition.dropTables.table = testTable
        npc.behavior = ChromaticDragonBehavior()

        TestUtils.getMockPlayer("chromaticeggdrop").use { p ->
            ChromaticDragonBehavior.EGG_RATE = 1 //guarantee egg drop if other parameters are satisfied
            var hasEgg = false

            var items = npc.definition.dropTables.roll(npc, p, 1)
            for (item in items)
                if (item.id == Items.BLACK_DRAGON_EGG_12480) hasEgg = true
            Assertions.assertEquals(false, hasEgg)

            p.skills.setStaticLevel(Skills.SUMMONING, 99)

            items = npc.definition.dropTables.roll(npc, p, 1)
            for (item in items)
                if (item.id == Items.BLACK_DRAGON_EGG_12480) hasEgg = true
            Assertions.assertEquals(true, hasEgg)
        }

        npc.definition.dropTables.table = oldTable
    }
}