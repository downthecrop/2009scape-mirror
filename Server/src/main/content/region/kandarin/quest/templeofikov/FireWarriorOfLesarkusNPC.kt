package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.NPCs

class FireWarriorOfLesarkusNPC(id: Int = 0, val player: Player?, location: Location? = null) : AbstractNPC(id, location) {
    var clearTime = 0
    // Technically not constructed this way since this is invoked on load.
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return FireWarriorOfLesarkusNPC(id, null, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRE_WARRIOR_OF_LESARKUS_277)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        // You have 240 ticks to kill this guy or touch the door again.
        if (clearTime++ > 240) {
            removeAttribute(player!!, TempleOfIkov.attributeWarriorInstance)
            poofClear(this)
        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        val player = entity.asPlayer()
                //attributeWarriorInstance
        return attackable
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker.asPlayer()
        val opponent = this
        println(state.ammunition)
        if (state.ammunition != null && state.ammunition.itemId == 78) {
            // Only ice arrows will damage warrior.
        } else {
            // If it is the wrong combat or ammunition, deny all damage.
            player.properties.combatPulse.stop()
            if (state.estimatedHit > -1) {
                state.estimatedHit = 0
            }
            if (state.secondaryHit > -1) {
                state.secondaryHit = 0
            }
            runTask(player, 0) {
                sendNPCDialogue(player, opponent.id, "Your puny weapons do nothing against me human! Come back when you can give me a real fight!", FacialExpression.ANGRY)
            }
        }
    }

    override fun finalizeDeath(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            removeAttribute(player, TempleOfIkov.attributeWarriorInstance)
            if(getQuestStage(player, TempleOfIkov.questName) == 3) {
                setQuestStage(player, TempleOfIkov.questName, 4)
            }
            super.finalizeDeath(player)
        }
    }
}