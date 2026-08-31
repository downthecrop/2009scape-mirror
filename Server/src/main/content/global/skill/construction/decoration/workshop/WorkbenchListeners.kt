package content.global.skill.construction.decoration.workshop

import content.global.skill.construction.BuildHotspot
import content.global.skill.construction.BuildingUtils
import content.global.skill.construction.Decoration
import content.global.skill.construction.Hotspot
import content.global.skill.construction.decoration.StaySeated
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Animations
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.Scenery

/**
 * Handles usage of the workbench, which allows construction of flatpacks.
 * @author Bishop
 */

class WorkbenchListeners : InteractionListener, InterfaceListener {

    companion object {
        const val ATTRIBUTE_FLATPACK_MODE         = "con:workbench:active"
        const val ATTRIBUTE_WORKBENCH_ID          = "con:workbench:id"
        const val ATTRIBUTE_WORKBENCH_SELECTION   = "con:workbench:hotspot"

        private val workbenches = intArrayOf(
            Scenery.WORKBENCH_13704,     // lv20, wooden
            Scenery.WORKBENCH_13705,     // lv40, oak
            Scenery.WORKBENCH_13706,     // lv60, steel framed
            Scenery.WORKBENCH_13707,     // lv80, with vice
            Scenery.WORKBENCH_13708,     // lv99, with lathe
        )

        private val decoCategories = arrayOf(
            BuildHotspot.CHAIRS_1,       // ButtonID 111
            BuildHotspot.BOOKCASE,       // ButtonID 112
            BuildHotspot.BARRELS,        // ButtonID 113
            BuildHotspot.KITCHEN_TABLE,  // ButtonID 114
            BuildHotspot.DINING_TABLE,   // ButtonID 115
            BuildHotspot.DINING_BENCH_1, // ButtonID 116
            BuildHotspot.BED,            // ButtonID 117
            BuildHotspot.DRESSER,        // ButtonID 118
            BuildHotspot.DRAWERS,        // ButtonID 119
            BuildHotspot.CLOCK,          // ButtonID 120
            BuildHotspot.CAPE_RACK,      // ButtonID 121
            BuildHotspot.MAGIC_WARDROBE, // ButtonID 122
            BuildHotspot.ARMOUR_CASE,    // ButtonID 123
            BuildHotspot.TREASURE_CHEST, // ButtonID 124
            BuildHotspot.COSTUME_BOX,    // ButtonID 125
            BuildHotspot.TOY_BOX,        // ButtonID 126
        )

        /**
         * Returns the maximum construction level of decoration that can be built at a given workbench by id.
         */
        fun getBenchLevel(workbenchId: Int): Int {
            return ((workbenches.indexOf(workbenchId) + 1) * 20)
        }

        /**
         * Animates the player crafting a flatpack, and exchanges its materials for the flatpack item.
         * Flatpacks are free to produce for players with admin rights.
         */
        fun produceFlatpack(player: Player, deco: Decoration) {
            val reward = deco.flatpackItemID
            val animation = if (getScenery(player.location)?.id == Scenery.STOOL_13720) { Animations.HUMAN_CRAFT_POH_OAK_STOOL_4110 } else { Animations.HUMAN_CRAFT_POH_STOOL_4109 }
            if (reward == -1) return
            if (removeItemsIfPlayerHasEnough(player, *deco.items) || player.isAdmin) {
                closeInterface(player)
                lock(player, getAnimation(animation).duration)
                animate(player, animation)
                rewardXP(player, Skills.CONSTRUCTION, deco.experience.toDouble())
                addItemOrDrop(player, reward)
            }
        }
    }

    override fun defineListeners() {
        // Use the workbench
        on(workbenches, IntType.SCENERY, "work-at") { player, node ->
            setAttribute(player, ATTRIBUTE_WORKBENCH_ID, node.id)
            // Skip the animation if the player is already sitting down
            if (getAttribute(player, StaySeated.ATTRIBUTE_SEATED, false)) {
                val animation = if (getScenery(player.location)?.id == Scenery.STOOL_13720) { Animations.HUMAN_SEATED_POH_OAK_STOOL_4108 } else { Animations.HUMAN_SEATED_POH_STOOL_4107 }
                animate(player, animation)
                openInterface(player, Components.POH_WORKBENCH_397)
                return@on true
            }
            StaySeated.seat(player, node as core.game.node.scenery.Scenery)
            queueScript(player, 0) {
                openInterface(player, Components.POH_WORKBENCH_397)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        // Upgrade the workbench
        on(workbenches, IntType.SCENERY, "upgrade") { player, node ->
            // I could not find any video of how this looks or what the messages are, so all messages in this listener are placeholders
            val hotspot = Hotspot(BuildHotspot.WORKBENCH, 3, 4, 4, 4)
            val nodeObj = node as core.game.node.scenery.Scenery
            val resultDeco = Decoration.forObjectId(node.id + 1)
            if (!player.houseManager.isBuildingMode) {
                sendMessage(player, "You have to be in building mode to do this.") // I don't actually know if this is true but better safe than sorry
                return@on true
            }
            if (getDynLevel(player, Skills.CONSTRUCTION) < resultDeco.level) {
                sendMessage(player, "You need a Construction level of ${resultDeco.level} to upgrade this.")
                return@on true
            }
            if (!inInventory(player, Items.OAK_PLANK_8778, 2) || !inInventory(player, Items.STEEL_BAR_2353)) {
                sendMessage(player, "You need two oak planks and a steel bar to upgrade this.")
                return@on true
            }
            if (!inInventory(player, Items.HAMMER_2347) || !inInventory(player, Items.SAW_8794)) {
                sendMessage(player, "You need a hammer and a saw to upgrade this.")
                return@on true
            }
            if (removeItemsIfPlayerHasEnough(player, Item(Items.OAK_PLANK_8778, 2), Item(Items.STEEL_BAR_2353))) {
                queueScript(player, 0, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            lock(player, BuildingUtils.BUILD_MID_ANIM.duration)
                            // Replaces the workbench with the upgraded form in HouseManager but not immediately in the instance
                            BuildingUtils.buildDecoration(player, hotspot, resultDeco, nodeObj, true, false)
                            return@queueScript delayScript(player, BuildingUtils.BUILD_MID_ANIM.duration)
                        }
                        1 -> {
                            // Temporary replacement until POH can rebuild on next entrance with the correct workbench
                            replaceScenery(nodeObj, resultDeco.objectId, -1)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        // Select the category of decoration to craft
        on(Components.POH_WORKBENCH_397) { player, _, _, buttonID, _, _ ->
            val index = buttonID - 111
            if (index < 0 || index >= decoCategories.size) return@on false
            val hotspot = decoCategories[index]
            setAttribute(player, ATTRIBUTE_WORKBENCH_SELECTION, hotspot)
            setAttribute(player, ATTRIBUTE_FLATPACK_MODE, true)
            BuildingUtils.openBuildInterface(player, hotspot)
            return@on true
        }

        onClose(Components.POH_WORKBENCH_397) { player, _ ->
            if (!getAttribute(player, ATTRIBUTE_FLATPACK_MODE, false)) {
                removeAttributes(player, ATTRIBUTE_FLATPACK_MODE, ATTRIBUTE_WORKBENCH_SELECTION, ATTRIBUTE_WORKBENCH_ID)
            }
            return@onClose true
        }

        onClose(Components.POH_DECORATION_396) { player, _ ->
            removeAttributes(player, ATTRIBUTE_FLATPACK_MODE, ATTRIBUTE_WORKBENCH_SELECTION, ATTRIBUTE_WORKBENCH_ID)
            return@onClose true
        }
    }

}