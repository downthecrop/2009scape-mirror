package content.global.handlers.npc

import core.api.*
import content.data.consumables.Consumables
import core.game.consumable.Potion
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class DecantListener : InteractionListener {

    override fun defineListeners() {
        on(IntType.NPC,"decant"){ player, node ->
            val (toRemove, toAdd) = decantContainer(player.inventory)
            for (item in toRemove)
                removeItem(player, item)
            for (item in toAdd)
                addItem(player, item.id, item.amount)
            player.dialogueInterpreter.open(DecantingDialogue(),node.asNpc())
            return@on true
        }
    }

    internal class DecantingDialogue : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage++){
                0 -> npc("There you go!")
                1 -> player("Thanks!").also { stage = END_DIALOGUE }
            }
        }
    }

}
