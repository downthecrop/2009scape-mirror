package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import org.rs09.consts.Scenery as SceneryConst
import org.rs09.consts.Items
import core.game.node.scenery.Scenery
import core.tools.secondsToTicks


class StoveInteraction : InteractionListener {

    // Imagine if these indexes incremented logically...
    companion object{
        val valid_stoves = intArrayOf(
            SceneryConst.FIREPIT_WITH_HOOK_13529,
            SceneryConst.FIREPIT_WITH_POT_13531,
            SceneryConst.SMALL_OVEN_13533,
            SceneryConst.LARGE_OVEN_13536,
            SceneryConst.STEEL_RANGE_13539,
            SceneryConst.FANCY_RANGE_13542
        )
        val in_progress_stoves = intArrayOf(
            SceneryConst.FIREPIT_WITH_HOOK_13530,
            SceneryConst.FIREPIT_WITH_POT_13532,
            SceneryConst.SMALL_OVEN_13534,
            SceneryConst.LARGE_OVEN_13537,
            SceneryConst.STEEL_RANGE_13541,
            SceneryConst.FANCY_RANGE_13544

        )
        val stoves_with_kettle = intArrayOf(
            SceneryConst.FIREPIT_WITH_HOOK_13530,
            SceneryConst.FIREPIT_WITH_POT_13532,
            SceneryConst.SMALL_OVEN_13535,
            SceneryConst.LARGE_OVEN_13538,
            SceneryConst.STEEL_RANGE_13540,
            SceneryConst.FANCY_RANGE_13543

        )
        const val kettleBoiled = "kettle_boiled"
    }
    override fun defineListeners() {
        onUseWith(SCENERY, Items.FULL_KETTLE_7690, *valid_stoves){ player, used, with ->
            val idx = valid_stoves.indexOf(with.id)
            if (removeItem(player, used)){
                replaceScenery(with as Scenery, in_progress_stoves[idx], -1)
                sendMessage(player, "You place the kettle over the fire.")
                setAttribute(player, kettleBoiled, 1)
                queueScript(player, secondsToTicks(10), QueueStrength.SOFT){ _ ->
                    sendMessage(player, "The kettle boils.")
                    // Of course they changed how these items work so we need 2 different cases
                    setAttribute(player, kettleBoiled, 2)
                    if (with.id in intArrayOf(SceneryConst.FIREPIT_WITH_HOOK_13529, SceneryConst.FIREPIT_WITH_POT_13531))
                        return@queueScript stopExecuting(player)
                    else{
                        getScenery(with.location)?.let { replaceScenery(it,  stoves_with_kettle[idx], -1) }
                        return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }


        on(stoves_with_kettle, SCENERY, "take-kettle"){ player, node ->
            if (getAttribute(player, kettleBoiled, 0) == 0){
                sendMessage(player, "This is not your kettle.")
                return@on false
            }
            else if (getAttribute(player, kettleBoiled, 0) == 1){
                sendMessage(player, "The kettle has not boiled yet.")
                return@on false
            }
            if(addItem(player, Items.HOT_KETTLE_7691)){
                replaceScenery(node as Scenery, valid_stoves[stoves_with_kettle.indexOf(node.id)], -1)
                setAttribute(player, kettleBoiled, 0)
                return@on true
            }
            else {
                sendMessage(player, "You do not have space to do that.")
                return@on false
            }
        }
    }

}
