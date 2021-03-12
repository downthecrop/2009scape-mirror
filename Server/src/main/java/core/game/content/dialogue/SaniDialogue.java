package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.plugin.Initializable;
import rs09.game.system.config.ShopParser;

/**
 * Handles the SaniDialogue dialogue.
 * @author Vexia
 * 
 */
@Initializable
public class SaniDialogue extends DialoguePlugin {

	/**
	 * Constructs the {@code SaniDialgoue}
	 */
	public SaniDialogue() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs the {@code SaniDialogue}
	 * @param player The player instance.
	 */
	public SaniDialogue(Player player) {
		super(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Greetings, " + player.getUsername(), "I sell weapons and armour.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Let me see your stock.", "Where do I get other items, like gloves?", "How do I open these box sets?");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Let me see your stock.");
				stage = 10;
				break;
			case 2:
				player("Where do I get other items, like gloves and capes?");
				stage = 20;
				break;
			case 3:
				player("How do I open these box sets?");
				stage = 30;
				break;
			}
			break;
		case 10:
			options("Weapons", "Armour");
			stage++;
			break;
		case 11:
			switch (buttonId) {
			case 1:
				if (player.getSkills().getLevel(Skills.ATTACK) < 30) {
					openWeaponShop(player, 204);
					end();
					break;
				}
				options("Weapons (Bronze - Mithril)", "Weapons (Adamant - Dragon)");
				stage = 12;
				break;
			case 2:
				end();
				ShopParser.Companion.getSHOPS().get(4905).open(player);
				break;
			}
			break;
		case 12:
			switch (buttonId) {
			case 1:
				openWeaponShop(player, 204);
				end();
				break;
			case 2:
				openWeaponShop(player, 205);
				end();
				break;
			}
			break;
		case 13:
			end();
			break;
		case 20:
			npc("Gloves of all kinds may be feely purchased from the", "Culinaromancer's chest below the Lumbridge Castle.", "On the other hand, high-tier armours can be obtained", "by defeating high-levelled bosses, like the Corporeal Beast.");
			stage++;
			break;
		case 21:
			npc("Bossing is not the only source of high-tier armour.", "Try Slayer or Barrows, catching Dragon Implings or", "perhaps even defeating monsters inside the TzHaar", "cave for fabled onyx equipment is more your style.");
			stage++;
			break;
		case 22:
			player("I see...", "So this means that you only sell common armours", "to help adventurers like nyself get started in the world", "of Keldagrim.");
			stage++;
			break;
		case 23:
			npc("Indeed, that is true. If you have any more questions", "The Keldagrim guide near the green portal should be able", "to assist you further.");
			stage = 0;
			break;
		case 30:
			player("How exactly do I open these box sets that you", "are selling?");
			stage++;
			break;
		case 31:
			npc("Take the box set to the Grand Exchange clerk. Right", "click her and select 'sets'. Next, right-click the box", "item that is in your inventory to exchange the set for", "the items that are inside the box.");
			stage++;
			break;
		case 32:
			npc("You may always convert the individual items back into", "a box set at any time in order to save banking", "space.");
			stage = 0;
			break;
		}
		return true;
	}

	/**
	 * Opens the weapon shop.
	 * @param player The player.
	 * @param uid The uid.
	 */
	private void openWeaponShop(Player player, int uid) {
		ShopParser.Companion.openUid(player, uid);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SaniDialogue(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4905 };
	}
}
