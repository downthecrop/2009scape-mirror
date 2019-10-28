package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the puppy interface plugin.
 * @author 'Vexia
 */
@InitializablePlugin
public final class PuppyInterfacePlugin extends ComponentPlugin {

	/**
	 * Represents the names of the puppys.
	 */
	private static final String[] NAMES = new String[] { "labrador", "bulldog", "dalmatian", "greyhound", "terrier", "sheepdog" };

	/**
	 * Represents the puppies.
	 */
	private static final Item[][] PUPPIES = new Item[][] { { new Item(12516), new Item(12708), new Item(12710) }, { new Item(12522), new Item(12720), new Item(12722) }, { new Item(12518), new Item(12712), new Item(12714) }, { new Item(12514), new Item(12704), new Item(12706) }, { new Item(12512), new Item(12700), new Item(12702) }, { new Item(12520), new Item(12716), new Item(12718) } };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(668, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		int index = button == 3 ? 1 : button == 8 ? 0 : button == 6 ? 1 : button == 4 ? 2 : button == 5 ? 3 : button == 6 ? 4 : 5;
		player.getDialogueInterpreter().open(6893, NAMES[index], PUPPIES[index][RandomFunction.random(PUPPIES[index].length)]);
		return true;
	}

}
