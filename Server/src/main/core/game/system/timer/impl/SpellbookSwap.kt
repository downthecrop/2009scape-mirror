package core.game.system.timer.impl

import core.api.Event.DialogueClosed
import core.api.Event.SpellCast
import core.api.Event.SpellbookChanged
import core.api.clearLogoutListener
import core.api.registerLogoutListener
import core.api.removeTimer
import core.game.event.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.SpellBookManager.SpellbookChangeSource
import core.game.system.timer.PersistTimer
import core.tools.minutesToTicks

/**
 * A timer dedicated to the handling of the Lunar spell Spellbook Swap.
 */
class SpellbookSwap : PersistTimer(runInterval = minutesToTicks(2), identifier = "spellbook:swap") {
	
	private val spellCastHook = object : EventHook<SpellCastEvent> {
		override fun process(entity : Entity, event : SpellCastEvent) {
			if (event.spellBook == SpellBook.LUNAR && event.spellId == 12) return  // Ignore spellbook swap itself
			revertSpellbook(entity)
			removeTimer<SpellbookSwap>(entity)
		}
	}
	
	private val spellBookChangeHook = object : EventHook<SpellbookChangeEvent> {
		override fun process(entity : Entity, event : SpellbookChangeEvent) {
			if (event.source != SpellbookChangeSource.SPELLBOOK_SWAP_CAST
					&& event.source != SpellbookChangeSource.SPELLBOOK_SWAP_RESTORE) {
				removeTimer<SpellbookSwap>(entity)
			}
		}
	}
	
	private val dialogueCloseHook = object : EventHook<DialogueCloseEvent> {
		override fun process(entity : Entity, event : DialogueCloseEvent) {
			if (event.dialogue?.getIds()?.contains(3264731) != true) return // Spellbook swap selection dialogue
			if (entity !is Player) return
			if (SpellBook.forInterface(entity.spellBookManager.spellBook) == SpellBook.LUNAR) {
				entity.removeAttribute("spell:runes")
				removeTimer<SpellbookSwap>(entity)
			}
		}
	}
	
	override fun run(entity : Entity) : Boolean {
		revertSpellbook(entity)
		return false
	}
	
	override fun onRegister(entity : Entity) {
		entity.hook(SpellCast, spellCastHook)
		entity.hook(SpellbookChanged, spellBookChangeHook)
		entity.hook(DialogueClosed, dialogueCloseHook)
		if (entity is Player) {
			registerLogoutListener(entity, "spellbook:swap") { player ->
				revertSpellbook(player)
			}
		}
	}
	
	override fun onRemoval(entity : Entity) {
		entity.unhook(spellCastHook)
		entity.unhook(spellBookChangeHook)
		entity.unhook(dialogueCloseHook)
		if (entity is Player) {
			clearLogoutListener(entity, "spellbook:swap")
		}
	}
	
	private fun revertSpellbook(entity : Entity) {
		if (entity !is Player) return
		if (SpellBook.forInterface(entity.spellBookManager.spellBook) == SpellBook.LUNAR) return
		// Clear autocast
		val weaponInterface = entity.getExtension<WeaponInterface>(WeaponInterface::class.java)
		if (weaponInterface != null && entity.properties.autocastSpell != null) {
			weaponInterface.selectAutoSpell(-1, true)
		}
		// Close autocast selection interface if it's still open
		if (entity.getAttribute("autocast_select", false)) {
			entity.removeAttribute("autocast_select")
			entity.removeAttribute("autocast_component")
			if (weaponInterface != null) {
				entity.interfaceManager.openTab(weaponInterface)
			}
		}
		entity.dispatch(SpellbookChangeEvent(
				SpellBook.forInterface(entity.spellBookManager.spellBook),
				SpellBook.LUNAR,
				SpellbookChangeSource.SPELLBOOK_SWAP_RESTORE))
		entity.spellBookManager.setSpellBook(SpellBook.LUNAR)
		entity.spellBookManager.update(entity)
	}
}
