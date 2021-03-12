package core.game.interaction.item.withobject

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.plugin.Plugin
import core.plugin.PluginManager.definePlugin
import core.tools.Items
import java.util.*

/**
 * Handles coal truck interactions
 * @author ceik
 */
@Initializable
class CoalTrucksHandler : OptionHandler() {

    val seersVillageTrucks = ZoneBorders(2690,3502,2699,3508)

    override fun newInstance(arg: Any?): Plugin<Any>? {
        definePlugin(useCoalWithTruck())
        val def = ObjectDefinition.forId(2114)
        def.handlers["option:remove-coal"] = this
        def.handlers["option:investigate"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        var coalInTruck = player.getAttribute("coal-truck-inventory", 0)
        var freeSpace = player.inventory.freeSlots()
        val toAdd: MutableList<Item> = ArrayList()
        when (option) {
            "remove-coal" -> {
                if (coalInTruck == 0) {
                    player.dialogueInterpreter.sendDialogue("The coal truck is empty.")
                    return true
                }
                var toRemove = player.inventory.freeSlots()

                if(toRemove > coalInTruck) toRemove = coalInTruck

                player.inventory.add(Item(Items.COAL_453,toRemove))

                coalInTruck -= toRemove
                player.setAttribute("/save:coal-truck-inventory", coalInTruck)


                if (!player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE, 1, 2)
                        && seersVillageTrucks.insideBorder(player)
                        && player.getAttribute("diary:seers:coal-truck-full", false)) {
                    player.removeAttribute("diary:seers:coal-truck-full")
                    player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 1, 2)
                }
            }
            "investigate" -> player.dialogueInterpreter.sendDialogue("There is currently $coalInTruck coal in the truck.", "The truck has space for " + (120 - coalInTruck) + " more coal.")
        }
        return true
    }

    inner class useCoalWithTruck : UseWithHandler(Items.COAL_453) {
        override fun newInstance(arg: Any?): Plugin<Any>? {
            addHandler(2114, OBJECT_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            var coalInTruck = player.getAttribute("coal-truck-inventory", 0)

            var coalInInventory = player.inventory.getAmount(Items.COAL_453)

            if(coalInInventory + coalInTruck >= 120){
                coalInInventory = 120 - coalInTruck
                event.player.packetDispatch.sendMessage("You have filled up the coal truck.")

                //handle coal truck task for seer's village
                if (!player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE).isComplete(1, 2)
                        && player.viewport.region.id == 10294) { // region 10294 is at coal truck mine, region 10806 is in seers village
                    player.setAttribute("/save:diary:seers:coal-truck-full", true)
                }
            }

            player.inventory.remove(Item(Items.COAL_453,coalInInventory))
            coalInTruck += coalInInventory

            player.setAttribute("/save:coal-truck-inventory",coalInTruck)
            return true
        }
    }
}