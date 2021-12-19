package rs09.game.interaction.item

import api.*
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class CulChestItems: InteractionListener() {

    override fun defineListeners() {

        onEquip(Items.CLEAVER_7451){player, node ->
            alchemize(player,node)
        }

        onEquip(Items.MEAT_TENDERISER_7449){player, node ->
            alchemize(player,node)
        }

        onEquip(Items.GLOVES_7458){player, node ->
            alchemize(player,node)
        }

        onEquip(Items.GLOVES_7459){player, node ->
            alchemize(player,node)
        }

        onEquip(Items.GLOVES_7460){player, node ->
            alchemize(player,node)
        }

        onEquip(Items.GLOVES_7461){player,node ->
            alchemize(player,node)
        }

        onEquip(Items.GLOVES_7462){player, node ->
            alchemize(player,node)
        }

    }

    fun alchemize(player: Player, node: Node): Boolean{
        val amount = amountInInventory(player, node.id) + amountInEquipment(player, node.id)
        val coins = amount * itemDefinition(node.id).value

        removeAll(player, node.id, api.Container.INVENTORY)
        removeItem(player, node.id, api.Container.EQUIPMENT)
        addItemOrDrop(player, 995, coins)

        sendDialogue(player, "The item instantly alchemized itself!")
        return false //tell equip handler not to equip the gloves at all if they still even exist lel
    }
}