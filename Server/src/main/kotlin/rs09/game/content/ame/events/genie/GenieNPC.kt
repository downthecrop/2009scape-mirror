package rs09.game.content.ame.events.genie

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.ame.RandomEventNPC
import rs09.game.content.global.WeightBasedTable

class GenieNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.GENIE_409) {
    val phrases = arrayOf("Greetings, @name!","Ehem... Master @name?","Are you there, Master @name?","No one ignores me!")
    var assigned_item = 0
    val items = arrayOf(Items.LAMP_2528)

    override fun tick() {
        if(RandomFunction.random(1,15) == 5){
            sendChat(phrases.random().replace("@name",player.name.capitalize()))
        }
        super.tick()
    }

    override fun init() {
        super.init()
        assignItem()
        sendChat(phrases.random().replace("@name",player.name.capitalize()))
    }

    fun assignItem(){
        assigned_item = items.random()
        player.setAttribute("genie:item",assigned_item)
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(GenieDialogue(),npc)
    }
}