package content.region.fremennik.lighthouse.quest.horror.handlers

import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import content.region.fremennik.lighthouse.quest.horror.JossikLighthouseDialogueFile
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository.getPlayerByName
import org.rs09.consts.NPCs

class DagonnothBabyNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DagonnothBabyNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAGANNOTH_1344, NPCs.DAGANNOTH_1345, NPCs.DAGANNOTH_1346, NPCs.DAGANNOTH_1347)
    }

    override fun handleTickActions() {
        super.handleTickActions()

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val target = getPlayerByName(targetName)

        // poofclear if target runs too far or leaves
        if (target == null
            || !target.isActive
            || !this.location.withinDistance(target.location, 25)
        ) {
            // clear the player's inCombat attribute
            if (target != null) {
                removeAttribute(target, HorrorFromTheDeep.HFTD_COMBAT)
                clearHintIcon(target)
            }

            // clear the NPC
            this.walkingQueue.reset()
            this.properties.combatPulse.stop()
            poofClear(this)
        } else {
            // keep attacking target
            attack(target)
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)

            // mark baby as dead
            setAttribute(killer, HorrorFromTheDeep.HFTD_BABY_DEAD, true)

            // not in combat
            clearHintIcon(killer)
            removeAttribute(killer, HorrorFromTheDeep.HFTD_COMBAT)

            // wait a moment, then talk to jossik to start mom fight
            queueScript(killer, 1, QueueStrength.SOFT) {
                face(findNPC(NPCs.JOSSIK_1335)).also {
                    openDialogue(killer, JossikLighthouseDialogueFile(), NPC(NPCs.JOSSIK_1335))
                }
                return@queueScript stopExecuting(killer)
            }
        }

        clear()
        super.finalizeDeath(killer)
    }

    // checks if player can attack this NPC
    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val target = getPlayerByName(targetName)

        if (entity is Player) {
            if (entity == target) {
                return true
            } else {
                sendMessage(entity, "It's not after you...")
            }
        }
        return false
    }

    // checks if this NPC can attack a target
    override fun canSelectTarget(target: Entity): Boolean {

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val yourTarget = getPlayerByName(targetName)

        // returns false if the target is not who the NPC is supposed to attack
        return target == yourTarget
    }
}