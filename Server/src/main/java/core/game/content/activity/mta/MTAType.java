package core.game.content.activity.mta;

import core.game.component.Component;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;

import core.game.content.activity.mta.impl.AlchemistZone;
import core.game.content.activity.mta.impl.EnchantingZone;
import core.game.content.activity.mta.impl.GraveyardZone;
import core.game.content.activity.mta.impl.TelekineticZone;

/**
 * A magic training arena game type.
 * @author Vexia
 */
public enum MTAType {
	TELEKINETIC(10778, new Component(198), null, Location.create(3363, 3316, 0), new TelekineticZone()) {
		@Override
		public boolean hasRequirement(Player player) {
			if (!player.getSkills().hasLevel(Skills.MAGIC, 33)) {
				player.getDialogueInterpreter().sendDialogue("You need to be able to cast the Telekinetic Grab spell in order to", "enter.");
				return false;
			}
			return true;
		}
	},
	ALCHEMISTS(10780, new Component(194), new Location(3366, 9623, 2), new Location(3363, 3320, 0), AlchemistZone.ZONE) {
		@Override
		public boolean hasRequirement(Player player) {
			if (!player.getSkills().hasLevel(Skills.MAGIC, 21)) {
				player.getDialogueInterpreter().sendDialogue("You need to be able to cast the Telekinetic Grab spell in order to", "enter.");
				return false;
			}
			if (player.getInventory().contains(995, 1)) {
				player.getDialogueInterpreter().sendDialogue("You cannot take money into the Alchemists' Playground.");
				return false;
			}
			return true;
		}

		@Override
		public void exit(Player player) {
			int earn = player.getAttribute("alch-earn", 0);
			if (earn != 0) {
				Item coins = new Item(995, earn);
				if (player.getBank().hasSpaceFor(coins)) {
					player.getBank().add(coins);
				}
				player.getDialogueInterpreter().sendDialogue("You've been awarded " + earn + " coins straight into your bank as a reward!");
			}
			super.exit(player);
		}
	},
	ENCHANTERS(10779, new Component(195), new Location(3363, 9649, 0), new Location(3361, 3318, 0), EnchantingZone.ZONE) {
		@Override
		public boolean hasRequirement(Player player) {
			if (!player.getSkills().hasLevel(Skills.MAGIC, 7)) {
				player.getDialogueInterpreter().sendDialogue("You need to be able to cast the Lvl-1 Enchant spell in order to", "enter.");
				return false;
			}
			return true;
		}
	},
	GRAVEYARD(10781, new Component(196), new Location(3363, 9639, 1), new Location(3365, 3318, 0), GraveyardZone.ZONE) {
		@Override
		public boolean hasRequirement(Player player) {
			if (!player.getSkills().hasLevel(Skills.MAGIC, 15)) {
				player.getDialogueInterpreter().sendDialogue("You need to be able to cast the Bones to Bananas spell in order to", "enter.");
				return false;
			}
			if (player.getInventory().contains(1963, 1) || player.getInventory().contains(6883, 1)) {
				player.getDialogueInterpreter().sendDialogue("You can't take bananas or peaches into the arena.");
				return false;
			}
			return true;
		}
	};

	/**
	 * The object id.
	 */
	private final int objectId;

	/**
	 * The overlay component.
	 */
	private final Component overlay;

	/**
	 * The start location.
	 */
	private final Location startLocation;

	/**
	 * The end location.
	 */
	private final Location endLocation;

	/**
	 * The mage training arena zone.
	 */
	private final MTAZone zone;

	/**
	 * Constructs a new {@Code MTAType} {@Code Object}
	 * @param objectId the object id.
	 * @param overlay the overlay.
	 * @param zone the zone.
	 */
	private MTAType(int objectId, Component overlay, Location startLocation, Location endLocation, MTAZone zone) {
		this.objectId = objectId;
		this.overlay = overlay;
		this.zone = zone;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}

	/**
	 * Enters the mage training arena game.
	 * @param player the player.
	 */
	public void enter(Player player) {
		if (!player.getSavedData().getActivityData().isStartedMta() || (!player.getInventory().containsItem(MageTrainingArenaPlugin.PROGRESS_HAT) && !player.getEquipment().containsItem(MageTrainingArenaPlugin.PROGRESS_HAT))) {
			player.getDialogueInterpreter().sendDialogue("You need a Pizazz Progress Hat in order to enter. Talk to the", "Entrance Guardian if you don't have one.");
			return;
		}
		if (!hasRequirement(player)) {
			return;
		}
		if (this != TELEKINETIC) {
			player.teleport(startLocation);
		} else {
			TelekineticZone.start(player);
		}
		player.sendMessage("You've entered the " + zone.getName() + ".");
	}

	/**
	 * Exits the zone area.
	 * @param player the player.
	 */
	public void exit(Player player) {
		player.teleport(endLocation);
	}

	/**
	 * Checks if the player has the requirements.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean hasRequirement(Player player) {
		return false;
	}

	/**
	 * Gets the mta type for the zone.
	 * @param mtaZone the zone.
	 * @return the type.
	 */
	public static MTAType forZone(MTAZone mtaZone) {
		for (MTAType type : values()) {
			if (type == null) {
				continue;
			}
			if (type.getZone() == mtaZone) {
				return type;
			}
		}
		return TELEKINETIC;
	}

	/**
	 * Gets the mtat type.
	 * @param id the id.
	 * @return the mtat type.
	 */
	public static MTAType forId(int id) {
		for (MTAType t : values()) {
			if (t.getObjectId() == id) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Gets the objectId.
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Gets the zone.
	 * @return the zone
	 */
	public MTAZone getZone() {
		return zone;
	}

	/**
	 * Gets the overlay.
	 * @return the overlay
	 */
	public Component getOverlay() {
		return overlay;
	}

}
