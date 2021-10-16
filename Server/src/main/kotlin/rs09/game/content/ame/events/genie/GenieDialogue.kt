package rs09.game.content.ame.events.genie

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class GenieDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val assigned = player!!.getAttribute("genie:item",0)
        npc("Ah, so you are there, @name. I'm so glad you summoned me.", "Please take this lamp and make your wish.")
        if(!player!!.inventory.add(Item(assigned))){
            GroundItemManager.create(Item(assigned),player)
        }
        player!!.antiMacroHandler.event?.terminate()
        stage = END_DIALOGUE
    }
}