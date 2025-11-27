package content.global.ame.events.genie

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import core.api.lock
import core.api.playAudio
import core.api.utils.WeightBasedTable
import org.rs09.consts.Sounds

class GenieNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.GENIE_409) {
    lateinit var phrases: Array<String>

    override fun tick() {
        sayLine(this, phrases, true, true)
        if (ticksLeft == 2) {
            lock(player, 2)
        }
        super.tick()
    }

    override fun init() {
        super.init()
        val honorific = if (player.isMale) "Master" else "Mistress"
        phrases = arrayOf(
            "Greetings, ${player.username}!",
            "Ehem... $honorific ${player.username}?",
            "Are you there, $honorific ${player.username}?",
            "No one ignores me!"
        )
        playAudio(player, Sounds.GENIE_APPEAR_2301)
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(GenieDialogue(),npc)
    }

    override fun onTimeUp() {
        noteAndTeleport()
        terminate()
    }
}
