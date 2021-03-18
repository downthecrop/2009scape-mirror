package rs09.game.content.ame.events.certer

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import rs09.game.content.ame.RandomEventNPC
import rs09.game.content.global.WeightBasedTable

class CerterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.GILES_2538) {
    override fun tick() {
        super.tick()
        if(RandomFunction.random(1,10) == 5) sendChat(player.username.capitalize() + "! I need your assistance.")
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(CerterDialogue(true),npc)
    }

    override fun init() {
        super.init()
        sendChat(player.username.capitalize() + "! I need your assistance.")
    }
}