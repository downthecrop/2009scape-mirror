package core.game.node.entity.skill.construction;


import static api.ContentAPIKt.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import kotlin.Unit;
import rs09.game.world.repository.Repository;
import rs09.plugin.ClassScanner;

/**
 * Handles the house portal options.
 * @author Emperor
 *
 */
@Initializable
public final class PortalOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (HouseLocation hl : HouseLocation.values()) {
			SceneryDefinition.forId(hl.getPortalId()).getHandlers().put("option:enter", this);
		}
		SceneryDefinition.forId(13405).getHandlers().put("option:lock", this);
		SceneryDefinition.forId(13405).getHandlers().put("option:enter", this);
		ClassScanner.definePlugin(new PortalDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = node.asScenery();
		if (object.getId() == 13405 && option.equals("enter")) {
			HouseManager.leave(player);
			return true;
		}
		if (option.equals("lock")) {
			if (player.getHouseManager().isInHouse(player)) {
				player.getHouseManager().setLocked(player.getHouseManager().isLocked() ? false : true);
				player.sendMessage("Your house is now "+(player.getHouseManager().isLocked() ? "locked." : "unlocked." ));
				return true;
			}
			player.sendMessage("This is not your house.");
			return true;
		}
		player.setAttribute("con:portal", object.getId());
		player.getDialogueInterpreter().open("con:portal");
		return true;
	}
	
	/**
	 * Handles the portal dialogue.
	 * @author Emperor
	 */
	@PluginManifest(type= PluginType.DIALOGUE)
	private class PortalDialogue extends DialoguePlugin {
		
		/**
		 * Constructs a new {@code PortalDialogue} {@code Object}
		 */
		public PortalDialogue() {
			
		}
		
		/**
		 * Constructs a new {@code PortalDialogue} {@code Object}
		 * @param player The player.
		 */
		public PortalDialogue(Player player) {
			super(player);
		}
		
		@Override
		public DialoguePlugin newInstance(Player player) {
			return new PortalDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			
			

			
				options("Go to your house", "Go to your house (building mode)", "Go to a friend's house", "Never mind");
				stage = 1;

			
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			end();
			switch (stage) {
			case 1:
				switch (buttonId) {
				case 1:
				case 2:
					//If the player does not have a house
					if (!player.getHouseManager().hasHouse()) {
						player.getPacketDispatch().sendMessage("<col=FF0000>You don't have a house, talk to an estate agent to purchase a house.");
						break;
					}
					//If the Player has a house, but does not have their house moved to the portal they are interacting with
					if (player.getHouseManager().getLocation().getPortalId() != player.getAttribute("con:portal", -1)) {
						player.getPacketDispatch().sendMessage("<col=FF0000>Your house is in " + player.getHouseManager().getLocation().getName() + ".");
						player.sendMessage("<col=FF0000>Speak with an estate agent to change your house location.");
						break;
					}
					player.getHouseManager().enter(player, buttonId == 2, true);
					break;
				case 3:
					if(player.getIronmanManager().isIronman()){
						end();
						sendMessage(player, "You can't do that as an ironman.");
						return true;
					}
					sendInputDialogue(player, false, "Enter friend's name:", (value) -> {
						Player p = Repository.getPlayerByName((String) value);
						if (p == null || !p.isActive()) {
							player.getPacketDispatch().sendMessage("This player is not online.");
							return Unit.INSTANCE;
						}
						if (p.getUsername().equals(player.getUsername())) {
							player.getPacketDispatch().sendMessage("You aren't a friend of yourself!");
							return Unit.INSTANCE;
						}
						if (!p.getHouseManager().isLoaded()) {
							player.getPacketDispatch().sendMessage("This player is not at home right now.");
							return Unit.INSTANCE;
						}
						if (p.getHouseManager().isBuildingMode()) {
							player.getPacketDispatch().sendMessage("This player is in building mode.");
							return Unit.INSTANCE;
						}
						if (p.getHouseManager().isLocked()) {
							player.getPacketDispatch().sendMessage("The other player has locked their house.");
							return Unit.INSTANCE;
						}
						p.setAttribute("poh_owner", (String) value);
						p.getHouseManager().enter(player, false, false);
						return Unit.INSTANCE;
					});
					break;
				}
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:portal") };
		}
	}
}