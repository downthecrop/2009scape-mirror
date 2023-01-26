package content.global.ame.events.rockgolem

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import kotlin.math.max


val ids = 413..418

class RockGolemRENPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(413){
    override fun talkTo(npc: NPC) {}
    override fun init() {
        super.init()
        val index = max(0,(player.properties.combatLevel / 20) - 1)
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Raarrrgghh! Flee human!")
        this.isRespawn = false
    }
    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }
    override fun tick() {
        if(!player.location.withinDistance(this.location,8)){
            this.terminate()
        }
        super.tick()
        if(!player.viewport.currentPlane.npcs.contains(this)) this.clear()
    }
}