package rs09.game.system.command.sets

import api.sendMessage
import core.cache.Cache
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.drop.NPCDropTables
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.item.Item
import core.net.packet.context.PlayerContext
import core.net.packet.out.ResetInterface
import core.net.packet.out.Varbit
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Items
import rs09.game.content.global.NPCDropTable
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.net.packet.PacketWriteQueue

@Initializable
class DevelopmentCommandSet : CommandSet(Privilege.ADMIN) {
    val farmKitItems = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325)

    override fun defineCommands() {

        /**
         * Gives the player a set of tools used to test farming stuff.
         */
        define("farmkit"){player,_ ->
            for(item in farmKitItems){
                player.inventory.add(Item(item))
            }
        }

        define("spellbook"){player, args ->
            if(args.size < 2){
                reject(player,"Usage: ::spellbook [int]. 0 = MODERN, 1 = ANCIENTS, 2 = LUNARS")
            }
            val spellBook = SpellBookManager.SpellBook.values()[args[1].toInt()]
            player.spellBookManager.setSpellBook(spellBook)
            player.spellBookManager.update(player)
        }

        define("killme") { player, _ ->
            player.impactHandler.manualHit(player, player.skills.lifepoints, HitsplatType.NORMAL)
        }

        define("rolldrops") { player: Player, args: Array<String> ->
            if(args.size < 2){
                reject(player,"Usage: ::rolldrops npcid amount")
            }

            val container = player.dropLog
            val npcId = args[1].toInt()
            val amount = args[2].toInt()

            for(i in 0..amount)
            {
                val drops = NPCDefinition.forId(npcId).dropTables.table.roll()
                for(drop in drops) container.add(drop)
            }

            container.open(player)
        }

        define("varbits") { player, args ->
            if(args.size < 2)
                reject(player, "Usage: ::list_varbits varpIndex")

            val varp = args[1].toIntOrNull() ?: reject(player, "Please use a valid int for the varpIndex.")
            GlobalScope.launch {
                sendMessage(player, "========== Found Varbits for Varp $varp ==========")
                for(id in 0 until 10000)
                {
                    val def = VarbitDefinition.forId(id)
                    if(def.configId == varp)
                    {
                        sendMessage(player, "${def.id} -> [offset: ${def.bitShift}, upperBound: ${def.bitSize}]")
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