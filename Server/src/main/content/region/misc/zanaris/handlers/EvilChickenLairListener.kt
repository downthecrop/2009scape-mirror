package content.region.misc.zanaris.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class EvilChickenLairListener: InteractionListener {
    override fun defineListeners() {

        addClimbDest(Location.create(2441, 4381, 0), Location.create(2457, 4380, 0))
        addClimbDest(Location.create(2455, 4380, 0), Location.create(2441, 4381, 0))

        onUseWith(IntType.SCENERY, Items.RAW_CHICKEN_2138, Scenery.CHICKEN_SHRINE_12093) { player, _, _ ->
            if (!hasRequirement(player, "Legend's Quest"))
                return@onUseWith false

            if(removeItem(player,(Item(Items.RAW_CHICKEN_2138)))){
                animate(player, Animation(10100))

                submitWorldPulse(object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        teleport(player, Location.create(2461, 4356, 0))
                        animate(player, Animation(9013))
                        return true
                    }
                })
            }
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.EGG_1944, Scenery.CHICKEN_SHRINE_12093) { player, _, _ ->
            sendMessage(player, "Nice idea, but nothing interesting happens.")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.TUNNEL_ENTRANCE_12253) { player, _, node ->
            if(removeItem(player, Item(Items.ROPE_954)))
                replaceScenery(node as core.game.node.scenery.Scenery, Scenery.TUNNEL_ENTRANCE_12254, 100)
            return@onUseWith true
        }

        on(Scenery.PORTAL_12260, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(2453, 4476, 0))
            return@on true
        }

    }
}
