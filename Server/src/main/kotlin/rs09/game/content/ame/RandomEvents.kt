package rs09.game.content.ame

import org.rs09.consts.Items
import rs09.game.content.ame.events.MysteriousOldManNPC
import rs09.game.content.ame.events.certer.CerterNPC
import rs09.game.content.ame.events.drilldemon.SeargentDamienNPC
import rs09.game.content.ame.events.evilchicken.EvilChickenNPC
import rs09.game.content.ame.events.genie.GenieNPC
import rs09.game.content.ame.events.rivertroll.RiverTrollRENPC
import rs09.game.content.ame.events.rockgolem.RockGolemRENPC
import rs09.game.content.ame.events.sandwichlady.SandwichLadyRENPC
import rs09.game.content.ame.events.shade.ShadeRENPC
import rs09.game.content.ame.events.treespirit.TreeSpiritRENPC
import rs09.game.content.ame.events.zombie.ZombieRENPC

import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem

enum class RandomEvents(val npc: RandomEventNPC, val loot: WeightBasedTable? = null) {
    SANDWICH_LADY(SandwichLadyRENPC()),
	GENIE(GenieNPC()),
    CERTER(CerterNPC(),WeightBasedTable.create(
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
    DRILL_DEMON(SeargentDamienNPC()),
    EVIL_CHICKEN(EvilChickenNPC()),
    SURPRISE_EXAM(MysteriousOldManNPC(),"sexam"),
    TREE_SPIRIT(TreeSpiritRENPC(),"skill"),
    RIVER_TROLL(RiverTrollRENPC(),"skill"),
    ROCK_GOLEM(RockGolemRENPC(),"skill"),
    SHADE(ShadeRENPC(),"skill"),
    ZOMBIE(ZombieRENPC(),"skill");

    var type: String = ""
    private set

    constructor(npc: RandomEventNPC, type: String) : this(npc,null){
        this.type = type
    }

    companion object {
        @JvmField
        val randomIDs = values().map { it.npc.id }.toList()
    }

}