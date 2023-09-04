import content.data.consumables.Consumables
import core.api.IfaceSettingsBuilder
import core.api.splitLines
import content.global.skill.slayer.Master
import content.global.skill.slayer.SlayerManager
import content.global.skill.slayer.Tasks
import core.game.node.item.Item
import core.api.utils.Vector
import core.game.world.map.Direction
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class APITests {
    var testPlayer: MockPlayer
    var testPlayer2: MockPlayer

    init {
        TestUtils.preTestSetup()
        testPlayer = TestUtils.getMockPlayer("test")
        testPlayer2 = TestUtils.getMockPlayer("test2")
    }

    @Test fun testIfaceSettings(){
        var builder = IfaceSettingsBuilder()
        val testOptions = builder.enableOptions(0..9).build()
        Assertions.assertEquals(2046, testOptions, "Testing option flags")

        builder = IfaceSettingsBuilder()
        val testSlotSwitch = builder.enableSlotSwitch().build()
        Assertions.assertEquals(2097152, testSlotSwitch, "Testing slot switch flag")

        builder = IfaceSettingsBuilder()
        val testNullSlot = builder.enableNullSlotSwitch().build()
        Assertions.assertEquals(8388608, testNullSlot, "Testing null slotswitch flag")

        builder = IfaceSettingsBuilder()
        val testUseWithFlags = builder.setUseOnSettings(true, true, true, true, true, true).build()
        Assertions.assertEquals(129024, testUseWithFlags, "Testing usewith flags")

        builder = IfaceSettingsBuilder()
        val testIfaceDepth = builder.setInterfaceEventsDepth(2).build()
        Assertions.assertEquals(2 shl 18, testIfaceDepth and 1835008, "Testing events depth")

        builder = IfaceSettingsBuilder()
        val testUseOption = builder.enableUseOption().build()
        Assertions.assertEquals(1 shl 17, testUseOption, "Testing use option")

        builder = IfaceSettingsBuilder()
        val testUseOnSelf = builder.enableUseOnSelf().build()
        Assertions.assertEquals(1 shl 22, testUseOnSelf, "Testing use on self flag")

        builder = IfaceSettingsBuilder()
        val testAllOptions = builder
            .enableAllOptions()
            .enableExamine()
            .enableNullSlotSwitch()
            .enableSlotSwitch()
            .enableUseOnSelf()
            .enableUseOption()
            .setInterfaceEventsDepth(2)
            .setUseOnSettings(true, true, true, true, true, true)
            .build()
        Assertions.assertEquals(15466494, testAllOptions, "Testing all options")
    }

    @Test fun testSlayerManagerSaveAndLoadAndSaveProducesEquivalentJSON() {
        var manager = SlayerManager()
        manager.login(testPlayer)
        manager.login(testPlayer2)
        manager = SlayerManager.getInstance(testPlayer)
        manager.flags.setPoints(20)
        manager.flags.setMaster(Master.CHAELDAR)
        manager.flags.setTask(Tasks.SKELETAL_WYVERN)
        manager.flags.setTaskAmount(500)

        val manager2 = SlayerManager.getInstance(testPlayer2)

        val jsonFirst = JSONObject()
        manager.savePlayer(testPlayer, jsonFirst)
        manager.parsePlayer(testPlayer2, jsonFirst)

        val jsonSecond = JSONObject()
        manager2.savePlayer(testPlayer2, jsonSecond)
        Assertions.assertEquals(jsonFirst.toJSONString(), jsonSecond.toJSONString())
    }

    @Test fun testSlayerSaveAndParseProducesEquivalent() {
        var manager = SlayerManager()
        manager.login(testPlayer)
        manager = SlayerManager.getInstance(testPlayer)
        manager.flags.setPoints(500)
        manager.flags.unlockHelm()
        manager.flags.unlockBroads()
        manager.flags.unlockRing()

        val json = JSONObject()
        manager.savePlayer(testPlayer, json)
        manager.flags.fullClear()
        manager.parsePlayer(testPlayer, json)
        Assertions.assertEquals(500, manager.flags.getPoints(), "Points were not 500!")
        Assertions.assertEquals(true, manager.flags.isHelmUnlocked(), "Helm was not unlocked!")
        Assertions.assertEquals(true, manager.flags.isBroadsUnlocked(), "Broads were not unlocked!")
        Assertions.assertEquals(true, manager.flags.isRingUnlocked(), "Ring was not unlocked!")
    }

    @Test fun testSlayerDecrementTaskAmountHasNoSideEffects() {
        var manager = SlayerManager()
        manager.login(testPlayer)
        manager = SlayerManager.getInstance(testPlayer)
        manager.flags.setTask(Tasks.CAVE_BUG)
        manager.flags.setTaskAmount(100)
        manager.flags.taskStreak = 4
        manager.flags.completedTasks = 4
        manager.flags.setMaster(Master.MAZCHNA)

        while(manager.hasTask()) manager.decrementAmount(1)
        manager.flags.taskStreak += 1
        manager.flags.completedTasks += 1
        manager.flags.flagCanEarnPoints()

        Assertions.assertEquals(0, manager.flags.getTaskAmount(), "Task amount was not 0!")
        Assertions.assertEquals(5, manager.flags.taskStreak, "Task streak was not 5!")
        Assertions.assertEquals(Tasks.CAVE_BUG, manager.flags.getTask(), "Task was not cave bugs!")
        Assertions.assertEquals(Master.MAZCHNA, manager.flags.getMaster(), "Master was not Mazchna!")
    }

    @Test fun testKnownProblemSaveParsesCorrectly() {
        val jsonString = "{\"slayer\": {\n" +
                "    \"taskStreak\": \"21\",\n" +
                "    \"rewardFlags\": 17301511,\n" +
                "    \"equipmentFlags\": 31,\n" +
                "    \"taskFlags\": 307220,\n" +
                "    \"removedTasks\": [\n" +
                "      \"73\"\n" +
                "    ],\n" +
                "    \"totalTasks\": \"108\"\n" +
                "  }}"

        val slayerData = JSONParser().parse(jsonString) as JSONObject
        var manager = SlayerManager()
        manager.login(testPlayer)
        manager = SlayerManager.getInstance(testPlayer)
        manager.parsePlayer(testPlayer, slayerData)

        Assertions.assertEquals(21, manager.flags.taskStreak)
        Assertions.assertNotEquals(0, manager.flags.getPoints())
        Assertions.assertEquals(true, manager.flags.isHelmUnlocked())
    }

    @Test fun lineSplitShouldSplitAtLimitAndPreserveAllWords() {
        var testCase = "The monks are running a ship from Port Sarim to Entrana, I hear too. Now leave me alone yer elephant!"
        var expectedLine1 = "The monks are running a ship from Port Sarim to"
        var expectedLine2 = "Entrana, I hear too. Now leave me alone yer elephant!"
        var lines = splitLines(testCase, 54)
        Assertions.assertEquals(expectedLine1, lines.getOrNull(0) ?: "")
        Assertions.assertEquals(expectedLine2, lines.getOrNull(1) ?: "")
        Assertions.assertEquals(2, lines.size)

        testCase = "Dramenwood staffs are crafted from branches of the Dramen tree, so they are. I hear there's a Dramen tree over on the island of Entrana in a cave."
        expectedLine1 = "Dramenwood staffs are crafted from branches of the"
        expectedLine2 = "Dramen tree, so they are. I hear there's a Dramen tree"
        var expectedLine3 = "over on the island of Entrana in a cave."
        lines = splitLines(testCase, 54)
        Assertions.assertEquals(expectedLine1, lines.getOrNull(0) ?: "")
        Assertions.assertEquals(expectedLine2, lines.getOrNull(1) ?: "")
        Assertions.assertEquals(expectedLine3, lines.getOrNull(2) ?: "")
        Assertions.assertEquals(3, lines.size)

        testCase = "This should be one line."
        lines = splitLines(testCase)
        Assertions.assertEquals(testCase, lines[0])
        Assertions.assertEquals(1, lines.size)

        testCase = "I just told you: from the Seer. You will need to persuade him to take the time to make a forecast somehow."
        lines = splitLines(testCase)
        expectedLine1 = "I just told you: from the Seer. You will need to"
        expectedLine2 = "persuade him to take the time to make a forecast"
        expectedLine3 = "somehow."
        Assertions.assertEquals(expectedLine1, lines.getOrNull(0) ?: "")
        Assertions.assertEquals(expectedLine2, lines.getOrNull(1) ?: "")
        Assertions.assertEquals(expectedLine3, lines.getOrNull(2) ?: "")
    }

    @Test fun consumableStackableItemShouldNotRemoveStack() {
        val stackableItem = Item(Items.PURPLE_SWEETS_10476, 999)
        TestUtils.getMockPlayer("Inventory Consumable Stack Slot Tester").use { player ->
            // Inventory setup
            player.inventory.clear()
            player.inventory.add(stackableItem, false, 0)

            // Setup
            val consumable = Consumables.getConsumableById(stackableItem.id)
            consumable.consumable.consume(player.inventory.get(0), player)
            TestUtils.advanceTicks(2, false)

            // Get item in that slot,
            val updatedConsumable = player.inventory.get(0)

            // Maintains slot clicked + Amount is decremented
            Assertions.assertEquals(0, updatedConsumable.slot)
            Assertions.assertEquals(998, updatedConsumable.amount)
        }
    }

    @Test fun consumableMultiPieceItemShouldBeRemovedFromCorrectSlot() {
        val consumables: Array<Item?> = arrayOf(
            Item(Items.CAKE_1891, 8),
            Item(Items.TWO_THIRDS_CAKE_1893, 8),
            Item(Items.SLICE_OF_CAKE_1895, 8)
        )

        TestUtils.getMockPlayer("Inventory Consumable Multi Piece Tester").use { player ->
            // Inventory setup
            player.inventory.clear()
            player.inventory.add(*consumables)

            val lastWholeCakeContainerIndex = 7
            val lastWholeCake = player.inventory.get(lastWholeCakeContainerIndex)

            val consumable = Consumables.getConsumableById(lastWholeCake.id)
            consumable.consumable.consume(player.inventory.get(lastWholeCakeContainerIndex), player)
            TestUtils.advanceTicks(2, false)

            // Cake amounts are correct
            val wholeCakeAmount = player.inventory.getAmount(Items.CAKE_1891)
            val twoThirdsCakeAmount = player.inventory.getAmount(Items.TWO_THIRDS_CAKE_1893)
            Assertions.assertEquals(7, wholeCakeAmount)
            Assertions.assertEquals(9, twoThirdsCakeAmount)

            // Cake was replaced in correct spot
            val inventorySlot0 = player.inventory.get(0)
            val inventorySlot7 = player.inventory.get(7)
            Assertions.assertEquals(Items.CAKE_1891, inventorySlot0.id)
            Assertions.assertEquals(Items.TWO_THIRDS_CAKE_1893, inventorySlot7.id)
        }
    }

    @Test fun consumableMultiPieceItemShouldAddReturnItemToCorrectSlot() {
        val PIE_DISH_NONCONSUMABLE_2313 = Items.PIE_DISH_2313
        val consumables: Array<Item?> = arrayOf(
            Item(Items.APPLE_PIE_2323, 8),
            Item(Items.HALF_AN_APPLE_PIE_2335, 8),
            Item(Items.REDBERRY_PIE_2325, 8)
        )

        TestUtils.getMockPlayer("Inventory Consumable Multi Piece With Return Tester").use { player ->
            // Inventory setup
            player.inventory.clear()
            player.inventory.add(*consumables)

            val lastWholePieContainerIndex = 7
            val lastWholePie = player.inventory.get(lastWholePieContainerIndex)

            val wholePieConsumable = Consumables.getConsumableById(lastWholePie.id)
            wholePieConsumable.consumable.consume(player.inventory.get(lastWholePieContainerIndex), player)
            TestUtils.advanceTicks(2, false)

            // Pie amounts are correct
            var wholePieAmount = player.inventory.getAmount(Items.APPLE_PIE_2323)
            var halfPieAmount = player.inventory.getAmount(Items.HALF_AN_APPLE_PIE_2335)
            Assertions.assertEquals(7, wholePieAmount)
            Assertions.assertEquals(9, halfPieAmount)

            // Pie was replaced in correct spot
            val inventorySlot0 = player.inventory.get(0)
            val inventorySlot7 = player.inventory.get(7)
            Assertions.assertEquals(Items.APPLE_PIE_2323, inventorySlot0.id)
            Assertions.assertEquals(Items.HALF_AN_APPLE_PIE_2335, inventorySlot7.id)

            // Tests for pie halves + pie tins
            val firstHalfPieContainerIndex = 7
            val firstHalfPie = player.inventory.get(firstHalfPieContainerIndex)
            val halfPieConsumable = Consumables.getConsumableById(firstHalfPie.id)
            halfPieConsumable.consumable.consume(player.inventory.get(firstHalfPieContainerIndex), player)
            TestUtils.advanceTicks(2, false)

            // Pie amounts are correct
            halfPieAmount = player.inventory.getAmount(Items.HALF_AN_APPLE_PIE_2335)
            val pieDishAmount = player.inventory.getAmount(PIE_DISH_NONCONSUMABLE_2313)
            Assertions.assertEquals(8, halfPieAmount)
            Assertions.assertEquals(1, pieDishAmount)

            val updatedSlot7 = player.inventory.get(7)
            Assertions.assertEquals(PIE_DISH_NONCONSUMABLE_2313, updatedSlot7.id)
        }
    }

    @Test fun consumableItemShouldNotHaveReturnItem() {
        val consumables: Array<Item?> = arrayOf(
            Item(Items.TROUT_333, 8),
            Item(Items.SHARK_385, 8),
            Item(Items.LOBSTER_379, 8)
        )
        TestUtils.getMockPlayer("Inventory Consumable No Return Item Tester").use { player ->
            // Inventory setup
            player.inventory.clear()
            player.inventory.add(*consumables)

            // Feed the player copious amounts of fish
            val lastTroutContainerIndex = 7
            val lastTrout = player.inventory.get(lastTroutContainerIndex)

            val troutConsumable = Consumables.getConsumableById(lastTrout.id)
            troutConsumable.consumable.consume(player.inventory.get(lastTroutContainerIndex), player)
            TestUtils.advanceTicks(4, false)

            val sharkConsumable = Consumables.getConsumableById(Items.SHARK_385)
            for (n in 0..7) {
                sharkConsumable.consumable.consume(player.inventory.get(n + 8), player)
                TestUtils.advanceTicks(4, false)
            }

            val lobsterConsumable = Consumables.getConsumableById(Items.LOBSTER_379)
            for (n in 16..23 step 2) {
                lobsterConsumable.consumable.consume(player.inventory.get(n), player)
                TestUtils.advanceTicks(4, false)
            }

            // Trout amounts are correct
            val troutAmount = player.inventory.getAmount(Items.TROUT_333)
            Assertions.assertEquals(7, troutAmount)

            // Trout was removed from the correct spot
            val inventorySlot0 = player.inventory.get(0)
            val inventorySlot7: Item? = player.inventory.get(7)
            Assertions.assertEquals(Items.TROUT_333, inventorySlot0.id)
            Assertions.assertNull(inventorySlot7)

            val sharkAmount = player.inventory.getAmount(Items.SHARK_385)
            Assertions.assertEquals(0, sharkAmount)
            for (n in 8..15) {
                val inventoryItem: Item? = player.inventory.get(n)
                Assertions.assertNull(inventoryItem)
            }

            val lobsterAmount = player.inventory.getAmount(Items.LOBSTER_379)
            Assertions.assertEquals(4, lobsterAmount)

            for (n in 16..23) {
                if (n % 2 == 0) {
                    val inventoryItem: Item? = player.inventory.get(n)
                    Assertions.assertNull(inventoryItem)
                } else {
                    val inventoryItem: Item = player.inventory.get(n)
                    Assertions.assertEquals(Items.LOBSTER_379, inventoryItem.id)
                }
            }
        }
    }

    @Test fun vectorToDirectionShouldReturnExpectedDirections() {
        val testData = arrayOf(
            Pair(Vector(1.0, 0.0), Direction.EAST),
            Pair(Vector(-1.0, 0.0), Direction.WEST),
            Pair(Vector(0.0, 1.0), Direction.NORTH),
            Pair(Vector(0.0, -1.0), Direction.SOUTH),
            Pair(Vector(1.0, 1.0), Direction.NORTH_EAST),
            Pair(Vector(1.0, -1.0), Direction.SOUTH_EAST),
            Pair(Vector(-1.0, 1.0), Direction.NORTH_WEST),
            Pair(Vector(-1.0, -1.0), Direction.SOUTH_WEST),
            Pair(Vector(15.0, 0.0), Direction.EAST),
            Pair(Vector(15.0, 1.0), Direction.EAST),
            Pair(Vector(-15.0, -9.7), Direction.SOUTH_WEST)
        )

        for ((vec, expDir) in testData) {
            Assertions.assertEquals(expDir, vec.toDirection(), "Vector: $vec")
        }
    }
}