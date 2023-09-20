package content.global.ame.events.genie

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import core.api.playAudio
import core.api.utils.WeightBasedTable
import org.rs09.consts.Sounds

class GenieNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.GENIE_409) {
    val phrases = arrayOf("Greetings, @name!","Ehem... Master @name?","Are you there, Master @name?","No one ignores me!")

    override fun tick() {
        if(RandomFunction.random(1,15) == 5){
            sendChat(phrases.random().replace("@name",player.username.capitalize()))
        }
        super.tick()
    }

    override fun init() {
        super.init()
        playAudio(player, Sounds.GENIE_APPEAR_2301)
        sendChat(phrases.random().replace("@name",player.username.capitalize()))
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(GenieDialogue(),npc)
    }
}
