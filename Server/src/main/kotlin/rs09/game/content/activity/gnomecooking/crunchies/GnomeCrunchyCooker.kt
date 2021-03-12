package rs09.game.content.activity.gnomecooking.crunchies

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.node.entity.skill.Skills

private const val UNFINISHED_CRUNCHY_CHOC = 9578
private const val UNFINISHED_CRUNCHY_SPICY = 9580
private const val UNFINISHED_CRUNCHY_TOAD = 9582
private const val UNFINISHED_CRUNCHY_WORM = 9584

/**
 * handles cooking gnome crunchies, don't wanna use standard
 * cooking stuff because no experience and no burn chance
 * @author Ceikry
 */

@Initializable
class GnomeCrunchyCooker : UseWithHandler(9577,9579,9581,9583, 2202){
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(17131, OBJECT_TYPE,this)
        addHandler(2728, OBJECT_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val used = event.used.asItem()
        val with = event.usedWith
        val player = event.player
        when(used.id){
            9577 -> cook(UNFINISHED_CRUNCHY_CHOC,used,player)
            9579 -> cook(UNFINISHED_CRUNCHY_SPICY,used,player)
            9581 -> cook(UNFINISHED_CRUNCHY_TOAD,used,player)
            9583 -> cook(UNFINISHED_CRUNCHY_WORM,used,player)
            2202 -> cook(2201,used,player)
        }
        return true
    }

    private fun cook(product: Int, raw: Item,  player: Player){
        GameWorld.Pulser.submit(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> player.lock().also { player.animator.animate(Animation(883)) }
                    2 -> {
                        if(player.inventory.containsItem(raw)) {
                            player.inventory.remove(raw)
                            player.inventory.add(Item(product))
                            if(raw.id != 2202) player.inventory.add(Item(2165))
                        }
                        if(raw.id == 2202){
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