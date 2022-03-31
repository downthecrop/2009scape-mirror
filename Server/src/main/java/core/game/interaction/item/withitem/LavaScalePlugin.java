package core.game.interaction.item.withitem;

import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the grinding of Lava Scales.
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class LavaScalePlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code LavaScalePlugin} {@code Object}.
	 */
	public LavaScalePlugin() {
		super(14695);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(233, ITEM_TYPE, this);
		ClassScanner.definePlugin(new AntifireMakePlugin());
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Player player = event.getPlayer();
		if(player.getInventory().remove(new Item(14695))){
			player.getInventory().add(new Item(14768, RandomFunction.random(3, 6)));
			player.animate(new Animation(364));
			player.sendMessage("You grind the scales into fine shards.");
		}
		return true;
	}
	
	/**
	 * Handles the making of the Extended antifire potion.
	 * It's apparently not made like regular potions.
	 * @author Splinter
	 * @version 1.0
	 */
	public final class AntifireMakePlugin extends UseWithHandler {

		/**
		 * Constructs a new {@code LavaScalePlugin} {@code Object}.
		 */
		public AntifireMakePlugin() {
			super(2454, 2456, 2458, 2452);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(14768, ITEM_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			Player player = event.getPlayer();
			int count = player.getInventory().getAmount(event.getUsedWith().asItem());
			int total = count * getReq(event.getUsedWith().asItem().getId());
			if(player.getInventory().contains(14768, total)){
				player.getInventory().remove(new Item(14768, total), new Item(event.getUsedWith().getId(), count));
				player.getInventory().add(new Item(event.getUsedWith().getId() + 12301, count));
				player.animate(new Animation(363));
				player.getSkills().addExperience(Skills.HERBLORE, 27.5 * total);
				player.sendMessages("You drop a total of "+(total)+" lava scales in the potions and upgrade all of the", "potions of the same dose into extended antifire potions.");
			} else {
				player.sendMessage("You don't have enough shards to upgrade all your potions of that dose.");
			}
			return true;
		}

	}
	
	/**
	 * Get the required amount of shards.
	 * @return
	 */
	public int getReq(int id){
		switch(id){
		case 2452:
			return 4;
		case 2454:
			return 3;
		case 2456:
			return 2;
		case 2458:
			return 1;
		}
		return 4;
	}

}
