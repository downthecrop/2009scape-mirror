package rs09.game.content.activity.gnomecooking.cocktails

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

private const val UNCOOKED_CHOC_SAT = 9572
private const val UNCOOKED_DRUN_DRA = 9576

/**
 * Handles the cooking of certain cocktail products
 * @author Ceikry
 */
@Initializable
class CocktailCooker : UseWithHandler(UNCOOKED_CHOC_SAT, UNCOOKED_DRUN_DRA) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(17131, OBJECT_TYPE,this)
        addHandler(2728, OBJECT_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        when(event?.used?.id){
            UNCOOKED_CHOC_SAT -> cook(CookedDrinks.COOKED_CHOC_SAT,event.player,event.usedItem)
            UNCOOKED_DRUN_DRA -> cook(CookedDrinks.COOKED_DRUN_DRA,event.player,event.usedItem)
        }
        return true
    }

    private fun cook(drink: CookedDrinks, player: Player, raw: Item){
        GameWorld.Pulser.submit(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> player.lock().also { player.animator.animate(Animation(883)) }
                    2 -> {
                        if(player.inventory.containsItem(raw)) {
                            player.inventory.remove(raw)
                            player.inventory.add(Item(drink.product))
                        }
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }

    internal enum class CookedDrinks(val product: Int){
        COOKED_CHOC_SAT(9573),
        COOKED_DRUN_DRA(2092)
    }

}