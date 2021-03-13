package rs09.game.node.entity.skill.farming

import org.rs09.consts.Items

/**
 * I don't want to do it this way but jagex has forced my hand
 */
enum class BasketsAndSacks(val produceID: Int, val baseContainer: Int, val capacity : Int) {
    POTATO(Items.POTATO_1942, Items.POTATOES1_5420, 10),
    ONION(Items.ONION_1957, Items.ONIONS1_5440, 10),
    CABBAGE(Items.CABBAGE_1965, Items.CABBAGES1_5460, 10),
    APPLE(Items.COOKING_APPLE_1955, Items.APPLES1_5378, 5),
    BANANA(Items.BANANA_1963, Items.BANANAS1_5408, 5),
    ORANGE(Items.ORANGE_2108, Items.ORANGES1_5388, 5),
    STRAWBERRY(Items.STRAWBERRY_5504, Items.STRAWBERRIES1_5398, 5),
    TOMATO(Items.TOMATO_1982, Items.TOMATOES1_5960, 5);

    val containers = ArrayList<Int>()

    companion object{
        private val map = HashMap<Int,BasketsAndSacks>()

        init {
            values().map { it.produceID to it }.toMap(map)
            for(b in values()){
                b.containers.add(b.baseContainer)
                for(i in 1 until b.capacity){
                    map[b.baseContainer + (i * 2)] = b
                    b.containers.add(b.baseContainer + (i * 2))
                }
            }
        }

        @JvmStatic
        fun forId(itemId: Int): BasketsAndSacks?{
            return map[itemId]
        }
    }

    fun checkIsLast(containerID: Int): Boolean {
        return containerID == containers.last()
    }

    fun checkIsFirst(containerID: Int): Boolean {
        return containerID == containers.first()
    }

    fun checkWhich(containerID: Int): Int{
        return containers.indexOf(containerID)
    }
}