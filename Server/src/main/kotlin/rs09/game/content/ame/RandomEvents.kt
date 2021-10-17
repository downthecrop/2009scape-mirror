package rs09.game.content.ame

import org.rs09.consts.Items
import rs09.game.content.ame.events.MysteriousOldManNPC
import rs09.game.content.ame.events.certer.CerterNPC
import rs09.game.content.ame.events.drilldemon.SeargentDamienNPC
import rs09.game.content.ame.events.evilchicken.EvilChickenNPC
import rs09.game.content.ame.events.sandwichlady.SandwichLadyRENPC
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem

enum class RandomEvents(val npc: RandomEventNPC, val loot: WeightBasedTable? = null) {
    SANDWICH_LADY(SandwichLadyRENPC()),
    CERTER(CerterNPC(),WeightBasedTable.create(
        WeightedItem(Items.COINS_995,1,100,2.0),
        WeightedItem(Items.SPINACH_ROLL_1969,1,1,2.0),
        WeightedItem(Items.KEBAB_1971,1,1,2.0),
        WeightedItem(Items.UNCUT_SAPPHIRE_1623,1,1,0.5),
        WeightedItem(Items.SAPPHIRE_1607,1,1,0.5),
        WeightedItem(Items.UNCUT_EMERALD_1621,1,1,0.25),
        WeightedItem(Items.EMERALD_1605,1,1,0.25),
        WeightedItem(Items.UNCUT_RUBY_1619,1,1,0.15),
        WeightedItem(Items.RUBY_1603,1,1,0.15),
        WeightedItem(Items.DIAMOND_1601,1,1,0.1),
        WeightedItem(Items.UNCUT_DIAMOND_1617,1,1,0.1),
        WeightedItem(Items.COSMIC_TALISMAN_1454,1,1,0.15),
        WeightedItem(Items.NATURE_TALISMAN_1462,1,1,0.15),
        WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985,1,1,1.0),
        WeightedItem(Items.LOOP_HALF_OF_A_KEY_987,1,1,1.0)
    )),
    DRILL_DEMON(SeargentDamienNPC()),
    EVIL_CHICKEN(EvilChickenNPC()),
    SURPRISE_EXAM(MysteriousOldManNPC(),"sexam");

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