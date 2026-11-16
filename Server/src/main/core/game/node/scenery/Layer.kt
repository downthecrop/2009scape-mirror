package core.game.node.scenery

import core.api.log
import core.tools.Log

enum class Layer {
    WALL,
    WALL_DECOR,
    GROUND,
    GROUND_DECOR;

    companion object {
        /**
         * Gets the authentic client scenery layer
         * @return the layer
         */
        @JvmStatic
        fun getLayer(type: Int): Layer? {
            return when (type) {
                in 0..3 -> WALL // walls, doors
                9 -> WALL // diagonal doors
                in 4..8 -> WALL_DECOR
                in 10..11 -> GROUND // scenery
                in 12..17 -> GROUND // roofs, same layer as scenery
                in 18..21 -> GROUND // roof edges, same layer as scenery
                22 -> GROUND_DECOR
                else -> null.also { log(this::class.java, Log.ERR, "Invalid scenery type $type") }
            }
        }

        /**
         * Returns the index of the authentic client scenery layer
         * @return the layer index
         */
        @JvmStatic
        fun getIndex(type: Int): Int = getLayer(type)?.ordinal ?: -1
    }
}
