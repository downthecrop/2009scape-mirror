package rs09.game.content.activity.communityevents

import core.cache.def.impl.ItemDefinition
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.*
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.MapZone
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.game.world.GameWorld.Pulser
import java.util.ArrayList
import core.game.content.global.action.DigAction
import core.game.content.global.action.DigSpadeHandler
import core.game.content.global.action.DigSpadeHandler.dig
import core.game.content.global.action.DigSpadeHandler.register
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.ai.general.scriptrepository.PlayerScripts
import java.util.HashMap


@Initializable
class SuperEggEaster2020

    : MapZone("Clue Zone", true), Plugin<Any?> {
    @Throws(Throwable::class)

    override fun newInstance(arg: Any?): Plugin<Any?>? {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }

    override fun configure() {
        val borders = ZoneBorders(2188, 3281, 2188, 3281)
        //borders.addException(ZoneBorders(2328, 3496, 2328, 3496))
        register(borders)
        //pulse.stop()
    }

    override fun enter(e: Entity?): Boolean {
        val clue_loc = Location.create(2188, 3281, 0)
        val key = Item(Items.KEY_11039)

        if (e is Player) {
            val p = e
            PLAYERS.add(p)
           //if (p.getAttribute("step_1",true)){
                //register(clue_loc) { p.inventory.add(key) }
                //p.setAttribute("step_1",false)
            //}
            clues()
        }
        return true
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            PLAYERS.remove(e)
        }
        return super.leave(e, logout)
    }

    companion object {

        private val PLAYERS: MutableList<Player> = ArrayList(10)

        fun clues(){

            for (player in PLAYERS) {
                val clue_loc = Location.create(2188, 3281, 0)
                val gear_check: Boolean = player.equipment.containsAll(1355)
                val stat_check: Boolean = player.skills.totalLevel >= 720

                //Super Egg!!!
                if (player.location.equals(clue_loc) && player.getAttribute("string_1",true)) {
                    //player.setAttribute("step_1",true)
                    //player.dialogueInterpreter.sendDialogue("Your spade hits metal, you reach into the hole and find a strange key.")
                } else player.dialogueInterpreter.sendDialogue("A magical force prevents you from digging.")
            }

        }
    }
}


