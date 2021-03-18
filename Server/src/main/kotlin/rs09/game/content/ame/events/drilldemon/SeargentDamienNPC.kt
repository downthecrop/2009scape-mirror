package rs09.game.content.ame.events.drilldemon

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import rs09.game.content.ame.RandomEventNPC
import rs09.game.content.global.WeightBasedTable

class SeargentDamienNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.SERGEANT_DAMIEN_2790) {

    override fun init() {
        super.init()
        sendChat(player.username.capitalize() + "! Drop and give me 20!")
    }

    override fun tick() {
        super.tick()
        if(RandomFunction.random(1,10) == 5){
            sendChat(player.username.capitalize() + "! Drop and give me 20!")
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(SeargentDamienDialogue(),npc)
    }
}