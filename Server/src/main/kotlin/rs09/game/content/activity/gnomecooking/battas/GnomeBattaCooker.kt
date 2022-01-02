package rs09.game.content.activity.gnomecooking.battas

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.world.GameWorld

/**
 * Handles cook options for battas
 * @author Ceikry
 */
@Initializable
class GnomeBattaCooker : UseWithHandler(Items.RAW_BATTA_2250,9478,9480,9482,9483,9485) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(17131, OBJECT_TYPE,this)
        addHandler(2728, OBJECT_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val used = event.used
        val product = when(used.id){
            Items.RAW_BATTA_2250 -> Item(Items.HALF_BAKED_BATTA_2249)
            9478 -> Item(9479)
            9480 -> Item(9481)
            9483 -> Item(9484)
            9485 -> Item(9486)
            9482 -> Item(2255)
            else -> Item(0)
        }
        if(product.id == 0) return false
        cook(player,used.asItem(),product)
        return true
    }

    fun cook(player: Player, raw: Item, product: Item){
        GameWorld.Pulser.submit(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> player.lock().also { player.animator.animate(Animation(883)) }
                    2 -> {
                        if(player.inventory.containsItem(raw)) {
                            player.inventory.remove(raw)
                            player.inventory.add(product)
                            if(product.id == 2255) player.skills.addExperience(Skills.COOKING,82.0)
                            else player.skills.addExperience(Skills.COOKING,40.0)
                        }
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }
}