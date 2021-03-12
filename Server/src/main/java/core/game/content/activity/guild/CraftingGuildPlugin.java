package core.game.content.activity.guild;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.ObjectDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.global.Skillcape;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.crafting.TanningProduct;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.object.GameObject;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the crafting guild.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CraftingGuildPlugin extends OptionHandler {

	/**
	 * Represents the brown apron item.
	 */
	private static final Item BROWN_APRON = new Item(1757);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2647).getHandlers().put("option:open", this);
		NPCDefinition.forId(804).getHandlers().put("option:trade", this);
		new MasterCrafterDialogue().init();
		new TannerDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node instanceof GameObject ? ((GameObject) node).getId() : ((NPC) node).getId();
		switch (option) {
		case "open":
			switch (id) {
			case 2647:
				if (player.getLocation().getY() >= 3289) {
					if (player.getSkills().getStaticLevel(Skills.CRAFTING) < 40) {
						player.getDialogueInterpreter().sendDialogues(805, null, "Sorry, only experienced crafters are allowed in here.", "You must be level 40 or above to enter.");
						return true;
					}
					if (!player.getEquipment().containsItem(BROWN_APRON)) {
						player.getDialogueInterpreter().open(805, true, true);
						return true;
					}
					player.getDialogueInterpreter().sendDialogues(805, null, "Welcome to the Guild of Master Craftsmen.");
					DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
				} else {
					DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
					return true;
				}
				break;
			}
			return true;
		case "trade":
			switch (id) {
			case 804:
				TanningProduct.open(player, 804);
				break;
			}
			return true;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof GameObject) {
			return DoorActionHandler.getDestination((Player) node, (GameObject) n);
		}
		return null;
	}

	/**
	 * Represents the dialogue plugin used for the crafting master.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class MasterCrafterDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code MasterCrafterDialogue} {@code Object}.
		 */
		public MasterCrafterDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code MasterCrafterDialogue} {@code Object}.
		 * @param player the player.
		 */
		public MasterCrafterDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new MasterCrafterDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (args.length == 2) {
				npc("Where's your brown apron? You can't come in here", "unless you're wearing one.");
				stage = 100;
				return true;
			}
			npc = (NPC) args[0];
			npc("Hello, and welcome to the Crafting Guild. Accomplished", "crafters from all over the land come here to use our", "top notch workshops.");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				if (npc.getId() == 805) {
					if (Skillcape.isMaster(player, Skills.CRAFTING)) {
						player("Hey, could I buy a Skillcape of Crafting?");
						stage = 3;
					} else {
						player("Hey, what is that cape you're wearing?", "I don't recognise it.");
						stage = 1;
					}
				} else {
					end();
				}
				break;
			case 1:
				npc("This? This is a Skillcape of Crafting. It is a symbol of", "my ability and standing here in the Crafting Guild. If", "you should ever achieve level 99 Crafting come and talk", "to me and we'll see if we can sort you out with one.");
				stage = 2;
				break;
			case 2:
				end();
				break;
			case 3:
				npc("Certainly! Right after you pay me 99000 coins.");
				stage = 4;
				break;
			case 4:
				options("Okay, here you go.", "No, thanks.");
				stage = 5;
				break;
			case 5:
				switch (buttonId) {
				case 1:
					player("Okay, here you go.");
					stage = 6;
					break;
				case 2:
					end();
					break;
				}
				break;
			case 6:
				if (Skillcape.purchase(player, Skills.CRAFTING)) {
					npc("There you go! Enjoy.");
				}
				stage = 7;
				break;
			case 7:
				end();
				break;
			case 100:
				npc("Where's your borwn apron? You can't come in here", "unless you're wearing one.");
				stage = 101;
				break;
			case 101:
				player("Err... I haven't got one.");
				stage = 102;
				break;
			case 102:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 805, 2732, 2733 };
		}

	}

	/**
	 * Represents the dialogue used for the tanner npc.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class TannerDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code TannerDialogue} {@code Object}.
		 */
		public TannerDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code TannerDialogue} {@code Object}.
		 * @param player the player.
		 */
		public TannerDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new TannerDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc("Greetings friend. I am a manufacturer of leather.");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				options("Can I buy some leather then?", "Leather is rather weak stuff.");
				stage = 1;
				break;
			case 1:
				switch (buttonId) {
				case 1:
					player("Can I buy some leather then?");
					stage = 10;
					break;
				case 2:
					player("Leather is rather weak stuff.");
					stage = 20;
					break;
				}
				break;
			case 10:
				npc("Certainly!");
				stage = 11;
				break;
			case 11:
				end();
				TanningProduct.open(player, 804);
				break;
			case 20:
				npc("Normal leather may be quite weak, but it's very cheap -", "I make it from cowhides for only 1 gp per hide - and", "it's so easy to craft that anyone can work with it.");
				stage = 21;
				break;
			case 21:
				npc("Alternatively you could try hard leather. It's not so", "easy to craft, but I only charge 3gp per cowhide to", "prepare it, and it makes much studier armour.");
				stage = 22;
				break;
			case 22:
				npc("I can also tan snake hides and dragonhides, suitable for", "crafting into the highest quality armour for rangers.");
				stage = 23;
				break;
			case 23:
				player("Thanks, I'll bear it in mind.");
				stage = 24;
				break;
			case 24:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 804 };
		}

	}
}
