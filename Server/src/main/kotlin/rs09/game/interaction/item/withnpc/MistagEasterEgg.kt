package rs09.game.interaction.item.withnpc

import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

class MistagEasterEgg : InteractionListener() {
    val DIAMOND = Items.DIAMOND_1601
    val MISTAG = NPCs.MISTAG_2084
    val ZANIK_RING = 14649

    override fun defineListeners() {
        onUseWith(NPC,DIAMOND,MISTAG){player, _, with ->
            val alreadyHasRing = player.inventory.contains(ZANIK_RING,1) || player.bank.contains(ZANIK_RING,1) || player.equipment.contains(ZANIK_RING,1)
            player.dialogueInterpreter.open(MistagEasterEggDialogue(alreadyHasRing),with.asNpc())
            return@onUseWith true
        }

        onEquip(ZANIK_RING){player,_ ->
            player.appearance.transformNPC(NPCs.ZANIK_3712)
            return@onEquip true
        }

        onUnequip(ZANIK_RING){player, _ ->
            player.appearance.transformNPC(-1)
            return@onUnequip true
        }
    }
}

class MistagEasterEggDialogue(val hasRing: Boolean): DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {

        if(hasRing){
            npc("Lovely gem, adventurer, but I have nothing for you.").also { stage = END_DIALOGUE }
            return
        }

        when(stage++){
            0 -> npc("Well thank you adventurer! Here, take this.")
            1 -> {
                end()
                if(player!!.inventory.remove(Item(Items.DIAMOND_1601))){
                    player!!.inventory.add(Item(14649))
                }
            }
        }
    }

    override fun npc(vararg messages: String?): Component? {
        return super.npc(FacialExpression.OLD_HAPPY,*messages)
    }
}