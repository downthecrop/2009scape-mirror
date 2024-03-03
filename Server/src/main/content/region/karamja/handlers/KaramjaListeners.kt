package content.region.karamja.handlers

import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import org.rs09.consts.*

class KaramjaListeners : InteractionListener {
    companion object {
        /**
         * Represents the Musa Point dungeon location.
         */
        private val MUSA_POINT_DUNGEON = location(2856, 9567, 0)

        /**
         * Represents the volcano rim location.
         */
        private val VOLCANO_RIM = location(2856, 3167, 0)

        /**
         * Represents the rocks used to enter the Musa Point dungeon.
         */
        private const val MUSA_POINT_DUNGEON_ENTRANCE = Scenery.ROCKS_492

        /**
         * Represents the rope used to exit the Musa Point dungeon.
         */
        private const val MUSA_POINT_DUNGEON_EXIT = Scenery.CLIMBING_ROPE_1764

        /**
         * Represents the pineapple plant objects.
         */
        private val PINEAPPLE_PLANT = intArrayOf(
            Scenery.PINEAPPLE_PLANT_1408, Scenery.PINEAPPLE_PLANT_1409,
            Scenery.PINEAPPLE_PLANT_1410, Scenery.PINEAPPLE_PLANT_1411,
            Scenery.PINEAPPLE_PLANT_1412, Scenery.PINEAPPLE_PLANT_1413
        )
        private const val PICK_PINEAPPLE_ANIMATION = 2282
        private const val PINEAPPLE = Items.PINEAPPLE_2114

        private const val SHAKE_TREE_ANIMATION = 2572
        private const val PALM_LEAF = Items.PALM_LEAF_2339
        private const val PALM_TREE_FULL = Scenery.LEAFY_PALM_TREE_2975
        private const val PALM_TREE_EMPTY = Scenery.LEAFY_PALM_TREE_2976
    }

    override fun defineListeners() {
        on(MUSA_POINT_DUNGEON_ENTRANCE, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, MUSA_POINT_DUNGEON)
            sendMessage(player, "You climb down through the pot hole.")
            return@on true
        }

        on(MUSA_POINT_DUNGEON_EXIT, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, VOLCANO_RIM)
            sendMessage(player, "You climb up the hanging rope...")
            sendMessage(player, "You appear on the volcano rim.")
            return@on true
        }

        on(PINEAPPLE_PLANT, IntType.SCENERY, "pick") { player, node ->
            // TODO: Convert FieldPickingPlugin to Kotlin & include this.
            if (!hasSpaceFor(player, Item(PINEAPPLE))) {
                sendMessage(player, "You don't have enough space in your inventory.")
                return@on true
            }
            if (node.id == Scenery.PINEAPPLE_PLANT_1413) {
                sendMessage(player, "There are no pineapples left on this plant.")
                return@on true
            }
            val last: Boolean = node.id == Scenery.PINEAPPLE_PLANT_1412
            if (addItem(player, PINEAPPLE)) {
                animate(player, PICK_PINEAPPLE_ANIMATION)
                playAudio(player, Sounds.PICK_2581, 30)
                replaceScenery(node.asScenery(), node.id + 1, if (last) 270 else 40)
                sendMessage(player, "You pick a pineapple.")
            }
            return@on true
        }

        on(Scenery.BANANA_TREE_2078, IntType.SCENERY, "search") { player, _ ->
            // TODO: Convert FieldPickingPlugin to Kotlin & include this.
            sendMessage(player, "There are no bananas left on the tree.")
            return@on true
        }

        on(PALM_TREE_FULL, IntType.SCENERY, "Shake") { player, node ->
            queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        lock(player, 2)
                        face(player, node)
                        animate(player, SHAKE_TREE_ANIMATION)
                        sendMessage(player, "You give the tree a good shake.")
                        replaceScenery(node.asScenery(), PALM_TREE_EMPTY, 60)
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        produceGroundItem(
                            player,
                            PALM_LEAF,
                            1,
                            getPathableRandomLocalCoordinate(player, 1, node.location)
                        )
                        sendMessage(player, "A palm leaf falls to the ground.")
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }
}