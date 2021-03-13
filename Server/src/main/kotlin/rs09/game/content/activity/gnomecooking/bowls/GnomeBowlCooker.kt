package rs09.game.content.activity.gnomecooking.bowls

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

@Initializable
class GnomeBowlCooker : UseWithHandler(Items.RAW_GNOMEBOWL_2178,9558,9559,9561,9563) {
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
            Items.RAW_GNOMEBOWL_2178 -> Item(Items.HALF_BAKED_BOWL_2177)
            9558 -> Item(9560)
            9559 -> Item(Items.TANGLED_TOADS_LEGS_2187)
            9561 -> Item(9562)
            9563 -> Item(9564)
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
                            if(product.id > 2180){
                                player.inventory.add(Item(Items.GNOMEBOWL_MOULD_2166))
                            }
                            player.inventory.remove(raw)
                            player.inventory.add(product)
                            player.skills.addExperience(Skills.COOKING,30.0)
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