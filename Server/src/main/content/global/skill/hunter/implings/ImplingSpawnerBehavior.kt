package content.global.skill.hunter.implings

import core.api.*
import core.tools.*
import core.game.node.entity.npc.*
import core.game.world.map.path.ClipMaskSupplier

/**
 * Manages the behavior for impling spawners (the invisible NPCs that turn into random implings).
*/
class ImplingSpawnerBehavior : NPCBehavior (*ImplingSpawner.getIds()) {
    override fun onCreation (self: NPC) {
        val isPuro = isPuroSpawner(self)
        val delay = if (isPuro) 120 else 180
        setAttribute(self, "transformTime", getWorldTicks() + secondsToTicks(delay))
        self.setRespawnTicks(3)
        self.isRespawn = isPuro
        self.walkRadius = if (isPuro) 20 else 100
        self.isWalks = true
        self.isNeverWalks = false
        self.setInvisible(true)
    }

    override fun onRespawn (self: NPC) {
        if (!isPuroSpawner(self))
            log (this::class.java, Log.ERR, "Non-puro spawner has respawned!")
        this.onCreation(self)
    }

    override fun tick (self: NPC) : Boolean {
        var transformTime = getAttribute(self, "transformTime", 0)
        if (transformTime != 0 && transformTime <= getWorldTicks()) {
            val table = ImplingSpawner.forId(self.id)?.table ?: return ImplingController.deregister(self)
            val impling = table.roll() ?: return ImplingController.deregister(self)
            val targetId = if (isPuroSpawner(self)) impling.puroId else impling.npcId
            self.transform(targetId)
            self.behavior = forId(self.id)
            self.setInvisible(false)
            removeAttribute(self, "transformTime")
            sendGraphics(1119, self.location)
        }
        return true
    }

    override fun getClippingSupplier (self: NPC) : ClipMaskSupplier {
        return ImplingClipper
    }

    private fun isPuroSpawner(self: NPC) : Boolean {
        return self.id > 6000
    }
}
