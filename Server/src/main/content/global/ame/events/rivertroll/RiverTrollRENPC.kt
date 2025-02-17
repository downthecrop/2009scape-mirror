package content.global.ame.events.rivertroll

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable

val ids = (391..396).toList()

class RiverTrollRENPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(391){
    override fun talkTo(npc: NPC) {}
    override fun init() {
        super.init()
        val id = idForCombatLevel(ids, player)
        this.transform(id)
        this.attack(player)
        sendChat("Fishies be mine, leave dem fishies!")
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