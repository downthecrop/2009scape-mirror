package core.game.node.entity.npc

import core.game.node.item.Item
import core.api.ContentInterface
import core.game.node.entity.Entity
import core.game.world.map.RegionManager
import core.game.node.entity.player.Player
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.world.map.path.ClipMaskSupplier
import core.game.world.map.path.Pathfinder

open class NPCBehavior(vararg val ids: Int = intArrayOf()) : ContentInterface {
    companion object {
        private val idMap = HashMap<Int,NPCBehavior>()
        private val defaultBehavior = NPCBehavior()

        @JvmStatic fun forId(id: Int) : NPCBehavior {
            return idMap[id] ?: defaultBehavior
        }
        fun register(ids: IntArray, behavior: NPCBehavior){
            ids.forEach { idMap[it] = behavior }
        }
    }
    
    object StandardClipMaskSupplier : ClipMaskSupplier {
        override fun getClippingFlag (z: Int, x: Int, y: Int) : Int {
            return RegionManager.getClippingFlag(z,x,y)
        }
    }

    /**
     * Called every tick, before the base NPC tick() method.
     * @param self the NPC instance this behavior belongs to
     * @return whether we should proceed with the base NPC tick() method - e.g. returning false means we do not proceed with a normal NPC tick.
     */
    open fun tick(self: NPC): Boolean {
        return true
    }

    /**
     * Called before this NPC receives damage, and allows you to adjust the battlestate if needed.
     * @param self the NPC instance this behavior belongs to
     * @param attacker the entity attacking this NPC
     * @param state the current state of the combat between this NPC and the attacker.
     */
    open fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {}

    /**
     * Called after this NPC receives damage, and allows you to adjust the battlestate if needed.
     * @param self the NPC instance this behavior belongs to
     * @param attacker the entity attacking this NPC
     * @param state the current state of the combat between this NPC and the attacker.
     */
    open fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {}

    /**
     * Called after this NPC's basic attack has been calculated, but before it is finalized, so adjustments can be made.
     * @param self the NPC instance this behavior belongs to
     * @param victim the entity this NPC is attacking
     * @param state the state of combat between this NPC and the victim.
     */
    open fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {}

    /**
     * Called when this NPC is being removed from the game world.
     * Note: This is not the same as death. Death does not remove an NPC, unless that NPC cannot respawn.
     * @param self the NPC instance this behavior belongs to
     */
    open fun onRemoval(self: NPC) {}

    /**
     * Called when this NPC is first created and spawned into the game world.
     * Note: This is not the same as respawning.
     * @param self the NPC instance this behavior belongs to
     */
    open fun onCreation(self: NPC) {}

    /**
     * Called when this NPC respawns after being killed.
     * @param self the NPC instance this behavior belongs to
     */
    open fun onRespawn(self: NPC) {}

    /**
     * Called immediately when the NPC first begins to die (on the same tick that the death animation begins)
     * @param self the NPC instance this behavior belongs to
     * @param killer the entity which killed this NPC.
     */
    open fun onDeathStarted(self: NPC, killer: Entity) {}

    /**
     * Called immediately after the death animation of this NPC has finished, on the same tick that drop tables are rolled.
     * @param self the NPC instance this behavior belongs to
     * @param killer the entity which killed this NPC.
     */
    open fun onDeathFinished(self: NPC, killer: Entity) {}

    /**
     * Called after this NPC's drop table is rolled, but before the items are actually dropped, so the list can be manipulated.
     * @param self the NPC instance this behavior belongs to
     * @param killer the killer of the npc
     * @param drops the generated list of drops for this roll of the table
     */
    open fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {}

    /**
     * Called by combat-related code to check if this NPC can be attacked by the `attacker` entity.
     * @param self the NPC instance this behavior belongs to
     * @param attacker the entity attempting to attack this NPC
     * @param style the combat style the attacker is attempting to use
     * @param shouldSendMessage whether the core combat code believes you should send a message e.g. "You can't attack this NPC with that weapon"
     * @return whether the attacker should be able to attack this NPC.
     */
    open fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean) : Boolean {
        if (attacker is Player && !self.definition.hasAction("attack"))
            return false
        return true
    }

    /**
     * Called by combat-related code to check if this NPC should ignore multi-combat rules when attempting to attack the given victim.
     * @param self the NPC instance this behavior belongs to
     * @param victim the entity that is being considered for attack.
     * @return whether we should ignore the rules of multi-way combat for the given entity.
     */
    open fun shouldIgnoreMultiRestrictions(self: NPC, victim: Entity) : Boolean {return false}

    /**
     * Called by combat-related code to allow the combat handler to be overridden
     * @param self the NPC instance this behavior belongs to
     * @param original the default swing handler this NPC would have used
     * @return the SwingHandler instance to be used for this cycle of combat
     */
    open fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler) : CombatSwingHandler {return original}

    /**
     * Called by MovementPulse to determine if a non-default Pathfinder should be used (e.g. whether this npc should intelligently walk around obstacles)
     */
    open fun getPathfinderOverride(self: NPC): Pathfinder? {
        return null
    }

    /**
     * Called by pathfinding code to determine the clipping mask supplier this NPC should use. You can use this to ignore water, etc.
    */
    open fun getClippingSupplier(self: NPC) : ClipMaskSupplier? {
        return StandardClipMaskSupplier
    }

    /**
     * Modifies the combat experience gained from killing an entity
     * @param self the NPC instance this behavior belongs to
     * @param attacker entity attacking the npc
     * @return multiplier for final xp gain
     */
    open fun getXpMultiplier(self: NPC, attacker: Entity) : Double {
        return 1.0
    }
}
