package rs09.game.content.ame.events.genie

import api.*
import core.game.content.dialogue.FacialExpression
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.ame.RandomEventManager
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class GenieDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val assigned = player!!.getAttribute("genie:item",0)
        npcl(FacialExpression.NEUTRAL, "Ah, so you are there, ${player!!.name.capitalize()}. I'm so glad you summoned me. Please take this lamp and make your wish.")
		addItemOrDrop(player!!, assigned)
        RandomEventManager.getInstance(player!!)!!.event?.terminate()
        stage = END_DIALOGUE
    }
}