package rs09.game.system.command.sets

import api.sendMessage
import core.cache.Cache
import core.cache.def.impl.DataMap
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.VarbitDefinition
import core.cache.def.impl.Struct
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.net.packet.context.PlayerContext
import core.net.packet.out.ResetInterface
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Items
import rs09.game.system.SystemLogger
import rs09.game.system.command.Privilege
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import rs09.net.packet.PacketWriteQueue

@Initializable
class DevelopmentCommandSet : CommandSet(Privilege.ADMIN) {
    val farmKitItems = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325)

    override fun defineCommands() {

        /**
         * Gives the player a set of tools used to test farming stuff.
         */
        define("farmkit", Privilege.ADMIN, "", "Provides a kit of various farming equipment."){player,_ ->
            for(item in farmKitItems){
                player.inventory.add(Item(item))
            }
        }

        define("cleardiary", Privilege.ADMIN) { player, _ ->
            for (type in DiaryType.values()) {
                val diary = player.achievementDiaryManager.getDiary(type)
                for (level in 0 until diary.levelStarted.size) {
                    for (task in 0 until diary.taskCompleted[level].size) {
                        diary.resetTask(player, level, task)
                    }
                }
            }
            sendMessage(player, "All achievement diaries cleared successfully.")
        }

        define("region", Privilege.STANDARD, "", "Prints your current Region ID.") {player, args ->
            sendMessage(player, "Region ID: ${player.viewport.region.regionId}")
        }

        define("spellbook", Privilege.ADMIN, "::spellbook <lt>book ID<gt> (0 = MODERN, 1 = ANCIENTS, 2 = LUNARS)", "Swaps your spellbook to the given book ID."){player, args ->
            if(args.size < 2){
                reject(player,"Usage: ::spellbook [int]. 0 = MODERN, 1 = ANCIENTS, 2 = LUNARS")
            }
            val spellBook = SpellBookManager.SpellBook.values()[args[1].toInt()]
            player.spellBookManager.setSpellBook(spellBook)
            player.spellBookManager.update(player)
        }

        define("killme", Privilege.ADMIN, "", "Does exactly what it says on the tin.") { player, _ ->
            player.impactHandler.manualHit(player, player.skills.lifepoints, HitsplatType.NORMAL)
        }

        define("struct") {player, args ->
            val mapId = args[1].toIntOrNull() ?: return@define

            val def = Struct.get(mapId)
            SystemLogger.logInfo(this::class.java, def.toString())
        }

        define("datamap") {player, args ->
            val mapId = args[1].toIntOrNull() ?: return@define

            val def = DataMap.get(mapId)
            SystemLogger.logInfo(this::class.java, def.toString())
        }

        define("dumpstructs", Privilege.ADMIN, "", "Dumps all the cache structs to structs.txt") {player, _ ->
            val dump = File("structs.txt")
            val writer = BufferedWriter(FileWriter(dump))
            val index = Cache.getIndexes()[2]
            val containers = index.information.containers[26].filesIndexes
            for(fID in containers)
            {
                val file = index.getFileData(26, fID)
                if(file != null){
                    val def = Struct.parse(fID, file)
                    if(def.dataStore.isEmpty()) continue //no data in struct.
                    writer.write(def.toString())
                    writer.newLine()
                }
            }
            writer.flush()
            writer.close()
        }

        define("dumpdatamaps", Privilege.ADMIN, "", "Dumps all the cache data maps to datamaps.txt") {player, _ ->
            val index = Cache.getIndexes()[17]
            val containers = index.information.containersIndexes

            val dump = File("datamaps.txt")
            val writer = BufferedWriter(FileWriter(dump))

            for(cID in containers)
            {
                val fileIndexes = index.information.containers[cID].filesIndexes
                for(fID in fileIndexes)
                {
                    val file = index.getFileData(cID, fID)
                    if(file != null){
                        val def = DataMap.parse((cID shl 8) or fID, file)
                        if(def.keyType == '?') continue //Empty definition - only a 0 present in the cachefile data.
                        writer.write(def.toString())
                        writer.newLine()
                    }
                }
            }
            writer.flush()
            writer.close()
        }

        define("rolldrops", Privilege.ADMIN, "::rolldrops <lt>NPC ID<gt> <lt>AMOUNT<gt>", "Rolls the given NPC drop table AMOUNT times.") { player: Player, args: Array<String> ->
            if(args.size < 2){
                reject(player,"Usage: ::rolldrops npcid amount")
            }

            val container = player.dropLog
            val npcId = args[1].toInt()
            val amount = args[2].toInt()

            container.clear()

            for(i in 0..amount)
            {
                val drops = NPCDefinition.forId(npcId).dropTables.table.roll()
                for(drop in drops) container.add(drop)
            }

            container.open(player)
        }

        define("varbits", Privilege.ADMIN, "::varbits <lt>Varp ID<gt>", "Lists all the varbits assigned to the given varp.") { player, args ->
            if(args.size < 2)
                reject(player, "Usage: ::varbits varpIndex")

            val varp = args[1].toIntOrNull() ?: reject(player, "Please use a valid int for the varpIndex.")
            GlobalScope.launch {
                sendMessage(player, "========== Found Varbits for Varp $varp ==========")
                for(id in 0 until 10000)
                {
                    val def = VarbitDefinition.forId(id)
                    if(def.varpId == varp)
                    {
                        sendMessage(player, "${def.id} -> [offset: ${def.startBit}, upperBound: ${def.endBit}]")
                    }
                }
                sendMessage(player, "=========================================")
            }
        }

        define("testpacket") { player, _ ->
            PacketWriteQueue.write(ResetInterface(), PlayerContext(player))
        }
    }
}