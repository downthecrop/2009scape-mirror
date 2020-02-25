package plugin.skill.magic;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

/**
 * 
 * For house teleportation.
 * 
 * @author Jamix77
 *
 */
public class HouseTeleportPlugin extends MagicSpell {
	
	public HouseTeleportPlugin(final int level, final double experience, final Item... items) {
		super(SpellBook.MODERN, level, experience, null, null, null, items);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		System.out.println("Casting house tele");
		if (((Player)entity).getHouseManager().getLocation().getExitLocation() == null) {
			((Player)entity).sendMessage("You must have a house to teleport to before attempting that.");
			return false;
		}
		Location location = ((Player)entity).getHouseManager().getLocation().getExitLocation();
		if (entity.getLocks().isTeleportLocked() || !super.meetsRequirements(entity, true, false)) {
			return false;
		}
		if (entity.getTeleporter().send(location.transform(0, RandomFunction.random(3), 0), TeleportType.NORMAL)) {
			
			entity.setAttribute("teleport:items", super.runes);
			entity.setAttribute("magic-delay", GameWorld.getTicks() + 5);
			return true;
		}
		return false;
	}

}
