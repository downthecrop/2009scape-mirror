package core.cache

import TestUtils
import api.itemDefinition
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ItemDefTests {
    val knownGood = HashMap<Int,Int>()
    init {
        TestUtils.preTestSetup()
    }

    @Test fun itemDefsShouldHaveExpectedValues() {
        File(TestUtils.loadFile("530_cache_item_values_from_client.csv")).useLines { lines ->
            lines.forEach {
                val toks = it.split(",")
                knownGood[toks[0].toIntOrNull() ?: return@forEach] = toks[toks.size - 1].toInt()
            }
        }
        for ((id, expectedValue) in knownGood) {
            val def = itemDefinition(id)
            Assertions.assertEquals(expectedValue, def.value, "Value of ${def.name} (${def.isUnnoted}) does not match known good values!")
        }
    }
}