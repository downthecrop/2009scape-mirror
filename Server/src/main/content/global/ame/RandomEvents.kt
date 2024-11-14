package content.global.ame

import org.rs09.consts.Items
import content.global.ame.events.MysteriousOldManNPC
import content.global.ame.events.certer.CerterNPC
import content.global.ame.events.drilldemon.SeargentDamienNPC
import content.global.ame.events.drunkendwarf.DrunkenDwarfNPC
import content.global.ame.events.evilbob.EvilBobNPC
import content.global.ame.events.evilchicken.EvilChickenNPC
import content.global.ame.events.freakyforester.FreakyForesterNPC
import content.global.ame.events.genie.GenieNPC
import content.global.ame.events.pillory.PilloryNPC
import content.global.ame.events.rickturpentine.RickTurpentineNPC
import content.global.ame.events.rivertroll.RiverTrollRENPC
import content.global.ame.events.rockgolem.RockGolemRENPC
import content.global.ame.events.sandwichlady.SandwichLadyRENPC
import content.global.ame.events.shade.ShadeRENPC
import content.global.ame.events.strangeplant.StrangePlantNPC
import content.global.ame.events.swarm.SwarmNPC
import content.global.ame.events.treespirit.TreeSpiritRENPC
import content.global.ame.events.zombie.ZombieRENPC

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.node.entity.skill.Skills

enum class RandomEvents(val npc: RandomEventNPC, val loot: WeightBasedTable? = null, val skillIds: IntArray = intArrayOf(), val type: String = "") {
    SANDWICH_LADY(npc = SandwichLadyRENPC()),
    GENIE(npc = GenieNPC()),
    CERTER(npc = CerterNPC(), loot = WeightBasedTable.create(
        WeightedItem(Items.UNCUT_SAPPHIRE_1623,1,1,3.4),
        WeightedItem(Items.KEBAB_1971,1,1,1.7),
        WeightedItem(Items.UNCUT_EMERALD_1621,1,1,1.7),
        WeightedItem(Items.SPINACH_ROLL_1969,1,1,1.5),
        WeightedItem(Items.COINS_995,80,80,1.1),
        WeightedItem(Items.COINS_995,160,160,1.1),
        WeightedItem(Items.COINS_995,320,320,1.1),
        WeightedItem(Items.COINS_995,480,480,1.1),
        WeightedItem(Items.COINS_995,640,640,1.1),
        WeightedItem(Items.UNCUT_RUBY_1619,1,1,0.86),
        WeightedItem(Items.COINS_995,240,240,0.65),
        WeightedItem(Items.COSMIC_TALISMAN_1454,1,1,0.43),
        WeightedItem(Items.UNCUT_DIAMOND_1617,1,1,0.22),
        WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985,1,1,0.1),
        WeightedItem(Items.LOOP_HALF_OF_A_KEY_987,1,1,0.1)
    )),
    DRILL_DEMON(npc = SeargentDamienNPC()),
    EVIL_CHICKEN(npc = EvilChickenNPC()),
    STRANGE_PLANT(npc = StrangePlantNPC()),
    SWARM(npc = SwarmNPC()),
    EVIL_BOB(npc = EvilBobNPC(), skillIds = intArrayOf(Skills.FISHING, Skills.MAGIC)),
    DRUNKEN_DWARF(npc = DrunkenDwarfNPC()),
    RICK_TURPENTINE(npc = RickTurpentineNPC(), loot = CERTER.loot),
    SURPRISE_EXAM(npc = MysteriousOldManNPC(), type = "sexam"),
    FREAKY_FORESTER(npc = FreakyForesterNPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    PILLORY(npc = PilloryNPC(), skillIds = intArrayOf(Skills.THIEVING)),
    TREE_SPIRIT(npc = TreeSpiritRENPC(), skillIds = intArrayOf(Skills.WOODCUTTING)),
    RIVER_TROLL(RiverTrollRENPC(), skillIds = intArrayOf(Skills.FISHING)),
    ROCK_GOLEM(RockGolemRENPC(), skillIds = intArrayOf(Skills.MINING)),
    SHADE(ShadeRENPC(), skillIds = intArrayOf(Skills.PRAYER)),
    ZOMBIE(ZombieRENPC(), skillIds = intArrayOf(Skills.PRAYER));

    companion object {
        @JvmField
        val randomIDs = values().map { it.npc.id }.toList()
        val skillMap = HashMap<Int, ArrayList<RandomEvents>>()
        val nonSkillList = ArrayList<RandomEvents>()

        init { populateMappings() }

        fun getSkillBasedRandomEvent (skill: Int) : RandomEvents? {
            return skillMap[skill]?.random()
        }

        fun getNonSkillRandom() : RandomEvents {
            return nonSkillList.random()
        }

        private fun populateMappings() {
            for (event in values()) {
                for (id in event.skillIds) {
                    val list = skillMap[id] ?: ArrayList<RandomEvents>().also { skillMap[id] = it }
                    list.add (event)
                }
                if (event.skillIds.isEmpty())
                    nonSkillList.add (event)
            }
        }
    }

}
