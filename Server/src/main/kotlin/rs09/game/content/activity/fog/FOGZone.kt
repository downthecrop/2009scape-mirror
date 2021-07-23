package rs09.game.content.activity.fog

import api.ContentAPI
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
class FOGZone : MapZone("Fist of Guthix", true, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FIRES), Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        ContentAPI.submitWorldPulse(pulse)
        return this
    }

    override fun configure() {
        registerRegion(6488)
        registerRegion(6489)
        registerRegion(6745)
        registerRegion(6744)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun enter(e: Entity?): Boolean {
        if(e is Player){
            ContentAPI.openOverlay(e, 730)
        }
        return super.enter(e)
    }

    override fun leave(e: Entity?, logout: Boolean): Boolean {
        if(e is Player) {
            ContentAPI.closeOverlay(e)
        }
        return super.leave(e, logout)
    }

    val pulse = object : Pulse(){
        override fun pulse(): Boolean {
            for(session in FOGWaitingArea.activeSessions){
                session.tick()
            }
            return false
        }
    }

    override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
        if(e !is Player) return false
        target ?: return false

        when(option?.name?.toLowerCase()){
            "take-stone" -> {
                if(FOGUtils.getSession(e)?.hunted != e){
                    ContentAPI.sendMessage(e, "You can't take that right now.")
                    return true
                }

                if(FOGUtils.carryingStone(e) || ContentAPI.inInventory(e, Items.STONE_OF_POWER_12845)){
                    ContentAPI.sendMessage(e, "You already have a stone.")
                    return true
                }

                ContentAPI.addItem(e, Items.STONE_OF_POWER_12845)
                return true
            }

            "pass" -> {
                ContentAPI.submitIndividualPulse(e, object : Pulse(){
                    var counter = 0
                    val stepDir = if(!FOGUtils.isInHouse(e)) {
                        when (target.asScenery().rotation) {
                            0 -> Direction.WEST
                            1 -> Direction.NORTH
                            2 -> Direction.EAST
                            3 -> Direction.SOUTH
                            else -> target.direction
                        }
                    } else {
                        when (target.asScenery().rotation) {
                            0 -> Direction.EAST
                            1 -> Direction.SOUTH
                            2 -> Direction.WEST
                            3 -> Direction.NORTH
                            else -> target.direction
                        }
                    }
                    override fun pulse(): Boolean {
                        when(counter++){
                            0 -> {
                                ContentAPI.lockInteractions(e,5)
                                ContentAPI.forceWalk(e, target.location, "dumb")
                                ContentAPI.face(e, target.location.transform(stepDir))
                            }
                            1 -> {
                                ContentAPI.forceWalkStep(e, 1, stepDir)
                            }
                            2 -> e.unlock().also { return true }
                        }
                        return false
                    }
                })
                return true
            }

        }

        return super.interact(e, target, option)
    }


}