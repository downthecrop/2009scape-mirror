package content.global.ame.events.certer

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.emote.Emotes
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import core.api.animate
import core.api.lock
import core.api.utils.WeightBasedTable

class CerterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.GILES_2538) {
    lateinit var phrases: Array<String>

    override fun tick() {
        sayLine(this, phrases, true, true)
        if (ticksLeft == 2) {
            lock(player, 2)
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        player.setAttribute("random:pause", true)
        player.dialogueInterpreter.open(CerterDialogue(true),npc)
    }

    override fun init() {
        super.init()
        phrases = arrayOf(
            "Greetings ${player.username}, I need your help.",
            "ehem... Hello ${player.username}, please talk to me!",
            "Hello, are you there ${player.username}?",
            "It's really rude to ignore someone, ${player.username}!",
            "No-one ignores me!"
        )
        player.setAttribute("random:pause", false)
        player.setAttribute("certer:reward", false)
        animate(this, Emotes.BOW.animation, true)
    }

    override fun onTimeUp() {
        noteAndTeleport()
        terminate()
    }
}