package core.game.interaction.item

import api.*
import core.Util
import core.cache.def.impl.ItemDefinition
import core.plugin.Initializable
import core.game.interaction.OptionHandler
import core.plugin.Plugin
import core.game.node.entity.skill.Skills
import core.game.interaction.item.ExplorersRingPlugin
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getBoolean
import rs09.ServerStore.Companion.getInt
import rs09.game.interaction.InteractionListener

/**
 * Handles the explorers ring.
 *
 * @author Vexia
 */
class ExplorersRingPlugin : InteractionListener() {

    val RINGS = intArrayOf(Items.EXPLORERS_RING_1_13560, Items.EXPLORERS_RING_2_13561, Items.EXPLORERS_RING_3_13562)
    val CABBAGE_PORT = Location.create(3051, 3291, 0)


    override fun defineListeners() {
        on(RINGS, ITEM, "run-replenish"){player, node ->
            val charges = getStoreFile().getInt(player.username.toLowerCase() + ":run")
            if (charges >= getRingLevel(node.id)) {
                sendMessage(player,"You have used all the charges you can for one day.")
                return@on true
            }
            player.settings.updateRunEnergy(-50.0)
            player.audioManager.send(5035)

            getStoreFile()[player.username.toLowerCase() + ":run"] = charges + 1

            sendMessage(player,"You feel refreshed as the ring revitalises you and a charge is used up.")
            visualize(player, 9988, 1733)
            return@on true
        }

        on(RINGS, ITEM, "low-alchemy"){player, _ ->
            if (!hasLevelStat(player, Skills.MAGIC, 21)) {
                sendMessage(player,"You need a Magic level of 21 in order to do that.")
                return@on true
            }
            if(getStoreFile().getBoolean(player.username.toLowerCase() + ":alchs")){
                sendMessage(player, "You have claimed all the charges you can for one day.")
                return@on true
            }
            sendMessage(player,"You grant yourself with 30 free low alchemy charges.") // todo this implementation is not correct, see https://www.youtube.com/watch?v=UbUIF2Kw_Dw

            getStoreFile()[player.username.toLowerCase() + ":alchs"] = true

            return@on true
        }

        on(RINGS, ITEM, "cabbage-port"){player, node ->
            teleport(player)
            return@on true
        }

        on(RINGS, ITEM, "operate", "rub"){player, node ->
            if(getRingLevel(node.id) < 3){
                sendMessage(player, "This item can not be operated.")
                return@on true
            }

            teleport(player)
            return@on true
        }
    }

    fun teleport(player: Player){
        teleport(player, CABBAGE_PORT, TeleportType.CABBAGE)
    }

    fun getRingLevel(id: Int): Int{
        return when(id){
            Items.EXPLORERS_RING_1_13560 -> 1
            Items.EXPLORERS_RING_2_13561 -> 2
            Items.EXPLORERS_RING_3_13562 -> 3
            else -> -1
        }
    }

    fun getStoreFile(): JSONObject{
        return ServerStore.getArchive("daily-explorer-ring")
    }
}
