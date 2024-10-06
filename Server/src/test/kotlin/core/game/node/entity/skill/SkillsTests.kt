package core.game.node.entity.skill

import TestUtils.getMockPlayer
import core.game.node.entity.player.info.login.PlayerSaveParser
import core.game.node.entity.player.info.login.PlayerSaver
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SkillsTests {
    init {
        TestUtils.preTestSetup()
    }

    @Test
    fun saveDynamicLevelsTest() {
        val player = getMockPlayer("")

        // Test migration of old save versions with incorrectly-saved dynamic levels
        val jsonparser = JSONParser()
        val json = jsonparser.parse("[{\"static\":\"55\",\"dynamic\":\"55\",\"id\":\"0\",\"experience\":\"177895.0\"},{\"static\":\"21\",\"dynamic\":\"21\",\"id\":\"1\",\"experience\":\"5120.0\"},{\"static\":\"23\",\"dynamic\":\"23\",\"id\":\"2\",\"experience\":\"6682.799999999999\"},{\"static\":\"26\",\"dynamic\":\"20\",\"id\":\"3\",\"experience\":\"9001.000000000002\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"4\",\"experience\":\"0.0\"},{\"static\":\"23\",\"dynamic\":\"20\",\"id\":\"5\",\"experience\":\"6772.5\"},{\"static\":\"37\",\"dynamic\":\"37\",\"id\":\"6\",\"experience\":\"28180.0\"},{\"static\":\"54\",\"dynamic\":\"54\",\"id\":\"7\",\"experience\":\"164425.0\"},{\"static\":\"40\",\"dynamic\":\"40\",\"id\":\"8\",\"experience\":\"40975.5\"},{\"static\":\"24\",\"dynamic\":\"24\",\"id\":\"9\",\"experience\":\"7260.0\"},{\"static\":\"40\",\"dynamic\":\"40\",\"id\":\"10\",\"experience\":\"40200.0\"},{\"static\":\"35\",\"dynamic\":\"35\",\"id\":\"11\",\"experience\":\"23750.0\"},{\"static\":\"60\",\"dynamic\":\"60\",\"id\":\"12\",\"experience\":\"292409.0\"},{\"static\":\"11\",\"dynamic\":\"11\",\"id\":\"13\",\"experience\":\"1371.5\"},{\"static\":\"75\",\"dynamic\":\"75\",\"id\":\"14\",\"experience\":\"1254300.0\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"15\",\"experience\":\"0.0\"},{\"static\":\"42\",\"dynamic\":\"42\",\"id\":\"16\",\"experience\":\"47742.5\"},{\"static\":\"10\",\"dynamic\":\"10\",\"id\":\"17\",\"experience\":\"1160.0\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"18\",\"experience\":\"0.0\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"19\",\"experience\":\"0.0\"},{\"static\":\"5\",\"dynamic\":\"5\",\"id\":\"20\",\"experience\":\"500.0\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"21\",\"experience\":\"0.0\"},{\"static\":\"1\",\"dynamic\":\"1\",\"id\":\"22\",\"experience\":\"0.0\"},{\"static\":\"11\",\"dynamic\":\"11\",\"id\":\"23\",\"experience\":\"1429.0\"}]") as JSONArray
        player.version = 1
        player.skills.parse(json)
        Assertions.assertTrue(player.skills.prayerPoints == 20.0)
        Assertions.assertTrue(player.skills.lifepoints == 20)
        Assertions.assertTrue(player.skills.dynamicLevels[Skills.PRAYER] == 23)
        Assertions.assertTrue(player.skills.dynamicLevels[Skills.HITPOINTS] == 26)

        // Test that serializing and parsing them again correctly updates the dynamic levels and keeps the hp/prayer points
        player.version = 2
        val root = JSONObject()
        PlayerSaver(player).saveSkills(root)
        val saveparser = PlayerSaveParser(player)
        saveparser.saveFile = root
        saveparser.parseSkills()
        Assertions.assertTrue(player.skills.prayerPoints == 20.0)
        Assertions.assertTrue(player.skills.lifepoints == 20)
        Assertions.assertTrue(player.skills.dynamicLevels[Skills.PRAYER] == 23)
        Assertions.assertTrue(player.skills.dynamicLevels[Skills.HITPOINTS] == 26)
    }
}
