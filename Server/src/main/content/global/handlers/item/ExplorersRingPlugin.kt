package content.global.handlers.item

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.json.simple.JSONObject
import org.rs09.consts.Items
import core.ServerStore
import core.ServerStore.Companion.getInt
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import content.global.skill.magic.modern.ModernListeners

class ExplorersRingPlugin : InteractionListener {

    val RINGS = intArrayOf(Items.EXPLORERS_RING_1_13560, Items.EXPLORERS_RING_2_13561, Items.EXPLORERS_RING_3_13562)
    val CABBAGE_PORT = Location.create(3051, 3291, 0)


    override fun defineListeners() {
        on(RINGS, IntType.ITEM, "run-replenish"){ player, node ->
            val charges = getStoreFile().getInt(player.username.toLowerCase() + ":run")
            if (charges >= getRingLevel(node.id)) {
                sendDialogue(player,"Your ring appears to have no more run energy recharges left for today.")
                return@on true
            }
            if (player.settings.runEnergy == 100.0) {
                sendMessage(player, "You are fully rested. You do not need to use the ring's power for the moment.")
                return@on true
            }
            player.settings.updateRunEnergy(-50.0)
            playAudio(player, 5035)

            getStoreFile()[player.username.toLowerCase() + ":run"] = charges + 1

            sendMessage(player,"You feel refreshed as the ring revitalises you and a charge is used up.")
            visualize(player, 9988, 1733)
            return@on true
        }

        //Sources Used
        // - https://www.youtube.com/watch?v=PEA0fkRK0-A&t=64s&pp=ygUcMjAwOSBydW5lc2NhcGUgZXhwbG9yZXIgcmluZw%3D%3D 
        // - https://runescape.wiki/w/Explorer%27s_ring?oldid=899927  
        on(RINGS, IntType.ITEM, "low-alchemy"){ player, _ ->
            if (!hasLevelStat(player, Skills.MAGIC, 21)) {
                sendMessage(player,"You need a Magic level of 21 in order to do that.")
                return@on true
            }
            val remaining = getStoreFile().getInt(player.username.lowercase() + ":alchs", 30)
            if (remaining <= 0) {
                sendMessage(player, "You have used up all of your charges for the day.")
                return@on true
            }
            sendDialogue(player, "Choose the item that you wish to convert to coins.")
            addDialogueAction (player) {_,_ -> 
                sendItemSelect (player, "Choose") { slot, optionIndex ->
                    val item = player.inventory[slot]
                    if (item == null)
                        return@sendItemSelect
                    if (!ModernListeners().alchemize(player, item, false, explorersRing = true))
                        return@sendItemSelect
                    getStoreFile()[player.username.lowercase() + ":alchs"] = remaining - 1
                }
            }
            return@on true
        }

        on(RINGS, IntType.ITEM, "cabbage-port"){ player, node ->
            teleport(player)
            return@on true
        }

        on(RINGS, IntType.ITEM, "operate", "rub"){ player, node ->
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
