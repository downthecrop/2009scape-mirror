package rs09.game.content.global.worldevents.holiday.easter

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.item.GroundItemManager
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class EasterEventListeners : InteractionListener() {

    val EGG_ATTRIBUTE = "/save:easter:eggs"
    val eggs = intArrayOf(Items.EASTER_EGG_11027, Items.EASTER_EGG_11028, Items.EASTER_EGG_11029, Items.EASTER_EGG_11030)

    override fun defineListeners() {

        on(eggs,ITEM,"take"){player, node ->
            player.pulseManager.run(object : MovementPulse(player,node, DestinationFlag.ITEM){
                override fun pulse(): Boolean {
                    if(player.inventory.contains(Items.BASKET_OF_EGGS_4565,1) || player.equipment.contains(Items.BASKET_OF_EGGS_4565,1)){
                        val item = GroundItemManager.get(node.id,node.location,null)
                        GroundItemManager.destroy(item)
                        player.incrementAttribute(EGG_ATTRIBUTE)
                        player.sendMessage("You place the egg in the basket. You now have ${player.getAttribute<Int>(EGG_ATTRIBUTE,0)} eggs!")
                    } else {
                        player.sendMessage("You need to have your egg basket with you to collect these.")
                    }
                    return true
                }
            })
            return@on true
        }

        on(Items.BASKET_OF_EGGS_4565,ITEM,"operate"){player, node ->
            val numEggs = player.getAttribute<Int>(EGG_ATTRIBUTE) ?: 0
            player.dialogueInterpreter.sendDialogue("You have $numEggs eggs in the basket.")
            return@on true
        }

        on(NPCs.EASTER_BUNNY_7197,NPC,"talk-to"){player, node ->
            val npc = node.asNpc()
            npc.faceLocation(player.location)
            val NEED_BASKET = !(player!!.inventory.contains(Items.BASKET_OF_EGGS_4565,1) || player!!.equipment.contains(Items.BASKET_OF_EGGS_4565,1) || player!!.bank.contains(Items.BASKET_OF_EGGS_4565,1))
            player.dialogueInterpreter.open(EasterBunnyDialogueFile(NEED_BASKET),npc)
            return@on true
        }

    }

}