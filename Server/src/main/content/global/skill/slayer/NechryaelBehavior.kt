package content.global.skill.slayer

import core.api.animate
import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class NechryaelBehavior : NPCBehavior(*Tasks.NECHRYAELS.npcs) {
    private val ATTR_SPAWNS = "deathSpawns"
    private val ATTR_NEXTSPAWN = "deathSpawnNextTick"

    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker !is Player) return
        if (!canSpawnDeathspawn(self)) return
        if (!RandomFunction.roll(5)) return
        spawnDeathSpawn(self, attacker)
    }

    fun spawnDeathSpawn(self: NPC, player: Player) {
        val npc = NPC.create(NPCs.DEATH_SPAWN_1614, self.location.transform(self.direction, 1))
        setAttribute(npc, "parent", self)
        setAttribute(npc, "target", player)
        npc.isRespawn = false
        npc.init()
        addSpawn(self, npc)
        setNextSpawn(self)
        animate(self, 9491)
    }

    fun canSpawnDeathspawn(self: NPC) : Boolean {
        if (getSpawns(self).size >= 2) {
            setNextSpawn(self)
            return false
        }
        return getNextSpawn(self) <= GameWorld.ticks
    }

    fun getNextSpawn(self: NPC) : Int {
        return getAttribute(self, ATTR_NEXTSPAWN, 0)
    }

    fun setNextSpawn(self: NPC) {
        setAttribute(self, ATTR_NEXTSPAWN, GameWorld.ticks + 50)
    }

    fun getSpawns(self: NPC) : ArrayList<NPC> {
        return getAttribute(self, ATTR_SPAWNS, ArrayList())
    }

    fun addSpawn(self: NPC, spawn: NPC) {
        val list = getSpawns(self)
        list.add(spawn)
        setAttribute(self, ATTR_SPAWNS, list)
    }

    fun removeSpawn(self: NPC, spawn: NPC) {
        val list = getSpawns(self)
        list.remove(spawn)
        setAttribute(self, ATTR_SPAWNS, list)
    }

    override fun shouldIgnoreMultiRestrictions(self: NPC, victim: Entity): Boolean {
        val list = getSpawns(self)
        return victim == self.properties.combatPulse.getVictim() || list.contains(victim.properties.combatPulse.getVictim())
    }
}

class DeathspawnBehavior : NPCBehavior(NPCs.DEATH_SPAWN_1614) {
    override fun onCreation(self: NPC) {
        setAttribute(self, "despawn-time", GameWorld.ticks + 100)
        val target = getAttribute<Player?>(self, "target", null) ?: return
        self.attack(target)
    }

    override fun onRemoval(self: NPC) {
        val parent = getAttribute<NPC?>(self, "parent", null) ?: return
        (parent.behavior as? NechryaelBehavior)?.let { it.removeSpawn (parent, self) }
    }

    override fun tick(self: NPC): Boolean {
        val target = getAttribute<Player?>(self, "target", null) ?: return true

        if (!target.isActive || DeathTask.isDead(target) || getAttribute(self, "despawn-time", 0) <= GameWorld.ticks)
            self.clear()
        return true
    }

    override fun shouldIgnoreMultiRestrictions(self: NPC, victim: Entity): Boolean {
        return victim == getAttribute<Player?>(self, "target", null)
    }

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        return attacker == getAttribute<Player?>(self, "target", null)
    }
}
