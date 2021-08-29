package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for taverly dungeon.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class TaverlyDungeonPlugin extends OptionHandler {

	/**
	 * The suits of armour.
	 */
	private static final NPC[] ARMOUR_SUITS = new NPC[2];

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2143).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2144).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = ((Scenery) node).getId();
		switch (id) {
		case 2143:
		case 2144:
			if (player.getLocation().getX() < node.getLocation().getX() && !player.getAttribute("spawned_suits", false)) {
				boolean alive = true;
				for (int i = 0; i < ARMOUR_SUITS.length; i++) {
					NPC npc = ARMOUR_SUITS[i];
					if (npc == null || !npc.isActive()) {
						Location location = Location.create(2887, 9829 + (i * 3), 0);
						ARMOUR_SUITS[i] = npc = NPC.create(453, location);
						npc.init();
						npc.getProperties().getCombatPulse().attack(player);
						Scenery object = RegionManager.getObject(location);
						if (object != null) {
							SceneryBuilder.remove(object);
						}
						alive = false;
					}
				}
				if (!alive) {
					player.setAttribute("spawned_suits", true);
					player.getPacketDispatch().sendMessage("Suddenly the suit of armour comes to life!");
					return true;
				}
			}
			player.removeAttribute("spawned_suits");
			DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
			return true;
		}
		return false;
	}

}
