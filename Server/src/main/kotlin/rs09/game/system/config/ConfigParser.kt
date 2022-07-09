package rs09.game.system.config

import api.*

import rs09.game.world.repository.Repository
import rs09.game.system.command.Privilege
import core.game.world.map.RegionManager
import core.game.node.item.GroundItemManager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigParser : Commands {
    fun prePlugin() {
        NPCConfigParser().load()
        ItemConfigParser().load()
        ObjectConfigParser().load()
        XteaParser().load()
        InterfaceConfigParser().load()
    }
    fun postPlugin() {
        ShopParser().load()
        DropTableParser().load()
        NPCSpawner().load()
        DoorConfigLoader().load()
        GroundSpawnLoader().load()
        MusicConfigLoader().load()
        RangedConfigLoader().load()
        CustomVarbitParser().load()
        ClueRewardParser().load()
    }

    fun reloadConfigs(callback: () -> Unit) {
        GlobalScope.launch { 
            Repository.npcs.toTypedArray().forEach { npc -> 
                npc.isRespawn = false
                npc.clear()
                Repository.npcs.remove(npc)
                Repository.removeRenderableNPC(npc)
            }
            
            GroundItemManager.getItems().toTypedArray().forEach {gi -> 
                GroundItemManager.getItems().remove(gi)
                RegionManager.getRegionPlane(gi.location).remove(gi) 
            }

            prePlugin()
            postPlugin()

            callback.invoke()
        }
    }

    override fun defineCommands() {
        define("reloadjson", Privilege.ADMIN, "", "Reloads all the JSON configs.") { player, _ -> 
            notify(player, "Reloading JSON...")
            reloadConfigs() { notify(player, "JSON reloaded.") }
        }
    }
}
