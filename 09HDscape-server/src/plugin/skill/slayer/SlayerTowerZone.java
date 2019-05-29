package plugin.skill.slayer;

import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.Option;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.game.world.map.zone.ZoneRestriction;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the slayer tower map zone.
 * @author Vexia
 */
@InitializablePlugin
public final class SlayerTowerZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code SlayerTowerZone} {@code Object}.
	 */
	public SlayerTowerZone() {
		super("slayer tower", true, ZoneRestriction.CANNON);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		return this;
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (e instanceof Player) {
			Player player = (Player) e;
			int level = player.getLocation().getZ() == 0 ? 61 : 71;
			if (target.getId() == 9319 && e.getSkills().getLevel(Skills.AGILITY) < level) {
				player.getPacketDispatch().sendMessage("You need an Agility level of at least " + level + " in order to do this.");
				return true;
			}
			if (target.getId() == 10527 || target.getId() == 10528) {
				DoorActionHandler.handleAutowalkDoor(player, (GameObject) target);
				return true;
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
		register(new ZoneBorders(3401, 3527, 3459, 3585));
	}

}
