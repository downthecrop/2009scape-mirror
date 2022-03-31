package core.game.interaction.item.withobject;

import core.game.content.dialogue.DialogueAction;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import rs09.game.world.repository.Repository;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;

/**
 * Handles the blessing of Spirit shields.
 * @author Splinter
 */
@Initializable
public class SpiritShieldBlessPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code SpiritShieldMakePlugin} {@code Object}
	 */
	public SpiritShieldBlessPlugin() {
		super(13754, 13734);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(24343, OBJECT_TYPE, this);
		ClassScanner.definePlugin(new SpiritShieldMakePlugin());
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if(!player.getInventory().containItems(13754, 13734)){
			player.sendMessage("You need a holy elixir and an unblessed spirit shield in order to do this.");
			return true;
		}
		if(player.getSkills().getLevel(Skills.PRAYER) < 85){
			player.sendMessage("You need a Prayer level of 85 in order to bless the shield.");
			return true;
		}
		if(player.getInventory().remove(new Item(13754, 1), new Item(13734, 1))){
			player.getInventory().add(new Item(13736, 1));
			player.sendMessage("You successfully bless the shield using the holy elixir.");
		}
		return true;
	}
	
	/**
	 * Handles the attaching of sigils to the blessed spirit shield.
	 * @author Splinter
	 */
	public class SpiritShieldMakePlugin extends UseWithHandler {

		/**
		 * Constructs a new {@code SpiritShieldMakePlugin} {@code Object}
		 */
		public SpiritShieldMakePlugin() {
			super(13746, 13748, 13750, 13752);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(2782, OBJECT_TYPE, this);
			addHandler(2783, OBJECT_TYPE, this);
			addHandler(4306, OBJECT_TYPE, this);
			addHandler(6150, OBJECT_TYPE, this);
			addHandler(22725, OBJECT_TYPE, this);
			addHandler(26817, OBJECT_TYPE, this);
			addHandler(37622, OBJECT_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(final NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final Item used = event.getUsedItem();
			final Item result = new Item(used.getId() - 8, 1);
			if(used.getId() > 13752 || used.getId() < 13746){
				return true;
			}
			if(!player.getInventory().containsItem(new Item(13736))){
				player.sendMessage("You need a blessed spirit shield in order to continue.");
				return true;
			}
			if(player.getSkills().getLevel(Skills.PRAYER) < 90 || player.getSkills().getLevel(Skills.SMITHING) < 85){
				player.sendMessage("You need a Prayer level of 90 and a Smithing of 85 in order to attach the sigil.");
				return true;
			}
			player.getDialogueInterpreter().sendOptions("Combine the two?", "Yes", "No");
			player.getDialogueInterpreter().addAction(new DialogueAction() {

				@Override
				public void handle(Player player, int buttonId) {
					switch (buttonId) {
					case 2:
						if(player.getInventory().remove(used, new Item(13736, 1))){
							player.getInventory().add(result);
							player.animate(new Animation(898));
							player.getDialogueInterpreter().sendItemMessage(result.getId(), "You successfully attach the "+used.getName().toLowerCase()+" to the blessed", "spirit shield.");
							Repository.sendNews(player.getUsername()+" has just made the "+result.getName()+".");
						}
						break;
					}
				}

			});
			return true;
		}
	}
}
