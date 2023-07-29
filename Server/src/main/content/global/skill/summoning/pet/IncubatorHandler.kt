package content.global.skill.summoning.pet

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.interaction.*
import core.tools.StringUtils

class IncubatorHandler : InteractionListener {
    val eggIds = IncubatorEgg.values().map { it.egg.id }.toIntArray()
    val incubators = intArrayOf(28550, 28352, 28359)

    override fun defineListeners() {
        on (incubators, IntType.SCENERY, "inspect", handler = ::handleInspectOption)
        on (incubators, IntType.SCENERY, "take-egg", handler = ::handleTakeOption)
        onUseWith (IntType.SCENERY, eggIds, *incubators, handler = ::handleEggOnIncubator)
    }

    fun handleEggOnIncubator (player: Player, used: Node, with: Node) : Boolean {
        val egg = IncubatorEgg.forItem (used.asItem()) ?: return false
        val activeEgg = IncubatorTimer.getEggFor (player, player.location.regionId)
        
        if (activeEgg != null) {
            sendMessage (player, "You already have an egg in this incubator.")
            return true
        }
        
        if (removeItem(player, used.asItem()))
            IncubatorTimer.registerEgg (player, player.location.regionId, egg)
        return true
    }

    fun handleInspectOption (player: Player, node: Node) : Boolean {
        val activeEgg = IncubatorTimer.getEggFor (player, player.location.regionId)

        if (activeEgg == null) {
            sendMessage (player, "The incubator is currently empty.")
            return true
        }

        if (activeEgg.finished) {
            sendMessage (player, "The egg inside has finished incubating.")
            return true
        }

        val creatureName = activeEgg.egg.product.name.lowercase()
        sendMessage (player, "There is currently ${if (StringUtils.isPlusN(creatureName)) "an" else "a"} $creatureName egg incubating.")
        return true
    }

    fun handleTakeOption (player: Player, node: Node) : Boolean {
        val region = player.location.regionId
        val activeEgg = IncubatorTimer.getEggFor (player, region) ?: return false

        if (!activeEgg.finished) {
            sendMessage (player, "That egg hasn't finished incubating!")
            return true
        }

        if (freeSlots(player) < 1) {
            sendMessage (player, "You do not have enough inventory space to do that.")
            return true
        }

        val egg = IncubatorTimer.removeEgg (player, region) ?: return false
        val product = egg.product
        val name = product.name.lowercase()

        sendMessage(player, "You take your $name out of the incubator.")
        addItem(player, product.id)
        return true
    }
}
