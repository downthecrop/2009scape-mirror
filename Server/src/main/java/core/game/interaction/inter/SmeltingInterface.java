package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.skill.smithing.smelting.Bar;
import core.game.node.entity.skill.smithing.smelting.SmeltingPulse;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.plugin.Plugin;
import kotlin.Unit;

/**
 * @author 'Vexia
 */
@Initializable
public class SmeltingInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(311, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		final BarButton barType = BarButton.forId(button);
		if (barType == null) {
			return true;
		}
		if (barType.getAmount() == -1) {
			player.getInterfaceManager().closeChatbox();
			sendInputDialogue(player, true, "Enter the amount:", (value) -> {
				submitIndividualPulse(player, new SmeltingPulse(player, null, barType.getBar(), (int) value));
				return Unit.INSTANCE;
			});
		} else {
			player.getPulseManager().run(new SmeltingPulse(player, null, barType.getBar(), barType.getAmount()));
		}
		return true;
	}

	/**
	 * @author 'Vexia
	 */
	public enum BarButton {
		BRONZE_1(16, Bar.BRONZE, 1), BRONZE_5(15, Bar.BRONZE, 5), BRONZE_10(14, Bar.BRONZE, 10), BRONZE_X(13, Bar.BRONZE, -1), BLURITE_1(20, Bar.BLURITE, 1), BLURITE_5(19, Bar.BLURITE, 5), BLURITE_10(18, Bar.BLURITE, 10), BLURITE_X(17, Bar.BLURITE, -1), IRON_1(24, Bar.IRON, 1), IRON_5(23, Bar.IRON, 5), IRON_10(22, Bar.IRON, 10), IRON_X(21, Bar.IRON, -1), SILVER_1(28, Bar.SILVER, 1), SILVER_5(27, Bar.SILVER, 5), SILVER_10(26, Bar.SILVER, 10), SILVER_X(25, Bar.SILVER, -1), STEEL_1(32, Bar.STEEL, 1), STEEL_5(31, Bar.STEEL, 5), STEEL_10(30, Bar.STEEL, 10), STEEL_X(29, Bar.STEEL, -1), GOLD_1(36, Bar.GOLD, 1), GOLD_5(35, Bar.GOLD, 5), GOLD_10(34, Bar.GOLD, 10), GOLD_X(33, Bar.GOLD, -1), MITHRIL_1(40, Bar.MITHRIL, 1), MITHRIL_5(39, Bar.MITHRIL, 5), MITHRIL_10(38, Bar.MITHRIL, 10), MITHRIL_X(37, Bar.MITHRIL, -1), ADAMANT_1(44, Bar.ADAMANT, 1), ADAMANT_5(43, Bar.ADAMANT, 5), ADAMANT_10(42, Bar.ADAMANT, 10), ADAMANT_X(41, Bar.ADAMANT, -1), RUNE_1(48, Bar.RUNITE, 1), RUNE_5(47, Bar.RUNITE, 5), RUNE_10(46, Bar.RUNITE, 10), RUNE_X(45, Bar.RUNITE, -1);

		/**
		 * Constructs a new {@code BarButton} {@code Object}.
		 * @param button the button.
		 * @param bar the bar.
		 * @param amount the amt.
		 */
		BarButton(int button, Bar bar, int amount) {
			this.button = button;
			this.bar = bar;
			this.amount = amount;
		}

		/**
		 * The button.
		 */
		private int button;

		/**
		 * The bar.
		 */
		private Bar bar;

		/**
		 * The ammount.
		 */
		private int amount;

		/**
		 * @param id
		 * @return
		 */
		public static BarButton forId(int id) {
			for (BarButton button : BarButton.values()) {
				if (button.getButton() == id) {
					return button;
				}
			}
			return null;
		}

		/**
		 * @return the button.
		 */
		public int getButton() {
			return button;
		}

		/**
		 * @return the bar.
		 */
		public Bar getBar() {
			return bar;
		}

		/**
		 * @return the amount.
		 */
		public int getAmount() {
			return amount;
		}
	}
}
