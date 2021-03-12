package rs09.game.content.quest.members.thelosttribe

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable

@Initializable
/**
 * Handles the cave goblin miner npc
 * @author Ceikry
 */
class CaveGoblinMinerNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location) {
    var mining = false
    var originallyMiner = false
    init {
        originallyMiner = id > 2073
    }

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return CaveGoblinMinerNPC(id,location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(2078,2077,2076,2075,2072,2071,2070,2069)
    }

    override fun tick() {
        mining = (id > 2074)

        if(properties.combatPulse.isAttacking && mining){
            transform(id - 6)
            this.isWalks = true
            this.walkRadius = 4
            this.isNeverWalks = false
        }
        super.tick()
    }

    override fun finalizeDeath(killer: Entity?) {
        if(!mining && originallyMiner){
            transform(id + 6)
            this.isWalks = false
            this.walkRadius = 0
            this.isNeverWalks = true
        }
        super.finalizeDeath(killer)
    }

}