package rs09.game.node.entity.skill.farming

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
class SeedOnPlantPot : UseWithHandler(Items.ACORN_5312,
    Items.WILLOW_SEED_5313,Items.MAPLE_SEED_5314,Items.YEW_SEED_5315,Items.MAGIC_SEED_5316,Items.APPLE_TREE_SEED_5283,Items.BANANA_TREE_SEED_5284,Items.ORANGE_TREE_SEED_5285,Items.CURRY_TREE_SEED_5286,Items.PINEAPPLE_SEED_5287,Items.PAPAYA_TREE_SEED_5288,Items.PALM_TREE_SEED_5289) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.PLANT_POT_5354, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        val player = event?.player ?: return false
        val pot = event.usedItem ?: return false
        val seed = event.usedWith.asItem() ?: return false

        if(!player.inventory.contains(Items.GARDENING_TROWEL_5325,1)){
            player.dialogueInterpreter.sendDialogue("You need a gardening trowel on you to do this.")
            return false
        }

        val seedling = when(seed.id){
            Items.ACORN_5312 -> Items.OAK_SEEDLING_5358
            Items.WILLOW_SEED_5313 -> Items.WILLOW_SEEDLING_5359
            Items.MAPLE_SEED_5314 -> Items.MAPLE_SEEDLING_5360
            Items.YEW_SEED_5315 -> Items.YEW_SEEDLING_5361
            Items.MAGIC_SEED_5316 -> Items.MAGIC_SEEDLING_5362
            Items.APPLE_TREE_SEED_5283 -> Items.APPLE_SEEDLING_5480
            Items.BANANA_TREE_SEED_5284 -> Items.BANANA_SEEDLING_5481
            Items.ORANGE_TREE_SEED_5285 -> Items.ORANGE_SEEDLING_5482
            Items.CURRY_TREE_SEED_5286 -> Items.CURRY_SEEDLING_5483
            Items.PINEAPPLE_SEED_5287 -> Items.PINEAPPLE_SEEDLING_5484
            Items.PAPAYA_TREE_SEED_5288 -> Items.PAPAYA_SEEDLING_5485
            Items.PALM_TREE_SEED_5289 -> Items.PALM_SEEDLING_5486
            else -> return false
        }

        if(player.inventory.remove(pot) && player.inventory.remove(Item(seed.id,1))){
            player.inventory.add(Item(seedling))
        }

        return true
    }
}