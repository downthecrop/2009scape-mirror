package core.game.content.zone.neitiznot;

import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;

/**
 * Handles the neitiznot zone.
 * @author Vexia
 */
@Initializable
public class NeitiznotZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code NeitiznotZone} {@code Object}
	 */
	public NeitiznotZone() {
		super("Neitiznot zone", true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		ClassScanner.definePlugins(new MawnisBurowgarDialogue(), new ThakkradYakDialogue(), new YakArmourPlugin(), new YakArmourPlugin());
		return this;
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (e instanceof Player) {
			Player player = e.asPlayer();
			switch (target.getId()) {
			case 21301:
				player.getBank().open();
				return true;
			case 5506:
				if (option.getName().equals("Craft-goods")) {
					player.getDialogueInterpreter().open("thakkrad-yak");
					return true;
				}
				break;
			}
		}
		return super.interact(e, target, option);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		registerRegion(9275);
	}

}
