package content.global.ame.events.drunkendwarf

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class DrunkenDwarfNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.DRUNKEN_DWARF_956) {
    lateinit var phrases: Array<String>
    private var attackDelay = 0

    override fun init() {
        super.init()
        phrases = arrayOf(
            "'Ello der ${player.username}! *hic*",
            "Oi, are you der ${player.username}!",
            "Dunt ignore your matey!",
            "Aww comeon, talk to ikle me ${player.username}!",
            "I hates you, ${player.username}!"
        )
    }

    override fun tick() {
        sayLine(this, phrases, true, true)
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (attackDelay <= getWorldTicks()) {
                this.attack(player)
            }
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        attackDelay = getWorldTicks() + 10
        this.pulseManager.clear()
        openDialogue(player, DrunkenDwarfDialogue(), this.asNpc())
    }

    override fun onTimeUp() {
        if (attackDelay <= getWorldTicks()) {
            this.attack(player)
        }
    }
}