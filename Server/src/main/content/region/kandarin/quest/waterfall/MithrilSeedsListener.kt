package content.region.kandarin.quest.waterfall

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.world.GameWorld.ticks
import core.game.world.map.RegionManager.getObject
import core.tools.RandomFunction
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Scenery as SceneryObj

/**
 * Handles planting flowers with mithril seeds.
 * @author Bishop
 */

class MithrilSeedsListener : InteractionListener {

    companion object {
        private const val ATTRIBUTE_DELAY = "mithril-flowers:delay"
        private val flowersToItems        = mapOf(
            SceneryObj.FLOWERS_2980 to Items.FLOWERS_2460, // multicolored pastels
            SceneryObj.FLOWERS_2981 to Items.FLOWERS_2462, // red
            SceneryObj.FLOWERS_2982 to Items.FLOWERS_2464, // blue
            SceneryObj.FLOWERS_2983 to Items.FLOWERS_2466, // yellow
            SceneryObj.FLOWERS_2984 to Items.FLOWERS_2468, // magenta
            SceneryObj.FLOWERS_2985 to Items.FLOWERS_2470, // orange
            SceneryObj.FLOWERS_2986 to Items.FLOWERS_2472, // multicolored primaries
            SceneryObj.FLOWERS_2987 to Items.FLOWERS_2474, // white
            SceneryObj.FLOWERS_2988 to Items.FLOWERS_2476, // black
        )
    }

    override fun defineListeners() {
        on(Items.MITHRIL_SEEDS_299, IntType.ITEM, "plant") { player, _ ->
            closeAllInterfaces(player)
            val flowerLoc = player.location
            if (getAttribute(player, ATTRIBUTE_DELAY, -1) > ticks) {
                return@on false
            }
            if (getObject(flowerLoc) != null) {
                sendMessage(player, "You can't plant a flower here.")
                return@on true
            }
            if (removeItem(player, Items.MITHRIL_SEEDS_299)) {
                // Source: Mod Ash https://reldo.runescape.wiki/i/106311
                val flowerObj = when (RandomFunction.random(1001)) {
                    in 0..897   -> RandomFunction.random(SceneryObj.FLOWERS_2981, SceneryObj.FLOWERS_2986 + 1) // normal flowers
                    in 898..997 -> SceneryObj.FLOWERS_2980 // multicolored pastels, 100/1001
                    in 998..999 -> SceneryObj.FLOWERS_2988 // black flowers, 2/1001
                    else        -> SceneryObj.FLOWERS_2987 // white flowers, 1/1001
                }
                val thisFlower = addScenery(flowerObj, flowerLoc, 0, 10)
                sendMessage(player, "You open the small mithril case and drop a seed by your feet.")
                setAttribute(player, ATTRIBUTE_DELAY, ticks + 3)
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                stopWalk(player)
                player.moveStep()
                lock(player, 1)
                queueScript(player, 1, QueueStrength.NORMAL) {
                    face(player, flowerLoc)
                    openDialogue(player, object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> sendDialogueOptions(player, "Select an Option", "Pick the flowers.", "Leave the flowers.").also { stage++ }
                                1 -> {
                                    when (buttonID) {
                                        1 -> {
                                            animate(player, Animations.HUMAN_BURYING_BONES_827)
                                            lock(player, 2)
                                            queueScript(player, 2, QueueStrength.NORMAL) {
                                                sendMessage(player, "You pick the flowers.")
                                                removeScenery(thisFlower)
                                                addItemOrDrop(player, flowersToItems[thisFlower.id]!!)
                                                return@queueScript stopExecuting(player)
                                            }
                                            end()
                                        }
                                        else -> end()
                                    }
                                }
                            }
                        }
                    })
                    return@queueScript stopExecuting(player)
                }
                queueScript(player, 100, QueueStrength.SOFT) {
                    removeScenery(thisFlower)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }
}