package core.game.node.entity.skill.construction.decoration.kitchen;


import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the Construction beer barrels.
 * @author Splinter
 */
@Initializable
public class BeerBarrelPlugin extends UseWithHandler {

	/**
	 * The object ids
	 */
	private static final int[] OBJECTS = new int[] { 
		13568, 13569, 13570, 13571, 13572, 13573
	};

	/**
	 * Constructs a new {@Code BeerBarrelPlugin} {@Code Object}
	 */
	public BeerBarrelPlugin() {
		super(1919);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : OBJECTS) {
			addHandler(id, OBJECT_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Player player = event.getPlayer();
		final Scenery object = (Scenery) event.getUsedWith();

		if (player.getInventory().remove(new Item(1919))) {
			player.animate(Animation.create(3661 + (object.getId() - 13569)));
			player.sendMessage("You fill up your glass with " + object.getName().toLowerCase().replace("barrel", "").trim() + ".");
			player.getInventory().add(new Item(getReward(object.getId()), 1));
		}
		return true;
	}

	/**
	 * Get the beer reward based on the interacted barrel.
	 * @return the item to give.
	 */
	public int getReward(int barrelId) {
		switch (barrelId) {
		case 13568:
			return 1917;
		case 13569:
			return 5763;
		case 13570:
			return 1905;
		case 13571:
			return 1909;
		case 13572:
			return 1911;
		case 13573:
			return 5755;
		}
		return 1917;
	}
}