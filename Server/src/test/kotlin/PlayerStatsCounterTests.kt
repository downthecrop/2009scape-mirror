import core.api.utils.PlayerStatsCounter
import core.game.node.item.Item
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS) class PlayerStatsCounterTests {
    companion object {
        private const val TEST_DB_PATH = "player_stats_test.db"
        private val counter = PlayerStatsCounter(TEST_DB_PATH)
        init {
            TestUtils.preTestSetup()
            counter.startup()
        }
        @AfterAll @JvmStatic fun cleanup() {
            File(TEST_DB_PATH).delete()
        }
    }

    @Test fun testKillIncrementShouldAddOneKillForNewPlayer() {
        val testPlayer = TestUtils.getMockPlayer("test_kill_inc")
        val testNPCId = 10

        val oldKills = PlayerStatsCounter.getKills(testPlayer, IntArray(testNPCId))
        Assertions.assertEquals(0, oldKills)

        PlayerStatsCounter.incrementKills(testPlayer,testNPCId)

        val newKills = PlayerStatsCounter.getKills(testPlayer, intArrayOf(testNPCId))
        Assertions.assertEquals(1, newKills)
    }

    @Test fun testGetKillShouldReturnKillsForSinglePlayer() {
        val testPlayer = TestUtils.getMockPlayer("kill_single_player_1")
        val testPlayer2 = TestUtils.getMockPlayer("kill_single_player_2")
        val testNPCId = 10

        PlayerStatsCounter.incrementKills(testPlayer,testNPCId)

        PlayerStatsCounter.incrementKills(testPlayer2,testNPCId)
        PlayerStatsCounter.incrementKills(testPlayer2,testNPCId)

        val kills = PlayerStatsCounter.getKills(testPlayer2, intArrayOf(testNPCId))
        Assertions.assertEquals(2, kills)
    }

    @Test fun testGetKillShouldReturnSumForAllNPCsForAGivenPlayer() {
        val testPlayer = TestUtils.getMockPlayer("test_sum_npcs")
        val testNPCId1 = 10
        val testNPCId2 = 11
        val testNPCId3 = 12

        PlayerStatsCounter.incrementKills(testPlayer,testNPCId1)

        PlayerStatsCounter.incrementKills(testPlayer,testNPCId2)
        PlayerStatsCounter.incrementKills(testPlayer,testNPCId2)
        PlayerStatsCounter.incrementKills(testPlayer,testNPCId2)

        PlayerStatsCounter.incrementKills(testPlayer,testNPCId3)
        PlayerStatsCounter.incrementKills(testPlayer,testNPCId3)

        val killsForNPCs1And2 = PlayerStatsCounter.getKills(testPlayer, intArrayOf(testNPCId1, testNPCId2))
        Assertions.assertEquals(4, killsForNPCs1And2)
        val killsForAllNPCs = PlayerStatsCounter.getKills(testPlayer, intArrayOf(testNPCId1, testNPCId2, testNPCId3))
        Assertions.assertEquals(6, killsForAllNPCs)
    }

    @Test fun testRewardIncrementShouldAddOneRewardForNewPlayer() {
        val testPlayer = TestUtils.getMockPlayer("test_reward_inc")
        val itemId = 10

        val oldRareDrops = PlayerStatsCounter.getRareDrops(testPlayer, itemId)
        Assertions.assertEquals(0, oldRareDrops)

        PlayerStatsCounter.incrementRareDrop(testPlayer, Item(itemId))

        val newRareDrops = PlayerStatsCounter.getRareDrops(testPlayer, itemId)
        Assertions.assertEquals(1, newRareDrops)
    }

    @Test fun testGetRewardsShouldReturnRewardsForSinglePlayer() {
        val testPlayer = TestUtils.getMockPlayer("reward_single_player_1")
        val testPlayer2 = TestUtils.getMockPlayer("reward_single_player_2")
        val testItemId = 10

        PlayerStatsCounter.incrementRareDrop(testPlayer,Item(testItemId))

        PlayerStatsCounter.incrementRareDrop(testPlayer2,Item(testItemId))
        PlayerStatsCounter.incrementRareDrop(testPlayer2,Item(testItemId))

        val rareDrops = PlayerStatsCounter.getRareDrops(testPlayer2, testItemId)
        Assertions.assertEquals(2, rareDrops)
    }

    @Test fun testGetRewardsShouldHonourItemAmountWhenIncrementing() {
        val testPlayer = TestUtils.getMockPlayer("test_reward_inc_itemamount")
        val itemId = 10

        val itemAmount: Long = 5

        val oldRareDrops = PlayerStatsCounter.getRareDrops(testPlayer, itemId)
        Assertions.assertEquals(0, oldRareDrops)

        PlayerStatsCounter.incrementRareDrop(testPlayer, Item(itemId, itemAmount.toInt()))

        val newRareDrops = PlayerStatsCounter.getRareDrops(testPlayer, itemId)
        Assertions.assertEquals(itemAmount, newRareDrops)
    }
}
