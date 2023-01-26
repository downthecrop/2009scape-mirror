package content.global.ame.events.zombie

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable


val ids = 419..424

class ZombieRENPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(419){
    override fun talkTo(npc: NPC) {}
    override fun init() {
        super.init()
        val index = (player.properties.combatLevel / 20) - 1
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Brainsssss!")
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