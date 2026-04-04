package content.global.skill.construction.decoration.pohstorage

import org.json.simple.JSONObject
import core.game.node.entity.player.Player

class StorageState(val player: Player) {

    private val containers = StorableFamily.values().associateWith { StorageContainer() }.toMutableMap()

    fun getContainer(type: StorableFamily): StorageContainer = containers[type]!!

    fun toJson(): JSONObject {
        val root = JSONObject()
        val containerData = JSONObject()

        containers.forEach { (group, container) ->
            containerData[group.name.lowercase()] = container.toJson()
        }

        root["containers"] = containerData
        return root
    }

    fun readJson(data: JSONObject) {
        val containerData = data["containers"] as? JSONObject ?: return

        for (group in StorableFamily.values()) {
            val json = containerData[group.name.lowercase()] as? JSONObject ?: continue
            containers[group]?.readJson(json)
        }
    }

    // clear storage
    fun clear() {
        containers.values.forEach { it.clear() }
    }
}
