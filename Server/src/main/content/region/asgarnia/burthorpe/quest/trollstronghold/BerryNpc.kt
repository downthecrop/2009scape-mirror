package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.data.Quests
import core.api.isQuestInProgress
import core.api.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class BerryNpc(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BerryNpc(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERRY_1127, NPCs.BERRY_1129)
    }

    // Transform from sleeping to awake.
    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        if (this.id == NPCs.BERRY_1129) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.BERRY_1127)
            this.skills.lifepoints = prevLifePoints
        }
        return attackable
    }

    override fun handleTickActions() {
        super.handleTickActions()
        // When returning to spawn, transform back to sleeping and keep lifepoints.
        if (getAttribute("return-to-spawn", false) && this.id == NPCs.BERRY_1127) {
            val prevLifePoints = this.skills.lifepoints
            this.transform(NPCs.BERRY_1129)
            this.skills.lifepoints = prevLifePoints
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (isQuestInProgress(killer as Player, Quests.TROLL_STRONGHOLD, 8, 10)) {
            produceGroundItem(killer, Items.CELL_KEY_2_3137, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}