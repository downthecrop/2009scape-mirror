package content.global.skill.fletching.log

import org.rs09.consts.Items

/**
 * Provides information pertaining to crafting different items from logs
 * @property logItemId the log item id that can be used to craft this item
 * @property finishedItemId the resulting finished bolt Item id.
 * @property level the level required to craft.
 * @property experience the experience gained by crafting.
 */
enum class LogCraftInfo(val logItemId: Int, val finishedItemId: Int, val experience: Double, val level: Int) {
    ARROW_SHAFT(Items.LOGS_1511, Items.ARROW_SHAFT_52, 5.0, 1),
    SHORT_BOW(Items.LOGS_1511, Items.SHORTBOW_U_50, 5.0, 5),
    LONG_BOW(Items.LOGS_1511, Items.LONGBOW_U_48, 10.0, 10),
    WOODEN_STOCK(Items.LOGS_1511, Items.WOODEN_STOCK_9440, 6.0, 9),

    OGRE_ARROW_SHAFT(Items.ACHEY_TREE_LOGS_2862, Items.OGRE_ARROW_SHAFT_2864, 6.4, 5),
    OGRE_COMP_BOW(Items.ACHEY_TREE_LOGS_2862, Items.UNSTRUNG_COMP_BOW_4825, 45.0, 30),

    OAK_SHORTBOW(Items.OAK_LOGS_1521, Items.OAK_SHORTBOW_U_54, 16.5, 20),
    OAK_LONGBOW(Items.OAK_LOGS_1521, Items.OAK_LONGBOW_U_56, 25.0, 25),
    OAK_STOCK(Items.OAK_LOGS_1521, Items.OAK_STOCK_9442, 16.0, 24),

    WILLOW_SHORTBOW(Items.WILLOW_LOGS_1519, Items.WILLOW_SHORTBOW_U_60, 33.3, 35),
    WILLOW_LONGBOW(Items.WILLOW_LOGS_1519, Items.WILLOW_LONGBOW_U_58, 41.5, 40),
    WILLOW_STOCK(Items.WILLOW_LOGS_1519, Items.WILLOW_STOCK_9444, 22.0, 39),

    MAPLE_SHORTBOW(Items.MAPLE_LOGS_1517, Items.MAPLE_SHORTBOW_U_64, 50.0, 50),
    MAPLE_LONGBOW(Items.MAPLE_LOGS_1517, Items.MAPLE_LONGBOW_U_62, 58.3, 55),
    MAPLE_STOCK(Items.MAPLE_LOGS_1517, Items.MAPLE_STOCK_9448, 32.0, 54),

    YEW_SHORTBOW(Items.YEW_LOGS_1515, Items.YEW_SHORTBOW_U_68, 67.5, 65),
    YEW_LONGBOW(Items.YEW_LOGS_1515, Items.YEW_LONGBOW_U_66, 75.0, 70),
    YEW_STOCK(Items.YEW_LOGS_1515, Items.YEW_STOCK_9452, 50.0, 69),

    MAGIC_SHORTBOW(Items.MAGIC_LOGS_1513, Items.MAGIC_SHORTBOW_U_72, 83.3, 80),
    MAGIC_LONGBOW(Items.MAGIC_LOGS_1513, Items.MAGIC_LONGBOW_U_70, 91.5, 85),

    TEAK_STOCK(Items.TEAK_LOGS_6333, Items.TEAK_STOCK_9446, 27.0, 46),

    MAHOGANY_STOCK(Items.MAHOGANY_LOGS_6332, Items.MAHOGANY_STOCK_9450, 41.0, 61);


    companion object {
        val logIds: IntArray = LogCraftInfo.values().map { logCraftInfo: LogCraftInfo -> logCraftInfo.logItemId }.distinct().toIntArray()

        private val logCraftablesByLogId = associateCraftInfoByLogIds()
        private fun associateCraftInfoByLogIds(): Map<Int, List<LogCraftInfo>> {
            val map = HashMap<Int, List<LogCraftInfo>>()

            for (logId in logIds) {
                map[logId] = LogCraftInfo.values().filter { logCraftInfo: LogCraftInfo -> logCraftInfo.logItemId == logId }
            }

            return map
        }

        fun forLogId(logId: Int): List<LogCraftInfo>? {
            return logCraftablesByLogId[logId]
        }
    }
}