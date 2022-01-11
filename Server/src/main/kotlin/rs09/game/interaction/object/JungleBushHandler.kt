package rs09.game.interaction.`object`

import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld

/**
 * Handles the chopping down of dense jungle, mainly to grant access to the Kharazi Jungle.
 * @author Ceikry
 */
class JungleBushHandler : InteractionListener(){
    val chopped_bush = 2895
    val chop_a  = Animation(910)
    val chop_b  = Animation(2382)
    val ids = intArrayOf(2892,2893)

    override fun defineListeners() {

        on(ids,SCENERY,"chop-down"){ player, node ->
            val toChop = node.asScenery()
            if(checkRequirement(player)){
                GameWorld.Pulser.submit(object : Pulse(0){
                    var ticks = 0
                    override fun pulse(): Boolean {
                        when(ticks++){
                            0 -> player.animator.animate(chop_a).also { player.lock() }
                            1 -> player.animator.animate(chop_b)
                            2 -> SceneryBuilder.replace(toChop, Scenery(chopped_bush, toChop.location, toChop.rotation),20)
                            3 -> {player.walkingQueue.reset(); player.walkingQueue.addPath(toChop.location.x, toChop.location.y,true)}
                            4 -> player.unlock().also { return true }
                        }
                        return false
                    }
                })
            } else {
                player.sendMessage("You need a machete to get through this dense jungle.")
            }
            return@on true
        }

    }

    fun checkRequirement(player: Player): Boolean{
        val machete = Item(Items.MACHETE_975)
        val jade_machete = Item(Items.JADE_MACHETE_6315)
        val opal_machete = Item(Items.OPAL_MACHETE_6313)
        val red_topaz_machete = Item(Items.RED_TOPAZ_MACHETE_6317)
        return player.inventory.containsItem(machete) || player.inventory.containsItem(jade_machete) || player.inventory.containsItem(opal_machete) || player.inventory.containsItem(red_topaz_machete)
    }
}
