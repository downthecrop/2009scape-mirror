package content.region.kandarin.seers.quest.elementalworkshop

import content.data.Quests
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.BELLOWS_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.FURNACE_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.LEFT_WATER_CONTROL_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.RIGHT_WATER_CONTROL_STATE
import content.region.kandarin.seers.quest.elementalworkshop.EWUtils.WATER_WHEEL_STATE
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin

/**
 * This updates the Elemental Workshop I state whenever the player enters, if the quest is complete.
 * Workshop should maintain state per this video: https://www.youtube.com/watch?v=YKSWisRgGZk
 */

@Initializable
class EWZone : MapZone("Elemental Workshop I", true), Plugin<Any?> {

    override fun newInstance(arg: Any?): EWZone {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        super.register(ZoneBorders(2688,9862,2744,9920))
    }

    // updates the workshop state based on the varbits for Elemental Workshop II
    override fun enter(e: Entity?): Boolean {
        if(e is Player) {
            if(isQuestComplete(e, Quests.ELEMENTAL_WORKSHOP_I)) {
                setVarbit(e, LEFT_WATER_CONTROL_STATE, 1)
                setVarbit(e, RIGHT_WATER_CONTROL_STATE, 1)
                setVarbit(e, WATER_WHEEL_STATE, 1)
                setVarbit(e, FURNACE_STATE, 1)
                setVarbit(e, BELLOWS_STATE, 1)
            }
        }
        return super.enter(e)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }
}