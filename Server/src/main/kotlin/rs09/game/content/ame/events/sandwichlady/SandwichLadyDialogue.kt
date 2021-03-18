package rs09.game.content.ame.events.sandwichlady

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class SandwichLadyDialogue(val isChoice: Boolean) : DialogueFile() {
    val SANDWICH_INTERFACE = 297
    override fun handle(componentID: Int, buttonID: Int) {
        val assigned = player!!.getAttribute("sandwich-lady:item",0)
        val choice = player!!.getAttribute("sandwich-lady:choice",0)
        if(!isChoice) {
            when (stage) {
                0 -> npc("Have a ${ItemDefinition.forId(assigned).name.toLowerCase()}, dear.").also { stage++ }
                1 -> {
                    end()
                    player!!.interfaceManager.open(Component(SANDWICH_INTERFACE))
                }
            }
        } else {
            when(stage){
                0 -> if(choice != assigned){
                    npc!!.sendChat("That's not what I said you could have!")
                    player!!.impactHandler.manualHit(npc,3,ImpactHandler.HitsplatType.NORMAL)
                    player!!.antiMacroHandler.event?.terminate()
                } else {
                    npc("Here you are, dear. I hope you enjoy it!")
                    if(!player!!.inventory.add(Item(assigned))){
                        GroundItemManager.create(Item(assigned),player)
                    }
                    player!!.antiMacroHandler.event?.terminate()
                    stage = END_DIALOGUE
                }
            }
        }
    }
}