package rs09.game.node.entity.skill.farming

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.node.entity.state.newsys.states.SeedlingState

private val cans = arrayListOf(Items.WATERING_CAN8_5340,Items.WATERING_CAN7_5339,Items.WATERING_CAN6_5338,Items.WATERING_CAN5_5337,Items.WATERING_CAN4_5336,Items.WATERING_CAN3_5335,Items.WATERING_CAN2_5334,Items.WATERING_CAN1_5333)
private val seedlings = arrayListOf(Items.OAK_SEEDLING_5358,Items.WILLOW_SEEDLING_5359,Items.MAPLE_SEEDLING_5360,Items.YEW_SEEDLING_5361,Items.MAGIC_SEEDLING_5362,Items.APPLE_SEEDLING_5480,Items.BANANA_SEEDLING_5481,Items.ORANGE_SEEDLING_5482,Items.CURRY_SEEDLING_5483,Items.PINEAPPLE_SEEDLING_5484,Items.PAPAYA_SEEDLING_5485,Items.PALM_SEEDLING_5486)

@Initializable
class SeedlingWaterer : UseWithHandler(*cans.toIntArray()) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for(seed in seedlings) addHandler(seed, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        val player = event?.player ?: return false
        val seedling = event.used.asItem() ?: return false
        val can = event.usedWith.asItem() ?: return false

        val nextCan = can.id.getNext()
        val wateredSeedling = if(seedling.id > 5400 ) seedling.id + 8 else seedling.id + 6

        if(player.inventory.remove(can) && player.inventory.remove(seedling)){
            player.inventory.add(Item(wateredSeedling))
            player.inventory.add(Item(nextCan))

            var state = player.states["seedling"] as SeedlingState?
            if(state == null){
                state = player.registerState("seedling") as SeedlingState?
                state?.addSeedling(wateredSeedling)
                state?.init()
            } else {
                state.addSeedling(wateredSeedling)
            }

            player.sendMessage("You water the seedling.")
        }
        return true
    }

    private fun Int.getNext(): Int{
        var index = cans.indexOf(this)
        if(index == -1) return Items.WATERING_CAN_5331
        if(index != cans.size -1) return cans[index + 1] else return Items.WATERING_CAN_5331
    }

}