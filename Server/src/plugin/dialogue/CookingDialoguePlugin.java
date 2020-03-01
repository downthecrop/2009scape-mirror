package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.CookingProperties;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ChildPositionContext;
import org.crandor.net.packet.out.RepositionChild;
import org.crandor.plugin.Plugin;

/**
 * Represents the dialogue used to handle the amount to make of a cookable item.
 * @author 'Vexia
 * @version 1.0
 */
public final class CookingDialoguePlugin extends DialoguePlugin {

	/**
	 * Represents the cooking dialogue id.
	 */
	public static final int DIALOGUE_ID = 43989;

	/**
	 * Represents the sinew item.
	 */
	private static final Item SINEW = new Item(9436);

	/**
	 * Represents the food we're cooking.
	 */
	private Food food;

	/**
	 * Represents the obejct we're cooking on.
	 */
	private GameObject object;

	/**
	 * Constructs a new {@code CookingDialoguePlugin} {@code Object}.
	 */
	public CookingDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CookingDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public CookingDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CookingDialoguePlugin(player);
	}

	@Override
	public void init() {
		super.init();
		try {
			new MeatPlugin().newInstance(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean open(Object... args) {
		food = ((Food) args[0]);
		object = ((GameObject) args[1]);
		if (args.length == 3) {
			interpreter.sendOptions("Select an Option", "Dry the meat into sinew.", "Cook the meat.");
			stage = 100;
			return true;
		}
		if (player.getInventory().getAmount(food.getRaw()) == 1) {
			end();
			food.cook(player, object, 1);
			return true;
		}
		display();
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			end();
			int amount = getAmount(buttonId);
			if (amount == -1) {
				player.setAttribute("runscript", new RunScript() {
					@Override
					public boolean handle() {
						int amount = (int) value;
						food.cook(player, object, amount);
						return false;
					}
				});
				player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
			} else if (amount == 28){
				player.setAttribute("runscript", new RunScript() {
					@Override
					public boolean handle() {
						food.cook(player, object, amount);
						return false;
					}
				});
			}
			food.cook(player, object, amount);
			break;
		case 100:
			switch (buttonId) {
			case 1:
				food = Consumables.forFood(SINEW);
				display();
				break;
			case 2:
				food = MeatPlugin.MEAT;
				display();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { DIALOGUE_ID };
	}

	/**
	 * Method used to display the content food.
	 */
	public void display() {
		player.getInterfaceManager().openChatbox(307);
		PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 307, 3, 60, 79));
		player.getPacketDispatch().sendItemZoomOnInterface(food.getRaw().getId(), 160, 307, 2);
		player.getPacketDispatch().sendString(food.getRaw().getName(), 307, 3);
		stage = 0;
	}

	/**
	 * Method used to get the amount to make based off the button id.
	 * @param buttonId the button id.
	 * @return the amount to make.
	 */
	private final int getAmount(final int buttonId) {
		switch (buttonId) {
		case 6:
			return 1;
		case 5:
			return 5;
		case 4:
			return -1;
		case 3:
			return 28;

		}
		return -1;
	}

	/**
	 * Represents the meat consumable food.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class MeatPlugin extends Food {

		/**
		 * Represents the cooking dialogue id.
		 */
		private static final int DIALOGUE_ID = 43989;

		/**
		 * Represents the meat plugin.
		 */
		public static final MeatPlugin MEAT = new MeatPlugin(2142, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of beef.", "You accidentally burn the meat."));

		/**
		 * Constructs a new {@code MeatPlugin} {@code Object}.
		 */
		public MeatPlugin() {
			/**
			 * empty.
			 */
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			Consumables.add(MEAT);
			Consumables.add(new MeatPlugin(9436, 2132, 2146, null, new MeatProperty(1, 30, 30, "You dry a piece of beef and extract the sinew.", "You accidentally burn the meat.")));
			Consumables.add(new MeatPlugin(2134, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of rat meat.", "You accidentally burn the meat.")));
			Consumables.add(new MeatPlugin(2136, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of bear meat.", "You accidentally burn the meat.")));
			return this;
		}

		/**
		 * Constructs a new {@code Meat} {@code Object}.
		 * @param item the item.
		 * @param raw the raw item.
		 * @param burnt the burnt item.
		 * @param foodProperties the food properties.
		 * @param cookingProperties the cooking properties.
		 */
		public MeatPlugin(int item, int raw, int burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
			super(item, raw, burnt, foodProperties, cookingProperties);
		}

		@Override
		public boolean interact(final Player player, final Node node) {
			int stage = TutorialSession.getExtension(player).getStage();
			if (stage < TutorialSession.MAX_STAGE) {
				cook(player, (GameObject) node, 1);
				return false;
			}
			if (TutorialSession.getExtension(player).getStage() < TutorialSession.MAX_STAGE) {
				return true;
			} else {
				player.getDialogueInterpreter().open(DIALOGUE_ID, this, node, true);
			}
			return false;
		}

		/**
		 * Represents the meat properties used to override for sinew.
		 * @author 'Vexia
		 * @date 23/12/2013
		 */
		public static class MeatProperty extends CookingProperties {

			/**
			 * Constructs a new {@code Meat} {@code Object}.
			 * @param level the level.
			 * @param experience the experience.
			 * @param burnLevel the burn level.
			 * @param messages the messages.
			 */
			public MeatProperty(int level, double experience, int burnLevel, String... messages) {
				super(level, experience, burnLevel, true, messages);
			}

			@Override
			public boolean cook(final Food food, final Player player, final GameObject object) {
				return super.cook(food, player, object);
			}
		}
	}
}
