package content.global.skill.hunter.implings

import core.api.*
import core.tools.*
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier
import core.game.node.entity.npc.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.item.Item
import core.game.node.entity.combat.CombatStyle
import content.global.skill.magic.spellconsts.Modern

/**
 * Manages the behavior for implings themselves.
*/
class ImplingBehavior : NPCBehavior (*Impling.getIds()) {
    override fun onCreation (self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
    }

    override fun tick(self: NPC) : Boolean {
        if (RandomFunction.roll(10))
            sendChat(self, "Tee-hee!")
        return true
    }

    override fun onRespawn(self: NPC) {
        if (!isPuroImpling(self))
            log (this::class.java, Log.ERR, "Non-puro impling has respawned!")
        sendGraphics(1119, self.properties.teleportLocation)
    }

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean) : Boolean {
        if (attacker !is Player) return false

        if (style != CombatStyle.MAGIC) {
            if (shouldSendMessage)
                sendMessage (attacker, "You can't do that.")
            return false
        }

        val spellBook = attacker.spellBookManager.spellBook
        if (spellBook != SpellBookManager.SpellBook.MODERN.interfaceId) {
            if (shouldSendMessage)
                sendMessage (attacker, "The impling is too fast for that.")
            return false
        }

        val spellId = attacker.properties.spell.spellId
        if (spellId != Modern.BIND && spellId != Modern.SNARE && spellId != Modern.ENTANGLE) {
            if (shouldSendMessage)
                sendMessage (attacker, "The impling is immune to that magic.")
            return false
        }

        return true
    }

    override fun onDeathFinished (self: NPC, killer: Entity) {
        if (!isPuroImpling(self))
            ImplingController.deregister(self)
        else if (self.originalId != self.id) { //if this is a spawner that transformed
            self.reTransform() //turn back into spawner NPC
            self.behavior = forId(self.id)
        }
    }

    //manually clear any rolled drops so any drops set by JSON are properly ignored.
    override fun onDropTableRolled (self: NPC, killer: Entity, drops: ArrayList<Item>) {
        drops.clear()
    }

    override fun getClippingSupplier (self: NPC) : ClipMaskSupplier {
        return ImplingClipper
    }

    private fun isPuroImpling (self: NPC) : Boolean {
        return self.id > 6054
    }
}
