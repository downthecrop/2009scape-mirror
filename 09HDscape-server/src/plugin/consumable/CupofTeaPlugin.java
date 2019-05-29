package plugin.consumable;

import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Drink;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.item.Item;

/**
 * Represents the cup of tea plugin.
 * @author 'Veixa
 * @version 1.0
 */
@InitializablePlugin
public final class CupofTeaPlugin extends Drink {

	/**
	 * Represents the force chat to use.
	 */
	private static final String CHAT = "Aaah, nothing like a nice cuppa tea!";

	@Override
	public CupofTeaPlugin newInstance(Object object) {
		Consumables.add(this);
		return this;
	}

	/**
	 * Constructs a new {@code CupofTeaPlugin} {@code Object}.
	 */
	public CupofTeaPlugin() {
		super(712, new ConsumableProperties(2, 1980));
	}

	@Override
	public void consume(final Item item, final Player player) {
		player.sendChat(CHAT);
		super.remove(player, item);
	}

}
