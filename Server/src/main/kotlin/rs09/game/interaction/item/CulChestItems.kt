package rs09.game.interaction.item

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

    fun alchemize(player: Player, node: Node){
        val amount = player.inventory.getAmount(node.id) + player.equipment.getAmount(node.id)
        val coins = amount * node.asItem().definition.value

        player.inventory.remove(Item(node.id,player.inventory.getAmount(node.id)))
        player.equipment.remove(Item(node.id))
        if(!player.inventory.add(Item(995,coins))){
            GroundItemManager.create(Item(995,coins),player)
        }

        player.dialogueInterpreter.sendDialogue("The item instantly alchemized itself!")
    }
}