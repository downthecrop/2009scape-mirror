package content.global.ame.events.swarm

import content.global.ame.RandomEventNPC
import core.api.playGlobalAudio
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.tools.minutesToTicks
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class SwarmNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.SWARM_411) {
    override fun init() {
        super.init()
        this.setAttribute("no-spawn-return", true)
        this.attack(player)
        playGlobalAudio(this.location, Sounds.SWARM_APPEAR_818)
        this.ticksLeft = minutesToTicks(60)
    }

    override fun tick() {
        super.tick()
        if (!this.inCombat())
            this.attack(player)
    }

    override fun talkTo(npc: NPC) {
    }
}