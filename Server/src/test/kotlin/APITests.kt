import api.IfaceSettingsBuilder
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.skill.slayer.Master
import core.game.node.entity.skill.slayer.Tasks
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.node.entity.skill.slayer.SlayerFlags
import rs09.game.node.entity.skill.slayer.SlayerManager
import rs09.game.system.SystemLogger

object APITests {
    val testPlayer = TestUtils.getMockPlayer("test")
    val testPlayer2 = TestUtils.getMockPlayer("test2")

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
}