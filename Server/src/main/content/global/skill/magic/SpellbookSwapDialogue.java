package content.global.skill.magic;

import content.data.Quests;
import core.game.component.Component;
import core.game.dialogue.DialoguePlugin;
import core.game.event.SpellbookChangeEvent;
import core.game.interaction.Listener;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.SpellBookManager.SpellbookChangeSource;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.plugin.Initializable;

import java.util.ArrayList;

import static core.api.ContentAPIKt.hasRequirement;


/**
 * Handles the SpellbookSwapDialogue dialogue.
 * @author Vexia
 * 
 */
@Initializable
public class SpellbookSwapDialogue extends DialoguePlugin {
	
	/**
	 * If we're using the perk.
	 */
	private boolean perk;

	/**
	 * Constructs a new {@Code SpellbookSwapDialogue} {@Code Object}
	 */
	public SpellbookSwapDialogue() {
		/*
		 * empty.
		 */
	}	

	/**
	 * Constructs a new {@Code SpellbookSwapDialogue} {@Code Object}
	 * @param player the player.
	 */
	public SpellbookSwapDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SpellbookSwapDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		if (args.length > 1) {
			perk = true;
			interpreter.sendOptions("Select a Spellbook", "Modern", "Ancient", "Lunar");
			return true;
		}
		interpreter.sendOptions("Select a Spellbook", "Ancient", "Modern");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (perk) {
				SpellBook book = SpellBook.values()[buttonId - 1];
				player.getSpellBookManager().setSpellBook(book);
				player.getInterfaceManager().openTab(new Component(book.getInterfaceId()));
				end();
				return true;
			}
			int type = 0;
			switch (buttonId) {
			case 1:
				type = 1;
				break;
			case 2:
				type = 2;
				break;
			}
			final SpellBook book = type == 1 ? SpellBook.ANCIENT : SpellBook.MODERN;
		    if (book == SpellBook.ANCIENT && !hasRequirement(player, Quests.DESERT_TREASURE)) {
			player.getPacketDispatch().sendMessage("You need to complete Desert Treasure to use Ancient Magicks.");
			end();
			return true;
		    }
			// Remove runes for Spellbook Swap
			ArrayList<Item> runes = player.getAttribute("spell:runes", new ArrayList<>());
			if (!runes.isEmpty() && player.getInventory().remove(runes.toArray(new Item[0]))) {
				player.removeAttribute("spell:runes");
				player.removeAttribute("tablet-spell");
			}
			// Award XP
			player.skills.addExperience(Skills.MAGIC, 130);
			// Change book
			player.dispatch(new SpellbookChangeEvent(
					SpellBook.LUNAR,
					book,
					SpellbookChangeSource.SPELLBOOK_SWAP_CAST));
			player.getSpellBookManager().setSpellBook(book);
			player.getInterfaceManager().openTab(new Component(book.getInterfaceId()));
		    player.getPacketDispatch().sendMessage("You have 2 minutes before your spellbook changes back to the Lunar Spellbook!");
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3264731 };
	}
}
