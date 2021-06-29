package rs09.game.system.config

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigParser {
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
    }
}