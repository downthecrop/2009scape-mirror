package content.global.ame.events.freakyforester

import core.api.*
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.system.task.Pulse
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs09.consts.Items

class FreakListeners : InteractionListener, MapArea {

    private val exitPortal = Scenery.PORTAL_8972
    private val freakNpc = NPCs.FREAKY_FORESTER_2458
    private val pheasants = intArrayOf(NPCs.PHEASANT_2459,NPCs.PHEASANT_2460,NPCs.PHEASANT_2461,NPCs.PHEASANT_2462)
    override fun defineListeners() {
        on(freakNpc,IntType.NPC,"talk-to") { player, node ->
            if (inBorders(player, FreakUtils.freakArea)) {
                if (getAttribute(player, FreakUtils.freakTask, -1) == -1) FreakUtils.giveFreakTask(player)
                openDialogue(player, FreakyForesterDialogue(), node.asNpc())
            } else {
                sendMessage(player, "They aren't interested in talking to you.")
            }
            return@on true
        }

        on(pheasants,IntType.NPC,"attack") { player, node ->
            if (getAttribute(player,FreakUtils.freakComplete, false)) {
                sendMessage(player,"You don't need to attack any more pheasants. You're allowed to leave.")
            } else if (inInventory(player, Items.RAW_PHEASANT_6178) || inInventory(player, Items.RAW_PHEASANT_6179) || getAttribute(player, FreakUtils.pheasantKilled, false)){
                sendMessage(player,"You don't need to attack any more pheasants.")
            } else {
                player.attack(node.asNpc())
            }
            return@on true
        }

        on(exitPortal,IntType.SCENERY,"enter") { player, _ ->
            if (getAttribute(player,FreakUtils.freakComplete,false)) {
                FreakUtils.cleanup(player)
                submitWorldPulse(object : Pulse(2) {
                    override fun pulse(): Boolean {
                        FreakUtils.reward(player)
                        return true
                    }
                })
                return@on true
            } else sendMessage(player,"A supernatural force prevents you from leaving.")
            return@on true
        }
    }
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(FreakUtils.freakArea)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.OFF_MAP)
    }

    override fun areaEnter(entity: Entity) {
        entity.locks.lockTeleport(1000000)
    }
}
